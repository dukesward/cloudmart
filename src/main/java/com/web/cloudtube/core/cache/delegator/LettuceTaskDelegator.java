package com.web.cloudtube.core.cache.delegator;

import com.web.cloudtube.core.cache.ApplicationCacheSerializable;
import com.web.cloudtube.core.cache.CacheableEntityTask;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LettuceTaskDelegator implements CacheTaskDelegator {

    static final Logger logger = LoggerFactory.getLogger(LettuceTaskDelegator.class);

    private final StatefulRedisConnection<String, String> connection;
    private CommandStrategy strategy;
    private final Lock lock = new ReentrantLock();

    public LettuceTaskDelegator(StatefulRedisConnection<String, String> connection) {
        this.strategy = CommandStrategy.SYNC;
        this.connection = connection;
    }

    public void applyStrategy(CommandStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void delegateSimpleCacheTask(String cacheId, String cacheValue) {
        RedisCommands<String, String> commands = this.connection.sync();
        this.delegateSimpleCacheTask(cacheId, cacheValue, commands);
    }

    public void delegateSimpleCacheTask(String cacheId, String cacheValue, RedisCommands<String, String> commands) {
        commands.set(cacheId, cacheValue);
    }

    @Override
    public void pushSimpleCacheProperty(String cacheId, Map<String, String> properties) {
        RedisCommands<String, String> commands = this.connection.sync();
        String value = commands.get(cacheId);
        properties.put(cacheId, value);
    }

    @Override
    public void delegateMapCacheTask(String cacheIdPrefix, ApplicationCacheSerializable serializable) {
        String cacheId = cacheIdPrefix + ":" + serializable.getCacheId();
        Map<String, String> properties = serializable.toCacheProperties();
        RedisCommands<String, String> commands = this.connection.sync();
        this.delegateMapCacheTask(cacheId, serializable.toCacheProperties(), commands);
        commands.expire(cacheId, serializable.getCacheExpiry());
    }

    public void delegateMapCacheTask(
            String cacheId,
            Map<String, String> properties,
            RedisCommands<String, String> commands) {
        for(Map.Entry<String, String> entry : properties.entrySet()) {
            commands.hset(cacheId, entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void pushMapCacheProperties(String cacheIdPrefix, ApplicationCacheSerializable serializable) {
        if(serializable.getCacheId() != null) {
            String cacheId = cacheIdPrefix + ":" + serializable.getCacheId();
            RedisCommands<String, String> commands = this.connection.sync();
            this.pushMapCacheProperties(cacheId, serializable, commands);
        }
    }

    public void pushMapCacheProperties(
            String cacheId,
            ApplicationCacheSerializable serializable,
            RedisCommands<String, String> commands) {
        Map<String, String> properties = commands.hgetall(cacheId);
        if(properties != null) {
            serializable.fromCacheProperties(properties);
        }
    }

    @Override
    public <T extends ApplicationCacheSerializable> T pushOrUpdate(
            String cacheId, CacheableEntityTask<T> task) {
        RedisCommands<String, String> commands = this.connection.sync();
        T cacheableEntity = null;
        if(commands.exists(cacheId) > 0) {
            cacheableEntity = task.buildFromCacheId(cacheId);
            this.pushMapCacheProperties(cacheId, cacheableEntity, commands);
        }else {
            cacheableEntity = task.buildFromRepository(cacheId);
            this.delegateMapCacheTask(cacheId, cacheableEntity.toCacheProperties(), commands);
            commands.expire(cacheId, cacheableEntity.getCacheExpiry());
        }
        return cacheableEntity;
    }

    @Override
    public <T extends ApplicationCacheSerializable> List<T> collectOrUpdate(
            String cacheId, CacheableEntityTask<T> task) {
        RedisCommands<String, String> commands = this.connection.sync();
        String cacheSizeId = cacheId + ":size";
        List<T> cacheableEntities = new ArrayList<>();
        boolean cacheAvailable = true;
        lock.lock();
        try {
            if(commands.exists(cacheSizeId) > 0) {
                String cacheSize = commands.get(cacheSizeId);
                int size = Integer.parseInt(cacheSize);
                for(int i=0; i<size; i++) {
                    String cachedEntityId = cacheId + ":" + i;
                    if(commands.exists(cachedEntityId) > 0) {
                        T cacheableEntity = task.buildFromCacheId(cacheId);
                        this.pushMapCacheProperties(cachedEntityId, cacheableEntity, commands);
                        cacheableEntities.add(cacheableEntity);
                    }
                }
                if(cacheableEntities.size() < size) cacheAvailable = false;
            }else {
                cacheAvailable = false;
            }
            if(!cacheAvailable) {
                cacheableEntities = task.collectFromRepository(cacheId);
                if(!cacheableEntities.isEmpty()) {
                    int expiry = cacheableEntities.get(0).getCacheExpiry();
                    // store cached entity list size for reference
                    commands.set(cacheSizeId, Integer.toString(cacheableEntities.size()));
                    // set expiry of cached entity size to be same as cached entities
                    commands.expire(cacheSizeId, expiry);
                    for(int i=0; i<cacheableEntities.size(); i++) {
                        T cacheableEntity = cacheableEntities.get(i);
                        String cachedEntityId = cacheId + ":" + i;
                        this.delegateMapCacheTask(cachedEntityId, cacheableEntity.toCacheProperties(), commands);
                        commands.expire(cachedEntityId, cacheableEntity.getCacheExpiry());
                    }
                }
            }
        }catch (Exception e) {
            logger.error("Unable to process collectOrUpdate on cacheId:" + cacheId, e);
            // assume that error occurs for redis connection only, and try getting from database
            if(cacheableEntities == null) {
                cacheableEntities = task.collectFromRepository(cacheId);
            }
        }finally {
            lock.unlock();
        }
        return cacheableEntities;
    }

    public enum CommandStrategy {
        SYNC,
        ASYNC,
        REACTIVE
    }
}
