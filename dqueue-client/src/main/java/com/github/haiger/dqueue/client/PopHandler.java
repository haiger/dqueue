package com.github.haiger.dqueue.client;

import com.github.haiger.dqueue.client.entity.Response;

public interface PopHandler {
    void dealJob(Response response) throws Throwable;
}
