package com.github.haiger.dqueue.server.store;

import java.util.List;

/**
 * @author haiger
 * @since 2017年1月5日 上午9:16:44
 */
public interface DelayQueue {
    void save(String bucketKey, String topic, String messageId, int delayAt);
    List<String> findReadyByPage(String bucketKey, int pageSize);
    void delete(String topic, String... messageIds);
}
