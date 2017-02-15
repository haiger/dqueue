package com.github.haiger.dqueue.server.store.redis;

import com.github.haiger.dqueue.common.util.redis.RedisUtil;
import com.github.haiger.dqueue.server.store.DelayQueue;

import redis.clients.jedis.Jedis;

/**
 * @author haiger
 * @since 2017年1月10日 下午4:58:01
 */
public class RedisDelayQueue implements DelayQueue {

    @Override
    public void save(String bucketKey, String topic, String messageId, int delayAt) {
        Jedis j = RedisUtil.getInstance().getJedis();
        try {
            j.zadd(bucketKey, delayAt, topic + "_" + messageId);
        } finally {
            RedisUtil.getInstance().releaseJedis(j);
        }
    }

    @Override
    public void toReady(String bucketKey) {
        
    }

}
