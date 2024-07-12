package com.web.cloudtube.core.cache.entity;

import com.web.cloudtube.core.cache.ApplicationCacheSerializable;

public abstract class ApplicationCacheableEntity implements ApplicationCacheSerializable {

    protected int cacheExpiry = 30;

    public int getCacheExpiry() {
        return this.cacheExpiry;
    };

    public void setCacheExpiry(int cacheExpiry) {
        this.cacheExpiry = cacheExpiry;
    }
}
