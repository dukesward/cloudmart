package com.web.cloudtube.core.cache.delegator;

import com.web.cloudtube.core.cache.ApplicationCacheSerializable;
import com.web.cloudtube.core.cache.CacheableEntityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;

public class JedisCacheTaskDelegator implements CacheTaskDelegator {
    static final Logger logger = LoggerFactory.getLogger(JedisCacheTaskDelegator.class);
    private JedisPool pool;

    public JedisCacheTaskDelegator(
            JedisPoolConfig config, String hostname, int port) {
        logger.debug(String.format("Intializing ApplicationCacheTaskDelegator with hostname=%s & port=%d", hostname, port));
        this.pool = new JedisPool(config, hostname, port);
    }

    public void delegateSimpleCacheTask(String cacheId, String cacheValue) {
        Jedis jedis = pool.getResource();
        jedis.set(cacheId, cacheValue);
        jedis.close();
    }

    public void pushSimpleCacheProperty(String cacheId, Map<String, String> properties) {
        Jedis jedis = pool.getResource();
        String value = jedis.get(cacheId);
        properties.put(cacheId, value);
        jedis.close();
    }

    public void delegateMapCacheTask(String cacheIdPrefix, ApplicationCacheSerializable serializable) {
        Jedis jedis = pool.getResource();
        String cacheId = cacheIdPrefix + ":" + serializable.getCacheId();
        Map<String, String> properties = serializable.toCacheProperties();
        for(Map.Entry<String, String> entry : properties.entrySet()) {
            Object cache = jedis.hset(cacheId, entry.getKey(), entry.getValue());
        }
        jedis.close();
    }

    public void pushMapCacheProperties(String cacheIdPrefix, ApplicationCacheSerializable serializable) {
        Jedis jedis = pool.getResource();
        if(serializable.getCacheId() != null) {
            String cacheId = cacheIdPrefix + ":" + serializable.getCacheId();
            Map<String, String> properties = jedis.hgetAll(cacheId);
            if(properties != null) {
                serializable.fromCacheProperties(properties);
            }
        }
        jedis.close();
    }

    @Override
    public <T extends ApplicationCacheSerializable> T pushOrUpdate(
            String cacheId, CacheableEntityTask<T> task) {
        return null;
    }

    @Override
    public <T extends ApplicationCacheSerializable> List<T> collectOrUpdate(
            String cacheId, CacheableEntityTask<T> task) {
        return null;
    }
}
