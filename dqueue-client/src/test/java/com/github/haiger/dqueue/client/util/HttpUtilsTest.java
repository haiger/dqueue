package com.github.haiger.dqueue.client.util;

import org.junit.Test;

import com.github.haiger.dqueue.client.exception.DQueueException;
import com.github.haiger.dqueue.client.util.HttpUtil;

public class HttpUtilsTest {

    @Test
    public void testPost() {
        try {
            String response = HttpUtil.post("http://192.168.66.202:8991/json", "{'type':'getTask','channel':'weixin_verity_ticket'}");
            System.out.println("result:"+response);
        } catch (DQueueException e) {
            e.printStackTrace();
        }
    }
}
