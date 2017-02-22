package com.github.haiger.dqueue.server.store.file;

import java.util.List;

import com.github.haiger.dqueue.server.store.DelayQueue;

/**
 * @author haiger
 * @since 2017年1月10日 下午5:00:10
 */
public class FileDelayQueue implements DelayQueue {

    @Override
    public void save(String bucketKey, String topic, String messageId, int delayAt) {
    }

    @Override
    public List<String> findReadyByPage(String bucketKey, int pageSize) {
        return null;
    }

    @Override
    public void delete(String topicMessageId, String... messageIds) {
        
    }

}
