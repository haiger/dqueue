package com.github.haiger.dqueue.common.protocol;

import java.io.Serializable;

/**
 * @author haiger
 * @since 2017年1月5日 下午1:50:04
 */
public class Request implements Serializable{
    private static final long serialVersionUID = -8201271544089897348L;
    
    private short code;
    private short codecType;// 序列化类型：json、binary...
    //private short version;// 协议版本
    private int id;
    private long timeout;
    private byte[] body;
    
    public Request(short code, short codecType, int id, long timeout, byte[] body) {
        this.code = code;
        this.codecType = codecType;
        this.id = id;
        this.timeout = timeout;
        this.body = body;
    }
}
