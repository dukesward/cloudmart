package com.web.cloudtube.core.apps.auth.entity;

import javax.persistence.*;

@Entity
@Table(name = "app_secured_keys")
public class AppSecuredKeys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String desSecurityKey;
    @Column(nullable = false)
    private String passSecurityKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesSecurityKey() {
        return desSecurityKey;
    }

    public void setDesSecurityKey(String desSecurityKey) {
        this.desSecurityKey = desSecurityKey;
    }

    public String getPassSecurityKey() {
        return passSecurityKey;
    }

    public void setPassSecurityKey(String passSecurityKey) {
        this.passSecurityKey = passSecurityKey;
    }
}
