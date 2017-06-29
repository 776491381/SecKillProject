package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKillDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SeckillService实现类
 * Created by fyy on 6/24/17.
 */

@Service
public class SeckillServiceImpl implements SeckillService {

    //注入service依赖
    @Autowired
    private SeckillDao seckillDao;
    @Autowired //resource inject (j2ee规范注解)
    private SuccessKillDao successKillDao;
    @Autowired
    private RedisDao redisDao;
    //日志
//    private Logger logger = LoggerFactory.getLogger(this.getClass());


    //加入混淆，越复杂越好,md5盐
    private final String salt = "qnfoqhfwsvnjdjoishgoajdejk%^@#Sf";

    public SeckillServiceImpl() {
    }

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 5);
    }

    public Seckill getById(long secKillId) {
        return seckillDao.queryById(secKillId);
    }

    /**
     * 输出秒杀地址
     *
     * @param secKillId
     * @return
     */
    public Exposer exportSeckillUrl(long secKillId) {

        //添加redis优化高并发
        Seckill seckill = redisDao.getSeckill(secKillId);
        if (seckill == null) {
            seckill = seckillDao.queryById(secKillId);
            if (seckill == null) {
                //无商品记录
                return new Exposer(false, secKillId);
            } else {
                System.out.println("enter redis");
                redisDao.putSeckill(seckill);
            }

        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (startTime.getTime() > nowTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, secKillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        String md5 = getMD5(secKillId);//TODO
        return new Exposer(true, md5, secKillId);
    }

    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws SeckillCloseException
     * @throws RepeatKillException
     */
    @Transactional
    public SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, SeckillCloseException, RepeatKillException {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行逻辑 ： 减库存 + 记录购买行为
        try {
            //记录购买行为
            int insertCount = successKillDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {

                int updateCount = seckillDao.reduceNumber(seckillId, new Date());
                //秒杀结束了
                if (updateCount <= 0) {
                    //Rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功
                    //Commit
                    SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }

            }
            //减库存

        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            System.out.println(e);
            //所有其他异常（编译器异常转化为运行期异常
            throw new SeckillException("seckill inner error");
        }
    }

    public SeckillExecution excuteSeckillProcedure(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        try {
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled sk = successKillDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
            } else {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception ignore) {
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }


    }


    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
