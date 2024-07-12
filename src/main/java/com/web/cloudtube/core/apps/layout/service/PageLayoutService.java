package com.web.cloudtube.core.apps.layout.service;

import com.web.cloudtube.core.apps.auth.UserGroup;
import com.web.cloudtube.core.apps.layout.entity.PageLayoutWidget;
import com.web.cloudtube.core.apps.layout.entity.PageMetadata;
import com.web.cloudtube.core.apps.layout.repository.PageLayoutWidgetRepository;
import com.web.cloudtube.core.apps.layout.repository.PageMetadataRepository;
import com.web.cloudtube.core.cache.ApplicationCacheProperties;
import com.web.cloudtube.core.cache.CacheableEntityTask;
import com.web.cloudtube.core.cache.delegator.CacheTaskDelegator;
import com.web.cloudtube.core.cache.entity.ApplicationCacheableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PageLayoutService {
    static final Logger logger = LoggerFactory.getLogger(PageLayoutService.class);
    private final CacheTaskDelegator cacheTaskDelegator;
    private final PageMetadataRepository metadataRepository;
    private final PageLayoutWidgetRepository layoutWidgetRepository;
    private static final String pageMetadataCacheIdPrefix
            = ApplicationCacheProperties.getEntityCacheIdPrefix(PageMetadata.class);
    private static final String pageLayoutWidgetCacheIdPrefix
            = ApplicationCacheProperties.getEntityCacheIdPrefix(PageLayoutWidget.class);

    @Autowired
    public PageLayoutService(
            CacheTaskDelegator cacheTaskDelegator,
            PageMetadataRepository metadataRepository,
            PageLayoutWidgetRepository layoutWidgetRepository
    ) {
        this.cacheTaskDelegator = cacheTaskDelegator;
        this.metadataRepository = metadataRepository;
        this.layoutWidgetRepository = layoutWidgetRepository;
    }

    @PostConstruct
    public void prepareLayoutObjects() {
        List<PageMetadata> metadataObjects = this.metadataRepository.findAll();
        for(PageMetadata metadata : metadataObjects) {
            this.getAssembledLayout(metadata.getId().getAppId(), metadata.getId().getPageId(), "all");
        }
    }

    public PageMetadata getPageMetadataByPage(String appId, String pageId) {
        String cacheId = pageMetadataCacheIdPrefix + ":" + appId + "." + pageId;
        return this.cacheTaskDelegator.pushOrUpdate(cacheId, new CacheableEntityTask<>() {
            @Override
            public PageMetadata buildFromCacheId(String cacheId) {
                return new PageMetadata(appId, pageId);
            }
            @Override
            public List<PageMetadata> collectFromRepository(String cacheId) {
                return PageLayoutService.this.metadataRepository.findByIdPageIdAndIdAppId(appId, pageId);
            }
            @Override
            public PageMetadata buildFromRepository(String cacheId) {
                List<PageMetadata> items = PageLayoutService.this.metadataRepository.findByIdPageIdAndIdAppId(appId, pageId);
                return !items.isEmpty() ? items.get(0) : null;
            }
        });
    }

    public Map<String, Object> getAssembledLayout(String appId, String pageId, String userType) {
        PageMetadata metadata = new PageMetadata(appId, pageId);
        String cacheId = pageMetadataCacheIdPrefix + ":" + appId + "." + pageId;
        List<PageMetadataCacheableEntity> entities = this.cacheTaskDelegator
                .collectOrUpdate(cacheId, new CacheableEntityTask<>() {
                    @Override
                    public PageMetadataCacheableEntity buildFromCacheId(String cacheId) {
                        return new PageMetadataCacheableEntity();
                    }
                    @Override
                    public PageMetadataCacheableEntity buildFromRepository(String cacheId) {
                        return null;
                    }
                    @Override
                    public List<PageMetadataCacheableEntity> collectFromRepository(String cacheId) {
                        List<PageMetadataCacheableEntity> entities = new ArrayList<>();
                        List<Map<String, String>> results = PageLayoutService.this.layoutWidgetRepository
                                .findWidgetsByPageId(appId, pageId, "any");
                        for(Map<String, String> result : results) {
                            PageMetadataCacheableEntity entity = new PageMetadataCacheableEntity();
                            entity.fromCacheProperties(result);
                            entities.add(entity);
                        }
                        return entities;
                    }
                });
        if(!entities.isEmpty()) {
            metadata.setLayout(entities.get(0).properties.get("layout"));
            metadata.setTheme(entities.get(0).properties.get("theme"));
            List<PageLayoutWidget> components = entities.stream()
                    .filter(e -> new UserGroup(e.properties.get("user_type")).includesType(userType))
                    .map(e -> {
                        PageLayoutWidget w = new PageLayoutWidget();
                        w.setAppId(appId);
                        w.setPageId(pageId);
                        w.fromCacheProperties(e.toCacheProperties());
                        return w;
                    })
                    .collect(Collectors.toList());
            metadata.addWidgets(components);
        }
        return metadata.toMap();
    }

    public static class PageMetadataCacheableEntity extends ApplicationCacheableEntity {
        private final Map<String, String> properties = new HashMap<>();
        @Override
        public String getCacheId() {
            return this.properties.get("widget_id");
        }

        @Override
        public Map<String, String> toCacheProperties() {
            return this.properties;
        }

        @Override
        public void fromCacheProperties(Map<String, String> properties) {
            this.properties.putAll(properties);
        }
    }
}
