package com.github.haiger.dqueue.client.exception;

public class DQueueException extends Exception {
    private static final long serialVersionUID = -6425413438365049434L;

    public DQueueException(String string) {
        super(string);
    }
    
    public DQueueException(Throwable t) {
        super(t);
    }
}
