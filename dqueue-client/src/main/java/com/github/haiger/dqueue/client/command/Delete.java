package com.github.haiger.dqueue.client.command;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.haiger.dqueue.client.entity.Property;

public class Delete implements DQueueCommand {
    private String key;
    
    public Delete(String key) {
        this.key = key;
    }
    
    @Override
    public String getCommandString() {
        Map<String, Object> map = new HashMap<String, Object>(5);
        map.put(Property.TYPE.getCode(), CommandType.DELETE.getCode());
        map.put(Property.KEY.getCode(), key);
        return JSON.toJSONString(map);
    }
}
