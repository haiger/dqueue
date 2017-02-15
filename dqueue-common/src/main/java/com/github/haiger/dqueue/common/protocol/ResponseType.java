package com.github.haiger.dqueue.common.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haiger
 * @since 2017年1月7日 上午12:41:18
 */
public enum ResponseType {
    NORMAL((short)0), ERROR((short)1), HEARTBEAT((short)2), MESSAGE((short)3),;
    private static Map<Short, ResponseType> mappings;

    static {
        mappings = new HashMap<Short, ResponseType>();
        for (ResponseType t : ResponseType.values()) {
            mappings.put(t.getType(), t);
        }
    }

    private short type;

    private ResponseType(short type) {
        this.type = type;
    }

    public short getType() {
        return type;
    }

    public static ResponseType fromType(int type) {
        return mappings.get(type);
    }
}
