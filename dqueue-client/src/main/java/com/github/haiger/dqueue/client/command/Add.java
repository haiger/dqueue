package com.github.haiger.dqueue.client.command;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.haiger.dqueue.client.entity.Job;
import com.github.haiger.dqueue.client.entity.Property;

public class Add implements DQueueCommand {
    private Job job;
    
    public Add(Job job) {
        this.job = job;
    }
    
    @Override
    public String getCommandString() {
        Map<String, Object> map = new HashMap<String, Object>(10);
        map.put(Property.TYPE.getCode(), CommandType.ADD.getCode());
        Job2Map(job, map);
        return JSON.toJSONString(map);
    }
    
    private void Job2Map(Job job, Map<String, Object> map) {
        map.put(Property.CHANNEL.getCode(), job.getChannel());
        map.put(Property.KEY.getCode(), job.getKey());
        map.put(Property.VALUE.getCode(), job.getValue());
        map.put(Property.INTERVAL.getCode(), job.getInterval());
        map.put(Property.RESERVEDTIMEOUT.getCode(), job.getReservedTimeout());
    }
}