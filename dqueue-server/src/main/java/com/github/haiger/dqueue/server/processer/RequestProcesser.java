package com.github.haiger.dqueue.server.processer;

import java.util.Map;

import com.github.haiger.dqueue.common.protocol.Response;

/**
 * @author haiger
 * @since 2017年2月14日 下午10:14:56
 */
public interface RequestProcesser {
    Response processer(Map<String, String> params);
}
