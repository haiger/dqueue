package com.github.haiger.dqueue.client.entity;

public enum Property {
    TYPE("type"), CHANNEL("channel"), KEY("key"), VALUE("value"), 
    INTERVAL("interval"), RESERVEDTIMEOUT("reservedTimeout");

    private String code;

    private Property(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
