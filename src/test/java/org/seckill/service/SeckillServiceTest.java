package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKillDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by fyy on 6/24/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    //    private SeckillDao seckillDao;
//    private SuccessKillDao successKillDao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        System.out.println();
        System.out.println(list);
    }

    @Test
    public void getById() throws Exception {
        int id = 1000;
        Seckill seckill = seckillService.getById(id);
        System.out.println(seckill.toString());
    }

//    @Test
//    public void exportSeckillUrl() throws Exception {
//        int id = 1000;
//        Exposer exposer = seckillService.exportSeckillUrl(id);
//        System.out.println(exposer.toString());
//    }
//
//    @Test
//    public void excuteSeckill() throws Exception {
//        int id = 1000;
//        long phone = 13282123333L;
//        String md5 = "e809ff5939c886349d1c81efb5e8fb58";
//        try {
//            SeckillExecution seckillExecution = seckillService.excuteSeckill(id, phone, md5);
//            System.out.println(seckillExecution.toString());
//        } catch (SeckillCloseException e1) {
////            throw e1;
//            System.out.println("seckill is closed");
//        } catch (RepeatKillException e2) {
////            throw e2;
//            System.out.println("seckill repeated");
//        }
//
//    }

    //完整逻辑
    @Test
    public void testSeckillLoginAndExcute(){
        int id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        System.out.println("\n"+exposer.toString());

        if(exposer.isExposed()){
            logger.info("exposer = {}",exposer);
            long phone = 13282123333L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.excuteSeckill(id, phone, md5);
                logger.info("result={}",seckillExecution);
            } catch (SeckillCloseException e1) {
                logger.error(e1.getMessage());
            } catch (RepeatKillException e2) {
                logger.error(e2.getMessage());
            }
        }else {
            logger.warn("exposer={}",exposer);
        }



    }


}