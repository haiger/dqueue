package com.github.haiger.dqueue.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.github.haiger.dqueue.client.command.Add;
import com.github.haiger.dqueue.client.command.Delete;
import com.github.haiger.dqueue.client.command.Finish;
import com.github.haiger.dqueue.client.command.Pop;
import com.github.haiger.dqueue.client.entity.Job;
import com.github.haiger.dqueue.client.entity.Response;
import com.github.haiger.dqueue.client.exception.DQueueException;
import com.github.haiger.dqueue.client.util.HttpUtil;
import com.github.haiger.dqueue.client.util.NamedThreadFactory;

public class DQueue {
    private String dqueueURI;
    private ExecutorService popExecutor;
    private List<PopTask> taskList;

    public DQueue(String dqueueURI) {
        this.dqueueURI = dqueueURI;
        
        taskList = new ArrayList<PopTask>();
        popExecutor = new ThreadPoolExecutor(1, 100, 0L, TimeUnit.MILLISECONDS, 
                new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory("POP_Task_"));
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (PopTask task : taskList) {
                    task.stop();
                }
                popExecutor.shutdown();
                while (!popExecutor.isTerminated()){}
                HttpUtil.close();
            }
        });
    }

    public Response add(Job job) throws DQueueException {
        //TODO: job check
        Add add =  new Add(job);
        return getResponse(add.getCommandString());
    }

    public Response delete(String key) throws DQueueException {
        Delete delete = new Delete(key);
        return getResponse(delete.getCommandString());
    }

    public Response finish(String key) throws DQueueException {
        Finish finish = new Finish(key);
        return getResponse(finish.getCommandString());
    }
    
    public Response popOne(String channel) throws DQueueException {
        Pop pop = new Pop(channel);
        return getResponse(pop.getCommandString());
    }
    
    public void popAlways(String channel, PopHandler handler) {
        PopTask task = new PopTask(this, channel, handler);
        taskList.add(task);
        popExecutor.submit(task);
    }
    
    private Response getResponse(String commandString) throws DQueueException {
        String content = HttpUtil.post(dqueueURI, commandString);
        return JSON.parseObject(content, Response.class);
    }
}
