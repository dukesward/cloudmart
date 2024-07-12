package com.web.cloudtube.core.apps.portal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "target_message_consent")
public class TargetMessageConsent {
    @Id
    private String id;
    @Column(nullable = false)
    private String businessCode;
    @Column(nullable = false)
    private String messageType;
    @Column(nullable = false)
    private Long messageCode;
    @Column(nullable = false)
    private Long messageGroupCode;
    @Column(nullable = false)
    private int consentCode;
    private LocalDateTime consentExpiry;
    @Column(nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Long getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(Long messageCode) {
        this.messageCode = messageCode;
    }

    public Long getMessageGroupCode() {
        return messageGroupCode;
    }

    public void setMessageGroupCode(Long messageGroupCode) {
        this.messageGroupCode = messageGroupCode;
    }

    public int getConsentCode() {
        return consentCode;
    }

    public void setConsentCode(int consentCode) {
        this.consentCode = consentCode;
    }

    public LocalDateTime getConsentExpiry() {
        return consentExpiry;
    }

    public void setConsentExpiry(LocalDateTime consentExpiry) {
        this.consentExpiry = consentExpiry;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
