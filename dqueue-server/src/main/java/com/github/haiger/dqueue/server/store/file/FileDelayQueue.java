package com.github.haiger.dqueue.server.store.file;

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
    public void toReady(String bucketKey) {
        
    }

}
