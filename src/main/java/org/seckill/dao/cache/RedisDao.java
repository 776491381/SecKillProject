package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 添加Redis缓存
 * Created by fyy on 6/28/17.
 */

public class RedisDao {

    private final JedisPool jedisPool;
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId) {

        try {
            Jedis jedis = jedisPool.getResource();
            jedis.auth("123456");
            try {
                String key = "seckill:" + seckillId;
                //并没有实现哪部序列化操作
                //采用自定义序列化
                //protostuff: pojo.
                //缓存重获取到
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception ignore) {

        }

        return null;
    }


    public String putSeckill(Seckill seckill) {

        try {
            Jedis jedis = jedisPool.getResource();
            jedis.auth("123456");
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE/*添加缓冲器*/));
                //超时缓存
                int timeout = 60 * 60;//1小时

                return jedis.setex(key.getBytes(), timeout, bytes);
            } finally {
                jedis.close();
            }

        } catch (Exception ignore) {

        }

        return null;
    }

}
