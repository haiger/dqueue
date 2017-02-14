package com.github.haiger.dqueue.common.protocol;

import java.io.Serializable;

/**
 * @author haiger
 * @since 2017年1月5日 下午1:50:04
 */
public class Request implements Serializable{
    private static final long serialVersionUID = -8201271544089897348L;
    
    private String code;
    //private short version;// 协议版本
    private int id;
    private long timeout;
    private Message message;
    
    public Request(){}
    
    public Request(String code, int id, long timeout, Message message) {
        this.code = code;
        this.id = id;
        this.timeout = timeout;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
