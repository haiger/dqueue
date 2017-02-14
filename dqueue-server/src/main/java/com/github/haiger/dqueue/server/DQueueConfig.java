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
    
    public static final String REDIS_STORE_TYPE = "redis";
    public static final String FILE_STORE_TYPE = "file";
    
    private static final String DEFAULT_HTTP_PORT = "8001";
    private static final String DEFAULT_TCP_PORT = "8000";
    
    private String storeType;
    private int httpPort;
    private int tcpPort;
    
    public void init() {
        Properties prop = new Properties();
        try {
            prop.load(DQueueConfig.class.getResourceAsStream("/dqueue.properties"));
            
            this.storeType = prop.getProperty("storeType", REDIS_STORE_TYPE);
            this.httpPort = Integer.valueOf(prop.getProperty("httpPort", DEFAULT_HTTP_PORT));
            this.tcpPort = Integer.valueOf(prop.getProperty("tcp", DEFAULT_TCP_PORT));
        } catch (IOException e) {
            log.error("读取dqueue配置文件异常:", e);
        }
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
}
