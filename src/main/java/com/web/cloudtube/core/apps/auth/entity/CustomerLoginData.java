package com.web.cloudtube.core.apps.auth.entity;

import com.web.cloudtube.core.apps.auth.UserGroup;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user_login_data")
public class CustomerLoginData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String accessId;
    @Column(nullable = false)
    private String loginType;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String userType;
    @Column(nullable = false)
    private String sessionId;
    @Column(nullable = false)
    private String loginId;
    private String loginSource;
    @Column(nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime loginTime = LocalDateTime.now();
    private LocalDateTime logoutTime;
    private LocalDateTime expireTime;

    public CustomerLoginData(String accessId, String loginType) {
        this.accessId = accessId;
        this.loginType = loginType;
    }

    public CustomerLoginData() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserType(UserGroup.UserType userType) {
        this.userType = userType.value();
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(String loginSource) {
        this.loginSource = loginSource;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public boolean hasExpired() {
        if(this.expireTime == null)
            return true;
        return LocalDateTime.now().isAfter(this.expireTime);
    }
}
