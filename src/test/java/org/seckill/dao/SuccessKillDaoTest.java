package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


/**
 *
 * Created by fyy on 6/21/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKillDaoTest {

    @Autowired
    private SuccessKillDao successKillDao; //autowire warning

    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1001L;
        long phone = 13282123005L;
//        SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(id,phone);
//        System.out.println(successKilled.toString());
        int returnNum = successKillDao.insertSuccessKilled(id,phone);
        SuccessKilled successKilled1 = successKillDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled1.toString());
        System.out.println(returnNum);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id = 1000L;
        long phone = 13282123005L;
        SuccessKilled successKilled = successKillDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled.toString());
//        System.out.println(successKillDao.queryByIdWithSeckill(id,phone));
    }

}