package com.github.haiger.dqueue.common.util.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * @author haiger
 * @since 2017年2月12日 下午9:24:38
 */
public class RedisLock {
    private static final Logger log = LoggerFactory.getLogger(RedisLock.class);

    public static boolean lock(String lockKey, long expire) {
        boolean locked = false;
        String expireStr = String.valueOf(expire);
        
        Jedis jedis = RedisUtil.getInstance().getJedis();
        try {
            if (jedis.setnx(lockKey, expireStr) > 0) {
                locked = true;
            } else {
                String currentExpireStr = jedis.get(lockKey);
                if (currentExpireStr != null &&
                        Long.valueOf(currentExpireStr) < System.currentTimeMillis()) {
                    
                    String oldExpireStr = jedis.getSet(lockKey, expireStr);
                    if (oldExpireStr != null && oldExpireStr.equals(currentExpireStr)) {
                        locked = true;
                    } else {
                        locked = false;
                    }
                } else {
                    locked = false;
                }
            }
        } catch (Exception e) {
            log.error("Redis get lock exception,key is:" + lockKey, e);
            locked = false;
        } finally {
            RedisUtil.getInstance().releaseJedis(jedis);
        }
        return locked;
    }

    public static void unLock(String lockKey, long expire) {
        Jedis jedis = RedisUtil.getInstance().getJedis();
        try {
            String currentExpireStr = jedis.get(lockKey);
            if (currentExpireStr != null && Long.valueOf(currentExpireStr) == expire) {
                jedis.del(lockKey);
            }
        } catch (Exception e) {
            log.error("Redis del lock exception,key is:" + lockKey, e);
        } finally {
            RedisUtil.getInstance().releaseJedis(jedis);
        }
    }
}
