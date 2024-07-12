package com.web.cloudtube.core.apps.auth.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_auth_data")
public class UserAuthProfile {
    @Id
    private Long userId;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String loginType;
    @Column(nullable = false)
    private String password;

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
