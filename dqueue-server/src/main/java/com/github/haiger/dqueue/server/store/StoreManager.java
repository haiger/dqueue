package com.github.haiger.dqueue.server.store;

import com.github.haiger.dqueue.server.DQueueConstant;
import com.github.haiger.dqueue.server.store.file.FileDelayQueue;
import com.github.haiger.dqueue.server.store.file.FileMetaPool;
import com.github.haiger.dqueue.server.store.file.FileReadyQueue;
import com.github.haiger.dqueue.server.store.redis.RedisDelayQueue;
import com.github.haiger.dqueue.server.store.redis.RedisMetaPool;
import com.github.haiger.dqueue.server.store.redis.RedisReadyQueue;

/**
 * @author haiger
 * @since 2017年2月15日 下午5:14:29
 */
public class StoreManager {
    private String storeType;
    
    private StoreManager(){}
    
    public static StoreManager getInstance() {
        return StoreManagerHolder.instance;
    }

    private static class StoreManagerHolder {
        static final StoreManager instance = new StoreManager();
    }
    
    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }
    
    public MetaPool getMetaPool() {
        if (storeType.equals(DQueueConstant.REDIS_STORE_TYPE)) {
            return new RedisMetaPool();
        } else if (storeType.equals(DQueueConstant.FILE_STORE_TYPE)) {
            return new FileMetaPool();
        }
        return null;
    }
    
    public DelayQueue getDelayQueue() {
        if (storeType.equals(DQueueConstant.REDIS_STORE_TYPE)) {
            return new RedisDelayQueue();
        } else if (storeType.equals(DQueueConstant.FILE_STORE_TYPE)) {
            return new FileDelayQueue();
        }
        return null;
    }
    
    public ReadyQueue getReadyQueue() {
        if (storeType.equals(DQueueConstant.REDIS_STORE_TYPE)) {
            return new RedisReadyQueue();
        } else if (storeType.equals(DQueueConstant.FILE_STORE_TYPE)) {
            return new FileReadyQueue();
        }
        return null;
    }
}
