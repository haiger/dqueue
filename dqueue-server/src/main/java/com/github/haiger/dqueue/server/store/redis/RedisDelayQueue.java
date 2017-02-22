package com.github.haiger.dqueue.server.store.redis;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.haiger.dqueue.common.util.redis.RedisUtil;
import com.github.haiger.dqueue.server.store.DelayQueue;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

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
    public List<String> findReadyByPage(String bucketKey, int pageSize) {
        List<String> members = new ArrayList<>(pageSize);
        Jedis j = RedisUtil.getInstance().getJedis();
        try {
            Set<Tuple> tuples = j.zrangeWithScores(bucketKey, 0, pageSize);
            long current = Instant.now().getEpochSecond();
            for (Tuple tuple : tuples) {
                double score = tuple.getScore();
                String member = tuple.getElement();
                if (isReady(current, score)) {
                    members.add(member);
                }
            }
        } finally {
            RedisUtil.getInstance().releaseJedis(j);
        }
        return members;
    }

    @Override
    public void delete(String bucketKey, String topic, String... messageIds) {
        String[] members = new String[messageIds.length];
        int i = 0;
        for (String messageId : messageIds) {
            members[i] = topic + "_" + messageId;
            i++;
        }
        Jedis j = RedisUtil.getInstance().getJedis();
        try {
            j.zrem(bucketKey, members);
        } finally {
            RedisUtil.getInstance().releaseJedis(j);
        }
    }
    
    private boolean isReady(long current, double delay) {
        return Double.valueOf(delay).longValue() <= current;
    }
}
