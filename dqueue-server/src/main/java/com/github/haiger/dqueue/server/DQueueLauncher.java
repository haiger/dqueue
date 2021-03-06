package com.github.haiger.dqueue.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.common.util.redis.RedisUtil;
import com.github.haiger.dqueue.server.processer.ProcesserFactory;
import com.github.haiger.dqueue.server.remoting.Server;
import com.github.haiger.dqueue.server.remoting.http.HttpServer;
import com.github.haiger.dqueue.server.store.StoreManager;
import com.github.haiger.dqueue.server.store.redis.DelayBucket;
import com.github.haiger.dqueue.server.store.redis.DelayTimer;

/**
 * @author haiger
 * @since 2017年1月5日 上午8:38:14
 */
public class DQueueLauncher {
    private static final Logger log = LoggerFactory.getLogger(DQueueLauncher.class);
    
    public static void main(String[] args) {
        try {
            DQueueConfig config = new DQueueConfig();
            config.init();
            
            RedisUtil.getInstance();
            
            DelayBucket bucket = new DelayBucket(config.getBucketCount());
            bucket.init();
            
            DelayTimer timer = new DelayTimer(bucket);
            timer.start();
            
            StoreManager.getInstance().setStoreType(config.getStoreType());
            StoreManager.getInstance().setDelayBucket(bucket);
            
            ProcesserFactory processerFactory =  new ProcesserFactory();
            processerFactory.init();
            
            Server httpServer = new HttpServer(config.getHttpPort(), processerFactory);
            
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    httpServer.shutdown();
                    timer.stop();
                }
            });
            
            httpServer.start();
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("DQueueLaucher goes wrong at:{}", e);
            System.exit(1);
        }
    }

}
