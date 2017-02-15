package com.github.haiger.dqueue.server.store;

/**
 * @author haiger
 * @since 2017年1月5日 上午9:16:44
 */
public interface DelayQueue {
    void save(String bucketKey, String topic, String messageId, int delayAt);
    void toReady(String bucketKey);
}
