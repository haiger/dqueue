package com.github.haiger.dqueue.client.command;

public enum CommandType {
    ADD("addTask"), POP("getTask"), FINISH("finishTask"), DELETE("deleteTask");

    private String code;

    private CommandType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
