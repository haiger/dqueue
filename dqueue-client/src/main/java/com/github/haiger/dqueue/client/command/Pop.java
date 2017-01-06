package com.github.haiger.dqueue.client.command;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.haiger.dqueue.client.entity.Property;

public class Pop implements DQueueCommand {
    private String channel;
    
    public Pop(String channel) {
        this.channel = channel;
    }
    
    @Override
    public String getCommandString() {
        Map<String, Object> map = new HashMap<String, Object>(5);
        map.put(Property.TYPE.getCode(), CommandType.POP.getCode());
        map.put(Property.CHANNEL.getCode(), channel);
        return JSON.toJSONString(map);
    }
}
