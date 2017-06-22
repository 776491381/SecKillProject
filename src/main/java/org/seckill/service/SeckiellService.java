package org.seckill.service;

import org.apache.ibatis.annotations.Param;
import org.seckill.dto.Exposer;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillExecution;
import org.seckill.exception.SeckillCloseExecution;
import org.seckill.exception.SeckillExecution;

import java.util.List;

/**
 * 站在'使用者'角度设计接口
 * 包含三个方面：方法定义粒度，参数，返回类型
 * Created by fyy on 6/22/17.
 */
public interface SeckiellService {

    /**
     * 返回所有秒杀详细
     *
     * @return List
     */
    List<Seckill> getSeckillList();

    /**
     * 返回单个秒杀详细
     *
     * @param secKillId
     * @return Seckill
     */
    Seckill getById(@Param("secKillId") long secKillId);


    /**
     * 秒杀开启后输出秒杀接口地址，否则输出系统时间和秒杀开始时间
     *
     * @param secKillId
     */
    Exposer exportSeckillUrl(long secKillId);

    /**
     * 用于执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SecurityException excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillExecution, SeckillCloseExecution, RepeatKillExecution;


}
