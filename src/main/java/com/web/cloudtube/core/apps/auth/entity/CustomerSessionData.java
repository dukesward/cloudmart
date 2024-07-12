package com.web.cloudtube.core.apps.auth.entity;

import com.web.cloudtube.core.util.DateAndTimeUtil;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "user_session_data")
public class CustomerSessionData {

    @Id
    private String id;
    @Column(nullable = false)
    private String sessionId;
    @Column(nullable = false)
    private String authId;
    @Column(nullable = false)
    private String userType;
    @Column(nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime inserted = LocalDateTime.now();
    private LocalDateTime expires;
    private String userId;

    public CustomerSessionData() {}

    public CustomerSessionData(
            String sessionId,
            String authId,
            String userType
    ) {
        this.id = UUID.randomUUID().toString();
        if(authId == null) {
            authId = this.id;
            // expiry days preferred to be loaded from properties
            // this.expires = DateAndTimeUtil.getDateWithExpiry(7);
        }
        this.authId = authId;
        this.sessionId = sessionId;
        this.userType = userType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAuthIdId() {
        return authId;
    }

    public void setAuthIdId(String authId) {
        this.authId = authId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public LocalDateTime getInserted() {
        return inserted;
    }

    public void setInserted(LocalDateTime inserted) {
        this.inserted = inserted;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
