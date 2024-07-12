package com.web.cloudtube.core.apps;

public class UserBaseProfile extends BaseProfile {

    private String userType;

    public UserBaseProfile() {
        super();
    }

    public UserBaseProfile(String userType) {
        super();
        this.userType = userType;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
