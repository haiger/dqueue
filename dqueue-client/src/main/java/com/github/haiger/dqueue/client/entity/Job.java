package com.github.haiger.dqueue.client.entity;

public class Job {
    private String channel;
    private String key;
    private String value;
    private Integer interval;
    private Integer reservedTimeout;
    
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Integer getInterval() {
        return interval;
    }
    public void setInterval(Integer interval) {
        this.interval = interval;
    }
    public Integer getReservedTimeout() {
        return reservedTimeout;
    }
    public void setReservedTimeout(Integer reservedTimeout) {
        this.reservedTimeout = reservedTimeout;
    }
}
