package com.web.cloudtube.core.apps.auth.service;

import com.web.cloudtube.core.apps.BaseProfile;
import com.web.cloudtube.core.apps.UserBaseProfile;
import com.web.cloudtube.core.apps.auth.UserGroup;
import com.web.cloudtube.core.apps.auth.entity.CustomerLoginData;
import com.web.cloudtube.core.apps.auth.entity.CustomerSessionData;
import com.web.cloudtube.core.apps.auth.properties.ApplicationAuthProperties;
import com.web.cloudtube.core.apps.auth.repository.CustomerSessionRespository;
import com.web.cloudtube.core.error.throwable.CustomerInvalidSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Base64;

@Service
public class CustomerProfileService {
    private static final String AUTH_SESSION_COOKIE_NAME = "CLOUD_SESSION_ID";
    private final CustomerSessionRespository sessionRepository;
    private final ApplicationAuthProperties authProperties;
    private final CustomerLoginService customerLoginService;

    static final Logger logger = LoggerFactory.getLogger(CustomerProfileService.class);

    @Autowired
    public CustomerProfileService(
            CustomerSessionRespository sessionRepository,
            CustomerLoginService customerLoginService,
            ApplicationAuthProperties properties
    ) {
        this.sessionRepository = sessionRepository;
        this.authProperties = properties;
        this.customerLoginService = customerLoginService;
    }

    public void updateUserLogin(BaseProfile baseProfile) {
        logger.debug("Enter CustomerLoginService:updateUserLogin");
        CustomerSessionData sessionData = (CustomerSessionData) baseProfile.getProperty("customerProfile");
        CustomerLoginData loginData = (CustomerLoginData) baseProfile.getProperty("userLoginData");
        if(loginData != null) {
            sessionData.setUserId(loginData.getUserId());
            if(!loginData.getUserType().equals(sessionData.getUserType()))
                sessionData.setUserType(loginData.getUserType());
        }
        try {
            this.sessionRepository.updateCustomerSessionDataByUserId(
                    sessionData.getUserId(), sessionData.getUserType(), sessionData.getId());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Session update failed");
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CustomerSessionData loadSessionData(
            HttpServletRequest request, HttpServletResponse response, UserBaseProfile baseProfile)
            throws CustomerInvalidSessionException {
        Cookie authId = WebUtils.getCookie(request, AUTH_SESSION_COOKIE_NAME);
        HttpSession session = request.getSession();
        if(session != null) {
            String sessionId = session.getId();
            String encodedKey = null;
            // we don't care what is exact userType but only know it's the default
            UserGroup customerGroup;
            CustomerSessionData sessionData = null;
            // ApplicationBasicPolicy policy = (ApplicationBasicPolicy) baseProfile.getProperty("policy");
            if (authId != null) {
                encodedKey = authId.getValue();
            }else {
                try {
                    String remoteAddr = (String) baseProfile.getProperty("httpRemoteAddress");
                    logger.debug("extracted client ip from remoteAddress: " + remoteAddr);
                    if (remoteAddr != null) {
                        // auth id = hash(ip) + ip -> base64 encoded
                        String hash = remoteAddr.hashCode() + remoteAddr;
                        encodedKey = Base64.getEncoder().encodeToString(hash.getBytes());
                    }
                } catch (Exception e) {
                    throw new CustomerInvalidSessionException();
                }
            }
            if (encodedKey != null) {
                baseProfile.setProperty("encodedAuthId", encodedKey);
                sessionData = this.sessionRepository.getByAuthId(encodedKey);
                if (sessionData == null) {
                    customerGroup = new UserGroup(UserGroup.UserType.VISITOR);
                    logger.debug("Auth ID missing or not valid, proceed with visitor data initialize");
                    sessionData = new CustomerSessionData(sessionId, encodedKey, customerGroup.getUserTypeValue());
                    authId = new Cookie(AUTH_SESSION_COOKIE_NAME, sessionData.getAuthIdId());
                    // authId.setMaxAge(defaultSessionExpiry * 24 * 3600);
                    authId.setPath("/");
                    response.addCookie(authId);
                    this.sessionRepository.save(sessionData);
                }else {
                    // if profile is bound to a ghost user id, set to null
                    if(sessionData.getUserId() != null && !this.customerLoginService.updateUserLogin(baseProfile, sessionData))
                        sessionData.setUserId(null);
                    // if session has been renewed or expired,
                    // bind current session id to session data
                    if(!sessionId.equals(sessionData.getSessionId())) {
                        sessionData.setSessionId(sessionId);
                        this.sessionRepository.updateCustomerSessionDataByAuthId(encodedKey, sessionId);
                    }
                }
                baseProfile.setProperty("customerProfile", sessionData);
            }else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid IP address");
            }
            return sessionData;
        }else {
            logger.error("Session cannot be null");
        }
        return null;
    }
}
