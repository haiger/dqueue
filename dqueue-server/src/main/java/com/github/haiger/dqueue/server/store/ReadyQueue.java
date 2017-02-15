package com.github.haiger.dqueue.server.store;

import java.util.List;

/**
 * @author haiger
 * @since 2017年1月5日 上午9:16:58
 */
public interface ReadyQueue {
    boolean push(String topic, String messageId);
    String popByTopic(String topic);
    List<String> mpopByTopic(String topic);
}
