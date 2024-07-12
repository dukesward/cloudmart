package com.web.cloudtube.core.apps;

import java.util.HashMap;
import java.util.Map;

public class BaseProfile {
    private static final ThreadLocal<Map<String, BaseProfile>> profiles = ThreadLocal.withInitial(HashMap::new);
    protected volatile Map<String, Object> properties;
    protected BaseProfile() {
        this.properties = new HashMap<>();
    }

    public static BaseProfile getUserProfile(String id) {
        if(!profiles.get().containsKey(id) || profiles.get().get(id) == null) {
            // this better to be from environment props or database
            BaseProfile userProfile = new UserBaseProfile();
            profiles.get().put(id, userProfile);
        }
        return profiles.get().get(id);
    }

    public static void cleanup() { profiles.remove(); }

    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }
}
