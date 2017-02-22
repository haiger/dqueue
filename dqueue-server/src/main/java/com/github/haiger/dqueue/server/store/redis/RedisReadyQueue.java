package com.github.haiger.dqueue.server.store.redis;

import java.util.List;

import com.github.haiger.dqueue.common.util.redis.RedisUtil;
import com.github.haiger.dqueue.server.store.ReadyQueue;

import redis.clients.jedis.Jedis;

/**
 * @author haiger
 * @since 2017年1月10日 下午4:59:38
 */
public class RedisReadyQueue implements ReadyQueue {

    @Override
    public boolean push(String topic, String... messageIds) {
        Jedis jedis = RedisUtil.getInstance().getJedis();
        try {
            long res = jedis.rpush(topic, messageIds);
            return res > 0 ? true : false;
        } finally {
            RedisUtil.getInstance().releaseJedis(jedis);
        }
    }

    @Override
    public String popByTopic(String topic) {
        Jedis jedis = RedisUtil.getInstance().getJedis();
        try {
            String messageId = jedis.lpop(topic);
            return messageId;
        } finally {
            RedisUtil.getInstance().releaseJedis(jedis);
        }
    }

    @Override
    public List<String> mpopByTopic(String topic) {
        return null;
    }

}
