package com.github.haiger.dqueue.server.processer;

import com.github.haiger.dqueue.common.protocol.Request;
import com.github.haiger.dqueue.common.protocol.Response;

/**
 * @author haiger
 * @since 2017年2月22日 下午5:16:34
 */
public abstract class AbstractProcesser {
    protected abstract Response verify(Request request);
    
    protected boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
