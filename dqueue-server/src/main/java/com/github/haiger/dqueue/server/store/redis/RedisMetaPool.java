package com.github.haiger.dqueue.server.store.redis;

import com.github.haiger.dqueue.common.protocol.Message;
import com.github.haiger.dqueue.common.protocol.codec.JsonCodec;
import com.github.haiger.dqueue.common.util.redis.RedisUtil;
import com.github.haiger.dqueue.server.DQueueConstant;
import com.github.haiger.dqueue.server.store.MetaPool;

import redis.clients.jedis.Jedis;

/**
 * @author haiger
 * @since 2017年1月5日 上午9:17:42
 */
public class RedisMetaPool implements MetaPool {

    @Override
    public void save(Message message) {
        Jedis jedis = RedisUtil.getInstance().getJedis();
        try {
            jedis.setex(KeyGenerator.metaKey(message), 
                    DQueueConstant.DEFAULT_REDIS_KEY_EXPIRE,
                    JsonCodec.encodeMessage(message));
        } finally {
            RedisUtil.getInstance().releaseJedis(jedis);
        }
    }

    @Override
    public Message getMetaByTopicAndId(String topic, String messageId) {
        Message message = null;
        Jedis jedis = RedisUtil.getInstance().getJedis();
        try {
            String messageStr = jedis.get(KeyGenerator.metaKey(topic, messageId));
            message = JsonCodec.decodeMessage(messageStr);
        } finally {
            RedisUtil.getInstance().releaseJedis(jedis);
        }
        return message;
    }

    @Override
    public void deleteByTopicAndId(String topic, String messageId) {
        Jedis jedis = RedisUtil.getInstance().getJedis();
        try {
            jedis.del(KeyGenerator.metaKey(topic, messageId));
        } finally {
            RedisUtil.getInstance().releaseJedis(jedis);
        }
    }

}
