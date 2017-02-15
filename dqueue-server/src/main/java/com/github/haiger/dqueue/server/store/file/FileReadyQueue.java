package com.github.haiger.dqueue.server.store.file;

import java.util.List;

import com.github.haiger.dqueue.server.store.ReadyQueue;

/**
 * @author haiger
 * @since 2017年1月10日 下午5:00:32
 */
public class FileReadyQueue implements ReadyQueue {

    @Override
    public boolean push(String topic, String messageId) {
        return false;
    }

    @Override
    public String popByTopic(String topic) {
        return null;
    }

    @Override
    public List<String> mpopByTopic(String topic) {
        return null;
    }

}
