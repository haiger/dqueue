package com.github.haiger.dqueue.server.store;

import com.github.haiger.dqueue.common.protocol.Message;

/**
 * @author haiger
 * @since 2017年1月5日 上午9:16:26
 */
public interface MetaPool {
    void save(Message message); 
    Message getMetaByTopicAndId(String topic, String messageId);
    void deleteByTopicAndId(String topic, String messageId);
}
