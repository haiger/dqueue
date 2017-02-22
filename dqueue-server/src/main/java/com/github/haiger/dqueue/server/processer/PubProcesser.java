package com.github.haiger.dqueue.server.processer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.common.protocol.Message;
import com.github.haiger.dqueue.common.protocol.Request;
import com.github.haiger.dqueue.common.protocol.RequestCode;
import com.github.haiger.dqueue.common.protocol.Response;
import com.github.haiger.dqueue.common.protocol.ResponseType;
import com.github.haiger.dqueue.common.protocol.codec.JsonCodec;
import com.github.haiger.dqueue.server.DQueueConstant;
import com.github.haiger.dqueue.server.store.StoreManager;

/**
 * @author haiger
 * @since 2017年1月5日 下午2:13:02
 */
@Processer(code = RequestCode.PUB)
public class PubProcesser extends AbstractProcesser implements RequestProcesser {
    private static final Logger log = LoggerFactory.getLogger(PubProcesser.class);

    @Override
    public Response processer(Request request) {
        Response response = verify(request);
        if (response == null || ! response.isSuccess()) {
            return response;
        }
        try {
            return StoreManager.getInstance().saveMeta(request.getMessage());
        } catch (Throwable e) {
            response.setType(ResponseType.ERROR.getType());
            response.setSuccess(false);
            response.setErrorInfo(e.getMessage());
            log.error("PubProcesser({}) goes wrong at:{}", 
                    JsonCodec.encodeRequest(request), e.getMessage());
        }
        return response;
    }

    @Override
    protected Response verify(Request request) {
        Message message = request.getMessage();
        Response response = new Response();
        response.setType(ResponseType.ERROR.getType());
        response.setSuccess(false);
        
        if (message == null) {
            response.setErrorInfo("message struct is not supported.");
        } else if (isNullOrEmpty(message.getTopic())) {
            response.setErrorInfo("message topic is null.");
        } else if (isNullOrEmpty(message.getId())) {
            response.setErrorInfo("message id is null.");
        } else if (isNullOrEmpty(message.getBody())) {
            response.setErrorInfo("message body is null.");
        } else if (message.getTtr() <= 0) {
            request.getMessage().setTtr(DQueueConstant.DEFAULT_TTR);
        } else {
            response.setSuccess(true);
        }
        
        return response;
    }

}
