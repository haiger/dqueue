package com.github.haiger.dqueue.client.command;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.haiger.dqueue.client.entity.Property;

public class Finish implements DQueueCommand {
    private String key;
    
    public Finish(String key) {
        this.key = key;
    }
    
    @Override
    public String getCommandString() {
        Map<String, Object> map = new HashMap<String, Object>(5);
        map.put(Property.TYPE.getCode(), CommandType.FINISH.getCode());
        map.put(Property.KEY.getCode(), key);
        return JSON.toJSONString(map);
    }
}
