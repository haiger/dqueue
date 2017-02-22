package com.github.haiger.dqueue.server;

/**
 * @author haiger
 * @since 2017年2月15日 下午4:21:43
 */
public class DQueueConstant {
    public static final String REDIS_STORE_TYPE = "redis";
    public static final String FILE_STORE_TYPE = "file";
    
    public static final String DEFAULT_HTTP_PORT = "8001";
    public static final String DEFAULT_TCP_PORT = "8000";
    
    public static final int DEFAULT_BUCKET_COUNT = 5;
    public static final int MAX_BUCKET_COUNT = 16;
    
    public static final String BUCKET_KEYS_PREFIX = "delay_bucket_";
    public static final String META_POOL_KEY_PREFIX = "meta_";
    public static final String READY_QUEUE_PREFIX = "ready_";
    
    public static final String REDIS_LOCK_KEY_SUFFIX = "_lock";
    public static final long DEFAULT_LOCK_TIME = 10*1000;
    
    public static final int DEFAULT_REDIS_KEY_EXPIRE = 2592000;// 15*24*60*60;
    
    public static final int DEFAULT_TTR = 60;
    
    public static final int DEFAULT_PAGE_SIZE = 100;
}
