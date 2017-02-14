package com.github.haiger.dqueue.common.protocol;

import java.util.List;

/**
 * @author haiger
 * @since 2017年1月5日 上午9:18:50
 */
public class Response {
    private short type;
    //private short version;// 协议版本
    private boolean success;
    private String errorInfo;
    private List<Message> messageList;

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
