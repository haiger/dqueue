package com.github.haiger.dqueue.server.processer;

import java.util.concurrent.atomic.AtomicInteger;

import com.github.haiger.dqueue.server.DQueueConstant;

/**
 * @author haiger
 * @since 2017年2月15日 下午3:34:46
 */
public class DelayBucket {
    private int bucketCount;
    private String[] bucketKeys;
    private AtomicInteger index;
    
    public DelayBucket(int bucketCount) {
        this.bucketCount = bucketCount <= 0 ? 
                DQueueConstant.DEFAULT_BUCKET_COUNT : 
                    bucketCount > DQueueConstant.MAX_BUCKET_COUNT ? 
                            DQueueConstant.MAX_BUCKET_COUNT : bucketCount;
        bucketKeys = new String[this.bucketCount];
        index = new AtomicInteger(0);
    }
    
    public void init() {
        for (int i = 0; i < bucketCount; i++) {
            bucketKeys[i] = DQueueConstant.BUCKET_KEYS_PREFIX + i;
        }
    }
    
    public String nextBucket() {
        return bucketKeys[Math.abs(index.getAndIncrement() % bucketCount)];
    }
    
    public int getBucketCount() {
        return this.bucketCount;
    }
    
    public String[] getBucketKeys() {
        return this.bucketKeys;
    }
}
