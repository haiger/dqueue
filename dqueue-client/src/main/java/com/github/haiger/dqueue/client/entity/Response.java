package com.github.haiger.dqueue.client.entity;

public class Response {
    private Boolean success;
    private String error;
    private String key;
    private String value;
    private String in;
    
    public Boolean isSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
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
    public String getIn() {
        return in;
    }
    public void setIn(String in) {
        this.in = in;
    }
}
