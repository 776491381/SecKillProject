package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by fyy on 6/21/17.
 */
public interface SeckillDao {


        int reduceNumber(@Param("seckillId") long seckillId , @Param("killTime") Date killTime);

        Seckill queryById(@Param("seckillId") long seckillId);

        List<Seckill> queryAll (@Param("offset") int offet , @Param("limit") int limit);

}
