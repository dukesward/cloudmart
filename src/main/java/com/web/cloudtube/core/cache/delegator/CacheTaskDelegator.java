package com.web.cloudtube.core.cache.delegator;

import com.web.cloudtube.core.cache.ApplicationCacheSerializable;
import com.web.cloudtube.core.cache.CacheableEntityTask;

import java.util.List;
import java.util.Map;

public interface CacheTaskDelegator {
    public void delegateSimpleCacheTask(String cacheId, String cacheValue);
    public void pushSimpleCacheProperty(String cacheId, Map<String, String> properties);
    public void delegateMapCacheTask(String cacheIdPrefix, ApplicationCacheSerializable serializable);
    public void pushMapCacheProperties(String cacheIdPrefix, ApplicationCacheSerializable serializable);
    public <T extends ApplicationCacheSerializable> T pushOrUpdate(String cacheId, CacheableEntityTask<T> task);
    public <T extends ApplicationCacheSerializable> List<T> collectOrUpdate(String cacheId, CacheableEntityTask<T> task);
}
