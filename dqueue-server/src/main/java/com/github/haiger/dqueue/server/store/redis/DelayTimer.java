package com.github.haiger.dqueue.server.store.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.common.util.NamedThreadFactory;
import com.github.haiger.dqueue.common.util.redis.RedisLock;
import com.github.haiger.dqueue.server.DQueueConstant;
import com.github.haiger.dqueue.server.store.StoreManager;

/**
 * @author haiger
 * @since 2017年1月5日 下午2:12:18
 */
public class DelayTimer {
    private static final Logger log = LoggerFactory.getLogger(DelayTimer.class);
    
    private ExecutorService timerExecutor;
    private DelayBucket bucket;
    private int taskCount;
    private List<DelayTask> taskList;
    
    public DelayTimer(DelayBucket bucket) {
        this.bucket = bucket;
        this.taskCount = bucket.getBucketCount();
        timerExecutor = new ThreadPoolExecutor(taskCount, taskCount, 10L, TimeUnit.SECONDS, 
                new ArrayBlockingQueue<Runnable>(1), new NamedThreadFactory("DelayTimer_task_"));
        taskList = new ArrayList<>(taskCount);
    }
    
    public void start() {
        String[] bucketKeys = bucket.getBucketKeys();
        for (int i = 0; i < taskCount; i++) {
            DelayTask task = new DelayTask(bucketKeys[i]);
            taskList.add(task);
            timerExecutor.execute(task);
        }
    }
    
    public void stop() {
        for (DelayTask task : taskList) {
            task.stop();
        }
    }

    private class DelayTask implements Runnable {
        private String bucketKey;
        private AtomicBoolean isRunning;
        
        public DelayTask(String bucketKey) {
            this.bucketKey = bucketKey;
            this.isRunning = new AtomicBoolean(true);
        }

        @Override
        public void run() {
            log.info("delayTask({}) start.", bucketKey);
            while (isRunning.get()) {
                String lockKey = bucketKey + DQueueConstant.REDIS_LOCK_KEY_SUFFIX;
                long expire = System.currentTimeMillis() + DQueueConstant.DEFAULT_LOCK_TIME;
                boolean locked = RedisLock.lock(lockKey, expire);
                try {
                    if (! locked) {
                        Thread.sleep(10);
                        continue;
                    }
                    StoreManager.getInstance().toReadyQueue(bucketKey, DQueueConstant.DEFAULT_PAGE_SIZE);
                } catch (Exception e) {
                    log.error("delayTask while run goes wrong at:{}", e);
                } finally {
                    if(locked){
                        RedisLock.unlock(lockKey, expire);
                    }
                }
            }
            log.info("delayTask({}) stop.", bucketKey);
        }
        
        public void stop() {
            isRunning.getAndSet(false);
        }
        
    }
}

