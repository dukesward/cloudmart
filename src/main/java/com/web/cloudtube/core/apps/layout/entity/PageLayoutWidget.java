package com.web.cloudtube.core.apps.layout.entity;

import com.web.cloudtube.core.cache.entity.ApplicationCacheableEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "page_layout_widget")
public class PageLayoutWidget extends ApplicationCacheableEntity {
    @Id
    private String widgetId;
    private String pageId;
    private String position;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String appId;
    private String subtype;
    private String parent;
    @Column(nullable = false)
    private String userType;
    @Transient
    private final List<PageLayoutWidget> children = new ArrayList<>();

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidget_id(String widgetId) {
        this.widgetId = widgetId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<PageLayoutWidget> getContents() {
        return children;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean addChild(PageLayoutWidget widget) {
        if(widget.parent.equals(this.widgetId)) {
            this.children.add(widget);
            return true;
        }
        for(PageLayoutWidget c : this.children) {
            if(c.addChild(widget)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasChild(PageLayoutWidget widget) {
        if(widget.parent.equals(this.widgetId)) {
            return true;
        }
        for(PageLayoutWidget c : this.children) {
            if(c.hasChild(widget)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getCacheId() {
        return this.appId + "." + this.widgetId;
    }

    @Override
    public Map<String, String> toCacheProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("page_id", this.pageId);
        properties.put("parent", this.parent);
        properties.put("type", this.type);
        properties.put("position", this.position);
        properties.put("subtype", this.subtype);
        properties.put("user_type", this.userType);
        return properties;
    }

    @Override
    public void fromCacheProperties(Map<String, String> properties) {
        this.widgetId = properties.get("widget_id");
        this.pageId = properties.get("page_id");
        this.parent = properties.get("parent");
        this.type = properties.get("type");
        this.position = properties.get("position");
        this.subtype = properties.get("subtype");
        this.userType = properties.get("user_type");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> mapped = new HashMap<>();
        mapped.put("type", this.type);
        mapped.put("widget_id", this.getWidgetId());
        mapped.put("position", this.position);
        mapped.put("subtype", this.subtype);
        if(!this.children.isEmpty()) {
            List<Map<String, Object>> widgets = new ArrayList<>();
            for(PageLayoutWidget c : this.children) {
                widgets.add(c.toMap());
            }
            mapped.put("children", widgets);
        }
        mapped.put("user_type", this.userType);
        return mapped;
    }
}
