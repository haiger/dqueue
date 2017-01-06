package com.github.haiger.dqueue.common.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haiger
 * @since 2017年1月7日 上午12:41:18
 */
public enum ResponseType {
    NORMAL(0), ERROR(1), HEARTBEAT(2), MESSAGE(3),;
    private static Map<Integer, ResponseType> mappings;

    static {
        mappings = new HashMap<Integer, ResponseType>();
        for (ResponseType t : ResponseType.values()) {
            mappings.put(t.getCode(), t);
        }
    }

    private int code;

    private ResponseType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ResponseType fromCode(int code) {
        return mappings.get(code);
    }
}
