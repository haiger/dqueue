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
            jedis.setex(DQueueConstant.META_POOL_KEY_PREFIX + message.getId(), 
                    DQueueConstant.DEFAULT_REDIS_KEY_EXPIRE,
                    JsonCodec.encodeMessage(message));
        } finally {
            RedisUtil.getInstance().releaseJedis(jedis);
        }
    }

    @Override
    public Message getMetaById(String messageId) {
        return null;
    }

    @Override
    public void deleteById(String messageId) {
        
    }

}
