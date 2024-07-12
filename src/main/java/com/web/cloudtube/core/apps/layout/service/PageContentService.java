package com.web.cloudtube.core.apps.layout.service;

import com.web.cloudtube.core.cache.ApplicationCacheProperties;
import com.web.cloudtube.core.cache.CacheableEntityTask;
import com.web.cloudtube.core.apps.layout.entity.PageContent;
import com.web.cloudtube.core.apps.layout.repository.PageContentRepository;
import com.web.cloudtube.core.cache.delegator.CacheTaskDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageContentService {
    static final Logger logger = LoggerFactory.getLogger(PageContentService.class);
    private final CacheTaskDelegator cacheTaskDelegator;
    private final PageContentRepository contentRepository;
    private static final String pageContentCacheIdPrefix
            = ApplicationCacheProperties.getEntityCacheIdPrefix(PageContent.class);

    public PageContentService(
            CacheTaskDelegator cacheTaskDelegator,
            PageContentRepository contentRepository) {
        this.cacheTaskDelegator = cacheTaskDelegator;
        this.contentRepository = contentRepository;
    }

    public Map<String, Object> getContentsByPage(String appId, String pageName) {
        Map<String, Object> contents = new HashMap<>();
        String cacheId = pageContentCacheIdPrefix + ":" + pageName;
        List<PageContent> entities = this.cacheTaskDelegator
                .collectOrUpdate(cacheId, new CacheableEntityTask<PageContent>() {
                    @Override
                    public PageContent buildFromCacheId(String cacheId) {
                        return new PageContent();
                    }
                    @Override
                    public PageContent buildFromRepository(String cacheId) {
                        return null;
                    }
                    @Override
                    public List<PageContent> collectFromRepository(String cacheId) {
                        return PageContentService.this.contentRepository.findByMetadata(appId, pageName);
                    }
                });
        contents.put("page_name", pageName);
        Map<String, PageContentObject> contentMap = this.assembleContents(entities);
        List<Map<String, Object>> contentList = new ArrayList<>();
        for(Map.Entry<String, PageContentObject> entry : contentMap.entrySet()) {
            contentList.add(entry.getValue().toMap());
        }
        contents.put("contents", contentList);
        return contents;
    }

    public Map<String, PageContentObject> assembleContents(List<PageContent> contents) {
        Map<String, PageContentObject> contentMap = new HashMap<>();
        for(PageContent content : contents) {
            String parent = content.getParent();
            String widgetId = content.getWidgetId();
            if(contentMap.get(widgetId) != null
                    && !contentMap.get(widgetId).hasContent(content.getContentId())) {
                contentMap.get(widgetId).addContent(content);
            }
            //else if(contentMap.get(parent) != null) {
                // contentMap.get(parent).addContent(content);
            //}
            else {
                PageContentObject object = new PageContentObject(content);
                contentMap.put(widgetId, object);
            }
        }
        return contentMap;
    }

    public static class PageContentObject {
        private String widgetId;
        private String parent;
        private final List<ContentProperty> contents = new ArrayList<>();

        public PageContentObject(String widgetId, String parent) {
            this.widgetId = widgetId;
            this.parent = parent;
        }

        public PageContentObject(PageContent content) {
            this.widgetId = content.getWidgetId();
            this.parent = content.getParent();
            this.contents.add(new ContentProperty(
                    content.getContentId(), content.getType(), content.getValue()));
        }

        public String getWidgetId() {
            return widgetId;
        }

        public void setWidgetId(String widgetId) {
            this.widgetId = widgetId;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public List<ContentProperty> getContents() {
            return contents;
        }

        public boolean hasContent(String contentId) {
            for(ContentProperty content : this.contents) {
                if(content.contentId.equals(contentId)) {
                    return true;
                }
            }
            return false;
        }

        public void addContent(ContentProperty content) {
            this.contents.add(content);
        }

        public void addContent(PageContent content) {
            if(widgetId.equals(content.getParent())) {
                this.addContent(new ContentProperty(
                        content.getContentId(), content.getType(), content.getValue()));
            }else if(widgetId.equals(content.getWidgetId())) {
                this.addContent(new ContentProperty(
                        content.getContentId(), content.getType(), content.getValue()));
            }else {
                logger.error("Not able to recognize content widget id " + content.getWidgetId());
            }
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("widget_id", this.widgetId);
            map.put("parent_id", this.parent);
            List<Map<String, String>> contents = new ArrayList<>();
            for(ContentProperty property : this.getContents()) {
                contents.add(property.toMap());
            }
            map.put("contents", contents);
            return map;
        }
    }

    public static class ContentProperty {
        private String contentId;
        private String contentType;
        private String contentValue;

        public ContentProperty(
                String contentId, String contentType, String contentValue) {
            this.contentId = contentId;
            this.contentType = contentType;
            this.contentValue = contentValue;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentValue() {
            return contentValue;
        }

        public void setContentValue(String contentValue) {
            this.contentValue = contentValue;
        }

        public Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            map.put("content_id", this.contentId);
            map.put("type", this.contentType);
            map.put("value", this.contentValue);
            return map;
        }
    }
}
