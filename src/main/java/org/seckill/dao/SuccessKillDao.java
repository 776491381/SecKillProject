package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by fyy on 6/21/17.
 */
public interface SuccessKillDao {

    int insertSuccessKilled(@Param("seckillId") long seckillId , @Param("userPhone") long userPhone);

    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId , @Param("userPhone") long userPhone);




}
