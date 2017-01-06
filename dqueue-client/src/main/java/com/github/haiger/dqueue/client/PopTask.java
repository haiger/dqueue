package com.github.haiger.dqueue.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.client.entity.Response;
import com.github.haiger.dqueue.client.exception.DQueueException;

public class PopTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(PopTask.class);
    private DQueue dqueue;
    private String channel;
    private PopHandler handler;
    private boolean isRunning = true;

    public PopTask(DQueue dqueue, String channel, PopHandler handler) {
        this.dqueue = dqueue;
        this.channel = channel;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Response response = dqueue.popOne(channel);
                handler.dealJob(response);
            } catch (DQueueException e) {
                LOG.error("popOne({}) error at:{}", channel, e);
            } catch (Throwable e) {
                LOG.error("dealJob goes wrong at:{}", e);
            }
        }
    }
    
    public void stop() {
        isRunning = false;
    }

}
