package com.github.haiger.dqueue.common.protocol;

/**
 * @author haiger
 * @since 2017年1月5日 上午9:20:56
 */
public class Message {
    private String id;
    private String topic;
    private int delay;
    private int ttr;
    private String body;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public int getDelay() {
        return delay;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public int getTtr() {
        return ttr;
    }
    
    public void setTtr(int ttr) {
        this.ttr = ttr;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}
