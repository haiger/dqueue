package com.github.haiger.dqueue.server.processer;

import com.github.haiger.dqueue.common.protocol.Request;
import com.github.haiger.dqueue.common.protocol.RequestCode;
import com.github.haiger.dqueue.common.protocol.Response;

/**
 * @author haiger
 * @since 2017年1月5日 下午2:13:02
 */
@Processer(code = RequestCode.PUB)
public class PubProcesser implements RequestProcesser {

    @Override
    public Response processer(Request request) {
        return null;
    }

}
