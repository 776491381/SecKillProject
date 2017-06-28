package org.seckill.dao;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by fyy on 6/28/17.
 */
public class JedisTest {

    @Test
    public void TestJedis (){
        JedisPool jedisPool = new JedisPool("123.206.101.70",6379);
        Jedis jedis = jedisPool.getResource();
        jedis.auth("123456");
        System.out.println(jedis.ping());
    }



}
