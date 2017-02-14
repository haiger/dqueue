package com.github.haiger.dqueue.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.common.util.redis.RedisUtil;
import com.github.haiger.dqueue.server.remoting.Server;
import com.github.haiger.dqueue.server.remoting.http.HttpServer;

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
            
            Server httpServer = new HttpServer(config.getHttpPort());
            
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    httpServer.shutdown();
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
