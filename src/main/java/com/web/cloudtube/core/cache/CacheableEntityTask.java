package com.web.cloudtube.core.cache;

import java.util.List;

public interface CacheableEntityTask<T extends ApplicationCacheSerializable> {
    public T buildFromCacheId(String cacheId);
    public T buildFromRepository(String cacheId);
    public List<T> collectFromRepository(String cacheId);
}
