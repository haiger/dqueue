package com.github.haiger.dqueue.common.codec;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.haiger.dqueue.common.protocol.Message;
import com.github.haiger.dqueue.common.protocol.Request;
import com.github.haiger.dqueue.common.protocol.RequestCode;
import com.github.haiger.dqueue.common.protocol.Response;
import com.github.haiger.dqueue.common.protocol.ResponseType;
import com.github.haiger.dqueue.common.protocol.codec.JsonCodec;

/**
 * @author haiger
 * @since 2017年2月14日 下午5:45:17
 */
public class JsonCodecTest {
    private Request createRequest() {
        Message message = new Message();
        message.setId("123456");
        message.setTopic("order_pay");
        message.setDelay(1*60*60);
        message.setTtr(60);
        message.setBody("body");
        
        Request request = new Request();
        request.setCode(RequestCode.PUB.getCode());
        request.setId(1);
        request.setTimeout(1000);
        request.setMessage(message);
        
        return request;
    }
    
    private Response createResponse() {
        Message message1 = new Message();
        message1.setId("123456");
        message1.setTopic("order_pay");
        message1.setDelay(1*60*60);
        message1.setTtr(60);
        message1.setBody("body1");
        
        Message message2 = new Message();
        message2.setId("654321");
        message2.setTopic("order_pay");
        message2.setDelay(1*60*60);
        message2.setTtr(60);
        message2.setBody("body2");
        
        Response response = new Response();
        response.setType(ResponseType.MESSAGE.getCode());
        List<Message> messageList = new ArrayList<>(2);
        messageList.add(message1);
        messageList.add(message2);
        response.setMessageList(messageList);
        
        return response;
     }
    
    @Test
    public void testEncodeRequest() {
        System.out.println(JsonCodec.encodeRequest(createRequest()));
    }
    
    @Test
    public void testDecodeRequest() {
        String requestStr = "{\"code\":\"pub\",\"id\":1,\"message\":{\"body\":\"body\",\"delay\":3600,\"id\":\"123456\",\"topic\":\"order_pay\",\"ttr\":60},\"timeout\":1000}";
        Request request = JsonCodec.decodeRequest(requestStr);
        
        Assert.assertEquals("pub", request.getCode());
        System.out.println(request.getMessage().getBody());
    }
    
    @Test
    public void testCodecRequest() {
        Request request1 = createRequest();
        Request request2 = JsonCodec.decodeRequest(JsonCodec.encodeRequest(request1));
        Assert.assertEquals(request1.getMessage().getTopic(), request2.getMessage().getTopic());
    }
    
    @Test
    public void testEncodeResponse() {
        System.out.println(JsonCodec.encodeResponse(createResponse()));
    }
    
    @Test
    public void testDecodeResponse() {
        String responseStr = "{\"messageList\":[{\"body\":\"body1\",\"delay\":3600,\"id\":\"123456\",\"topic\":\"order_pay\",\"ttr\":60},"
                + "{\"body\":\"body2\",\"delay\":3600,\"id\":\"654321\",\"topic\":\"order_pay\",\"ttr\":60}],\"success\":false,\"type\":3}";
        Response response = JsonCodec.decodeResponse(responseStr);
        Assert.assertEquals(3600, response.getMessageList().get(0).getDelay());
        System.out.println(response.getType());
    }
}
