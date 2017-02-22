package com.github.haiger.dqueue.server.store.file;

import com.github.haiger.dqueue.common.protocol.Message;
import com.github.haiger.dqueue.server.store.MetaPool;

/**
 * @author haiger
 * @since 2017年1月10日 下午4:54:36
 */
public class FileMetaPool implements MetaPool {

    @Override
    public void save(Message message) {
        
    }

    @Override
    public Message getMetaByTopicAndId(String topic, String messageId) {
        return null;
    }

    @Override
    public void deleteByTopicAndId(String topic, String messageId) {
        
    }
}
