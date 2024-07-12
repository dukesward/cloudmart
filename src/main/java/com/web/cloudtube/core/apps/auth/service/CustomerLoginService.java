package com.web.cloudtube.core.apps.auth.service;

import com.google.common.hash.Hashing;
import com.web.cloudtube.core.apps.BaseProfile;
import com.web.cloudtube.core.apps.auth.UserGroup;
import com.web.cloudtube.core.apps.auth.entity.CustomerLoginData;
import com.web.cloudtube.core.apps.auth.entity.CustomerSessionData;
import com.web.cloudtube.core.apps.auth.entity.UserAuthProfile;
import com.web.cloudtube.core.apps.auth.properties.ApplicationAuthProperties;
import com.web.cloudtube.core.apps.auth.repository.CustomerLoginRepository;
import com.web.cloudtube.core.apps.auth.repository.UserAuthDataRepository;
import com.web.cloudtube.core.security.service.ApplicationPolicyService;
import com.web.cloudtube.core.util.PasswordBasicCipherUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class CustomerLoginService extends ApplicationPolicyService implements UserDetailsService {
    static final Logger logger = LoggerFactory.getLogger(CustomerLoginService.class);
    static final String DEFAULT_LOGIN_TYPE = "email";
    private final CustomerLoginRepository customerLoginRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final ApplicationAuthProperties authProperties;
    private final String encryptKeyAlgorithm = "DES";

    @Autowired
    public CustomerLoginService(
            CustomerLoginRepository customerLoginRepository,
            UserAuthDataRepository userAuthDataRepository,
            ApplicationAuthProperties authProperties
    ) {
        this.customerLoginRepository = customerLoginRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.authProperties = authProperties;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public boolean processUserLogin() {
        logger.debug("Enter CustomerLoginService:processLogin");
        return false;
    }

    public CustomerLoginData buildLoginProfile(
            String accessId, String username, String sessionId, String loginType) throws Exception {
        CustomerLoginData loginData = new CustomerLoginData(accessId, loginType);
        SecretKey secretKey = PasswordBasicCipherUtility.generateSecretKey(encryptKeyAlgorithm);
        loginData.setUserId(username);
        loginData.setUserType(UserGroup.UserType.CL_WHITE);
        loginData.setSessionId(sessionId);
        String sessionExpiry = this.authProperties.getAuthSessionExpiry();
        logger.debug("default expiry days of auth session is " + sessionExpiry);
        int defaultSessionExpiry = 0;
        if(sessionExpiry != null) {
            try {
                defaultSessionExpiry = Integer.parseInt(sessionExpiry);
            }catch (NumberFormatException nfe) {
                logger.error("property <auth_session_expiry> is not invalid: " + sessionExpiry);
                defaultSessionExpiry = 7;
            }
        }
        loginData.setExpireTime(LocalDateTime.now().plusDays(defaultSessionExpiry));
        // convert the secret key to a string for storage
        loginData.setLoginId(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        return loginData;
    }

    public boolean updateUserLogin(BaseProfile profile, CustomerSessionData customerProfile) {
        CustomerLoginData loginData = this.customerLoginRepository
                .findFirstByUserIdOrderByIdDesc(customerProfile.getUserId());
        if(loginData == null || loginData.getLogoutTime() != null || loginData.hasExpired())
            return false;
        customerProfile.setUserType(loginData.getUserType());
        return true;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean updateUserLogin(BaseProfile profile, String username, String password)
            throws ResponseStatusException {
        try {
            String accessId = (String) profile.getProperty("encodedAuthId");
            CustomerSessionData customerProfile = (CustomerSessionData) profile.getProperty("customerProfile");
            if(customerProfile == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid user profile");
            }
            CustomerLoginData latestLogin = this.customerLoginRepository.findFirstByUserIdOrderByIdDesc(username);
            if(latestLogin == null) {
                // new customer, proceed with user registration
                CustomerLoginData loginData = this.buildLoginProfile(accessId, username, customerProfile.getId(), DEFAULT_LOGIN_TYPE);
                String secret = loginData.getLoginId();
                if (secret == null || secret.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
                }
                String hashedPass = Hashing.sha256()
                        .hashString(password, StandardCharsets.UTF_8)
                        .toString();
                UserAuthProfile authData = new UserAuthProfile();
                authData.setLoginType(DEFAULT_LOGIN_TYPE);
                authData.setUsername(username);
                authData.setPassword(PasswordBasicCipherUtility.encryptPasswordWithDES(hashedPass, secret));
                this.customerLoginRepository.save(loginData);
                this.userAuthDataRepository.save(authData);
                profile.setProperty("userLoginData", loginData);
                return true;
            }else {
                // customer has logged in before, check if same ip address
                if(latestLogin.getAccessId().equals(accessId)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Username already exists");
                }
                return false;
            }
        }catch (ResponseStatusException re) {
            logger.error("Error processing user update/registration");
            throw re;
        }catch (Exception e) {
            logger.error("Error processing user update/registration");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Auth service unavailable");
        }
    }
}
