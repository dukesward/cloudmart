package com.web.cloudtube.core.apps.layout.entity;

import com.web.cloudtube.core.cache.entity.ApplicationCacheableEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "page_content")
public class PageContent extends ApplicationCacheableEntity {
    @EmbeddedId
    private WidgetId id;
    @Column(nullable = false)
    private String type;
    private String value;
    private String parent;

    public PageContent() {
        this.id = new WidgetId();
    }

    public PageContent(String widgetId, String contentId) {
        this.id = new WidgetId(widgetId, contentId);
    }

    public WidgetId getId() {
        return this.id;
    }

    public void setId(WidgetId widgetId) {
        this.id = widgetId;
    }

    public String getWidgetId() { return this.id.widgetId; }

    public String getContentId() { return this.id.contentId; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String getCacheId() {
        return this.id.widgetId + "." + this.id.contentId;
    }

    @Override
    public Map<String, String> toCacheProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("widgetId", this.id.widgetId);
        properties.put("contentId", this.id.contentId);
        properties.put("parent", this.parent);
        properties.put("type", this.type);
        properties.put("value", this.value);
        return properties;
    }

    @Override
    public void fromCacheProperties(Map<String, String> properties) {
        this.id.widgetId = properties.get("widgetId");
        this.id.contentId = properties.get("contentId");
        this.parent = properties.get("parent");
        this.type = properties.get("type");
        this.value = properties.get("value");
    }

    public int getCacheExpiry() { return 60; }

    @Embeddable
    public static class WidgetId implements Serializable {
        private String widgetId;
        private String contentId;

        public WidgetId() {}

        public WidgetId(String widgetId, String contentId) {
            this.widgetId = widgetId;
            this.contentId = contentId;
        }

        public String getWidgetId() {
            return widgetId;
        }

        public void setWidgetId(String widgetId) {
            this.widgetId = widgetId;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }
    }


}
