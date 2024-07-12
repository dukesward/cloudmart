package com.web.cloudtube.core.cache;

import java.util.Map;

public interface ApplicationCacheSerializable {
    String getCacheId();
    Map<String, String> toCacheProperties();
    void fromCacheProperties(Map<String, String> properties);
    int getCacheExpiry();
}
