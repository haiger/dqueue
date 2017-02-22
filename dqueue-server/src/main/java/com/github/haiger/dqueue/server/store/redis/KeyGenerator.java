package com.github.haiger.dqueue.server.store.redis;

import com.github.haiger.dqueue.common.protocol.Message;
import com.github.haiger.dqueue.server.DQueueConstant;

/**
 * @author haiger
 * @since 2017年2月15日 下午3:53:05
 */
public class KeyGenerator {
    public static String metaKey(Message message) {
        return DQueueConstant.META_POOL_KEY_PREFIX + message.getTopic() + "_" + message.getId();
    }
}
