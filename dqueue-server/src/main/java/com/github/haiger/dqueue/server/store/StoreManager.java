package com.github.haiger.dqueue.server.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.common.protocol.Message;
import com.github.haiger.dqueue.common.protocol.Response;
import com.github.haiger.dqueue.common.protocol.ResponseType;
import com.github.haiger.dqueue.common.protocol.codec.JsonCodec;
import com.github.haiger.dqueue.server.DQueueConstant;
import com.github.haiger.dqueue.server.store.file.FileDelayQueue;
import com.github.haiger.dqueue.server.store.file.FileMetaPool;
import com.github.haiger.dqueue.server.store.file.FileReadyQueue;
import com.github.haiger.dqueue.server.store.redis.DelayBucket;
import com.github.haiger.dqueue.server.store.redis.RedisDelayQueue;
import com.github.haiger.dqueue.server.store.redis.RedisMetaPool;
import com.github.haiger.dqueue.server.store.redis.RedisReadyQueue;

/**
 * @author haiger
 * @since 2017年2月15日 下午5:14:29
 */
public class StoreManager {
    private static final Logger log = LoggerFactory.getLogger(StoreManager.class);
    
    private String storeType;
    private DelayBucket bucket;
    
    private StoreManager(){}
    
    public static StoreManager getInstance() {
        return StoreManagerHolder.instance;
    }

    private static class StoreManagerHolder {
        static final StoreManager instance = new StoreManager();
    }
    
    public Response saveMeta(Message message) {
        int currentSecond = getCurrentSecond();
        int delayAt = currentSecond + message.getDelay();
        message.setDelay(delayAt);
        
        Response response = new Response();
        MetaPool metaPool = getMetaPool();
        ReadyQueue readyQueue = getReadyQueue();
        DelayQueue delayQueue = getDelayQueue();
        try {
            metaPool.save(message);
            
            if (delayAt <= currentSecond) {
                readyQueue.push(message.getTopic(), message.getId());
            } else {
                delayQueue.save(bucket.nextBucket(), 
                        message.getTopic(), 
                        message.getId(), 
                        delayAt);
            }
            
            response.setType(ResponseType.NORMAL.getType());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setType(ResponseType.ERROR.getType());
            response.setSuccess(false);
            response.setErrorInfo(e.getMessage());
            log.error("saveMeta({}) goes wrong at:{}", 
                    JsonCodec.encodeMessage(message), e.getMessage());
        }
        return response;
    }
    
    public void toReadyQueue(String bucketKey) {
        DelayQueue delayQueue = getDelayQueue();
        delayQueue.toReady(bucketKey);
    }
    
    private int getCurrentSecond() {
        return Long.valueOf(System.currentTimeMillis() / 1000).intValue();
    }
    
    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }
    
    public void setDelayBucket(DelayBucket bucket) {
        this.bucket = bucket;
    }
    
    private MetaPool getMetaPool() {
        if (storeType.equals(DQueueConstant.REDIS_STORE_TYPE)) {
            return new RedisMetaPool();
        } else if (storeType.equals(DQueueConstant.FILE_STORE_TYPE)) {
            return new FileMetaPool();
        }
        return null;
    }
    
    private DelayQueue getDelayQueue() {
        if (storeType.equals(DQueueConstant.REDIS_STORE_TYPE)) {
            return new RedisDelayQueue();
        } else if (storeType.equals(DQueueConstant.FILE_STORE_TYPE)) {
            return new FileDelayQueue();
        }
        return null;
    }
    
    private ReadyQueue getReadyQueue() {
        if (storeType.equals(DQueueConstant.REDIS_STORE_TYPE)) {
            return new RedisReadyQueue();
        } else if (storeType.equals(DQueueConstant.FILE_STORE_TYPE)) {
            return new FileReadyQueue();
        }
        return null;
    }
}
