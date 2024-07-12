package com.web.cloudtube.core.apps.layout.entity;

import javax.persistence.*;

@Entity
@Table(name = "page_property")
public class PageLayoutProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String widgetType;
    private String property;
    private String appId;
    private String value;
    private String propertyType;

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
