package com.web.cloudtube.core.cache;

import com.web.cloudtube.core.cache.entity.ApplicationCacheableEntity;
import com.web.cloudtube.core.apps.layout.entity.PageContent;
import com.web.cloudtube.core.apps.layout.entity.PageLayoutWidget;
import com.web.cloudtube.core.apps.layout.entity.PageMetadata;

import java.util.HashMap;
import java.util.Map;

public class ApplicationCacheProperties {

    private static final Map<Class<? extends ApplicationCacheableEntity>, String>
            cacheableEntityKeys = initializeKeys();

    private static Map<Class<? extends ApplicationCacheableEntity>, String> initializeKeys() {
        Map<Class<? extends ApplicationCacheableEntity>, String> keys = new HashMap<>();
        keys.put(PageMetadata.class, "metadataCache");
        keys.put(PageLayoutWidget.class, "layoutWidgetCache");
        keys.put(PageContent.class, "contentCache");
        return keys;
    }

    public static String getEntityCacheIdPrefix(Class<? extends ApplicationCacheableEntity> clazz) {
        return cacheableEntityKeys.get(clazz);
    }
}
