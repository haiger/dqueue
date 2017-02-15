package com.github.haiger.dqueue.common.util.redis;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author haiger
 * @since 2017年2月12日 下午9:01:07
 */
public class RedisUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);
    private JedisPool jedisPool;
    private int dbId;

    private RedisUtil() {
        init();
    }

    private void init() {
        Properties prop = new Properties();
        try {
            prop.load(RedisUtil.class.getResourceAsStream("/redis.properties"));
        } catch (IOException e) {
            log.error("读取redis配置文件异常:", e);
        }
        
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(prop.getProperty("maxTotal", "10")));
        config.setMaxIdle(Integer.valueOf(prop.getProperty("maxIdle", "8")));
        config.setMinIdle(Integer.valueOf(prop.getProperty("minIdle", "5")));
        config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("maxWait", "1000")));
        config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("testWhileIdle", "true")));
        config.setTimeBetweenEvictionRunsMillis(Long.valueOf(prop.getProperty("intervalTime", "120000")));
        config.setMinEvictableIdleTimeMillis(Long.valueOf(prop.getProperty("idleTime", "120000")));
        config.setNumTestsPerEvictionRun(Integer.valueOf(prop.getProperty("numTestsPerEvictionRun", "10")));
        jedisPool = new JedisPool(config, prop.getProperty("redis.host"),
                Integer.valueOf(prop.getProperty("redis.port")));
        dbId = Integer.valueOf(prop.getProperty("dbId", "0"));
        
        log.info("redisPool初始化成功.");
    }

    public static RedisUtil getInstance() {
        return RedisUtilHolder.instance;
    }

    private static class RedisUtilHolder {
        static final RedisUtil instance = new RedisUtil();
    }

    public Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        try {
            if (!"PONG".endsWith(jedis.ping())) {// 检测该链接是否可以,不可用就再从池里拿一个,应用端如果再出现异常可以也需要这样做一次
                jedis.close();
                jedis = jedisPool.getResource();
            }
        } catch (Exception e) {
            log.error("getJedis exeception at:{}", e);
            jedis.close();
            jedis = jedisPool.getResource();
        }
        return jedis;
    }

    public void releaseJedis(Jedis jedis) {
        jedis.close();
    }

    public void releaseBrokenJedis(Jedis jedis) {
        jedis.close();
    }

    public int getDbId() {  
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }
}
