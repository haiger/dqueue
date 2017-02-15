package com.github.haiger.dqueue.server;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author haiger
 * @since 2017年2月12日 下午10:19:07
 */
public class DQueueConfig {
    private static final Logger log = LoggerFactory.getLogger(DQueueConfig.class);
    
    private String storeType;
    private int httpPort;
    private int tcpPort;
    private int bucketCount;
    
    public void init() {
        Properties prop = new Properties();
        try {
            prop.load(DQueueConfig.class.getResourceAsStream("/dqueue.properties"));
        } catch (IOException e) {
            log.error("读取dqueue配置文件异常:", e);
        }
        
        this.storeType = prop.getProperty("storeType", DQueueConstant.REDIS_STORE_TYPE);
        this.httpPort = Integer.valueOf(prop.getProperty("httpPort", DQueueConstant.DEFAULT_HTTP_PORT));
        this.tcpPort = Integer.valueOf(prop.getProperty("tcp", DQueueConstant.DEFAULT_TCP_PORT));
        this.bucketCount = Integer.valueOf(prop.getProperty("bucketCount", DQueueConstant.DEFAULT_BUCKET_COUNT+""));
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getBucketCount() {
        return bucketCount;
    }

    public void setBucketCount(int bucketCount) {
        this.bucketCount = bucketCount;
    }
}
