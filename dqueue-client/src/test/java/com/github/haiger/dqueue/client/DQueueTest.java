package com.github.haiger.dqueue.client;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.github.haiger.dqueue.client.entity.Job;
import com.github.haiger.dqueue.client.entity.Response;

public class DQueueTest {
    private DQueue dq = new DQueue("http://192.168.66.202:8991/json");
    //@Test
    public void testAdd() {
        try {
            Job job = new Job();
            job.setChannel("haiger_test1");
            job.setKey("haiger12345678903");
            job.setValue("海红你好3");
            job.setInterval(30);
            job.setReservedTimeout(30);
            
            System.out.println(JSON.toJSONString(dq.add(job)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //@Test
    public void testPop() {
        try {
            System.out.println(JSON.toJSONString(dq.popOne("haiger_test1")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPopWithThread() {
        dq.popAlways("re_allot_fans", new PopHandler() {
            
            @Override
            public void dealJob(Response response) throws Throwable {
                if (response.isSuccess()) System.out.println(JSON.toJSONString(response));
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        try {
            Thread.sleep(120*1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //@Test
    public void testFinish() {
        try {
            System.out.println(JSON.toJSONString(dq.finish("haiger1234567890")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //@Test
    public void testDelete() {
        try {
            System.out.println(JSON.toJSONString(dq.delete("haiger1234567890")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
