package com.web.cloudtube.core.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public abstract class ApplicationPolicyService {
    static final Logger logger = LoggerFactory.getLogger(ApplicationPolicyService.class);
    public boolean action(String actionId) throws NoSuchMethodException {
        try {
            Method method = this.getClass().getMethod(actionId);
            return (boolean) method.invoke(this);
        }catch (IllegalArgumentException iae) {
            logger.debug("Argument for action " + actionId + " is incorrect");
            return false;
        }catch (Exception e) {
            logger.debug("Error is thrown during action " + actionId);
            return false;
        }
    }
}
