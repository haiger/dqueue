package com.github.haiger.dqueue.common.protocol.codec;

import com.alibaba.fastjson.JSON;
import com.github.haiger.dqueue.common.protocol.Request;
import com.github.haiger.dqueue.common.protocol.Response;

/**
 * @author haiger
 * @since 2017年1月7日 上午12:12:45
 */
public class JsonCodec {
    public static String encodeRequest(Request request) {
        return JSON.toJSONString(request);
    }
    
    public static Request decodeRequest(String requestStr) {
        return JSON.parseObject(requestStr, Request.class);
    }
    
    public static String encodeResponse(Response response) {
        return JSON.toJSONString(response);
    }
    
    public static Response decodeResponse(String responseStr) {
        return JSON.parseObject(responseStr, Response.class);
    }
}
