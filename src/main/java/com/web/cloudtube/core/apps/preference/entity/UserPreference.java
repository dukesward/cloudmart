package com.web.cloudtube.core.apps.preference.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_preference")
public class UserPreference {

    @Id
    private String id;
    @Column(nullable = false)
    private String userType;
    @Column(nullable = false)
    private String userId;
    private String prefferedId;

    protected UserPreference() {};

    public UserPreference(String id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrefferedId() {
        return prefferedId;
    }

    public void setPrefferedId(String prefferedId) {
        this.prefferedId = prefferedId;
    }
}
