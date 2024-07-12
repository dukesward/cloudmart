package com.web.cloudtube.core.apps.layout.entity;

import com.web.cloudtube.core.cache.entity.ApplicationCacheableEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "page_metadata")
public class PageMetadata extends ApplicationCacheableEntity {
    @EmbeddedId
    private PageId id;
    private String theme;
    private String layout;
    private String scope;
    @Transient
    private final List<PageLayoutWidget> widgets = new ArrayList<>();

    public PageMetadata() {};

    public PageMetadata(String appId, String pageId) {
        this.id = new PageId(pageId, appId);
    }

    public void setId(PageId id) {
        this.id = id;
    }

    public PageId getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<PageLayoutWidget> getWidgets() { return this.widgets; }

    public boolean addWidget(PageLayoutWidget widget) {
        boolean successful = false;
        if(this.id.pageId.equals(widget.getParent())) {
            successful = this.widgets.add(widget);
        }else {
            for(PageLayoutWidget w : this.widgets) {
                if(successful = w.addChild(widget)) break;
            }
        }
        return successful;
    }

    public void addWidgets(List<PageLayoutWidget> widgets) {
        this.addWidgets(widgets, 0);
    }

    public void addWidgets(List<PageLayoutWidget> widgets, int count) {
        if(count < widgets.size()) {
            List<PageLayoutWidget> temp = new ArrayList<>();
            boolean successful = false;
            for(PageLayoutWidget widget : widgets) {
                successful = this.addWidget(widget);
                if(!successful) temp.add(widget);
            }
            if(!temp.isEmpty()) this.addWidgets(temp, count++);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> layout = new HashMap<>();
        layout.put("page_name", this.id.pageId);
        layout.put("theme", this.theme);
        layout.put("layout", this.layout);
        Map<String, Object> layouts = new HashMap<>();
        List<Map<String, Object>> components = new ArrayList<>();
        for(PageLayoutWidget widget : this.widgets) {
            components.add(widget.toMap());
        }
        layouts.put("components", components);
        layout.put("layouts", layouts);
        return layout;
    }

    @Override
    public String getCacheId() {
        return this.id.appId + "." + this.id.pageId;
    }

    public void fromCacheProperties(Map<String, String> properties) {
        this.id.setPageId(properties.get("page_name"));
        this.layout = properties.get("layout");
        this.theme = properties.get("theme");
    }

    @Override
    public Map<String, String> toCacheProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("layout", this.layout);
        properties.put("page_name", this.id.pageId);
        properties.put("theme", this.theme);
        return properties;
    }

    @Embeddable
    public static class PageId implements Serializable {
        private String pageId;
        private String appId;

        public PageId() {}
        public PageId(String pageId, String appId) {
            this.pageId = pageId;
            this.appId = appId;
        }

        public String getPageId() {
            return pageId;
        }

        public void setPageId(String pageId) {
            this.pageId = pageId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }
    }

}
