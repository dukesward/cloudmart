package com.web.cloudtube.core.apps.auth;

import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserGroup {
    private UserType userType;
    private int _userType;

    public UserGroup(UserType userType) { this.userType = userType; }

    public UserGroup(int userType) {
        this.userType = UserType.fromType(userType);
    }

    public UserGroup(String userType) {
        this.userType = UserType.fromType(userType);
    }

    public int getUserType() {
        return this._userType;
    }

    public String getUserTypeValue() {
        return this.userType.value();
    }

    public boolean includesType(UserType userType) {
        if(this.getUserTypeValue().equals(userType.userType))
            return true;
        for(UserType type : this.userType.children) {
            if(new UserGroup(type).includesType(userType))
                return true;
        }
        return false;
    }

    public boolean includesType(int _userType) {
        return _userType == this._userType;
    }

    public boolean includesType(String userType) {
        return this.includesType(UserType.fromType(userType));
    }

    public boolean includesType(List<UserType> types) {
        for(UserType type : types) {
            if(this.includesType(type))
                return true;
        }
        return false;
    }
    
    public enum UserType {
        VISITOR("visitor"),
        ADMIN("admin"),
        CL_WHITE("white"),
        CL_BLUE("blue"),
        CL_GOLD("gold"),
        CUSTOMER("customer", UserType.ADMIN, UserType.CL_WHITE, UserType.CL_BLUE, UserType.CL_GOLD),
        ALL("all", UserType.VISITOR, UserType.CUSTOMER);

        final String userType;
        final List<UserType> children;
        static final UserType[] types = { UserType.VISITOR };
        private UserType(final String userType, UserType ... children) {
            this.userType = userType;
            this.children = new ArrayList<>();
            this.children.addAll(Arrays.asList(children));
        }

        public List<UserType> getSubTypes() {
            return this.children;
        }

        static public UserType fromType(int userType) {
            return userType < types.length ? types[userType] : UserType.VISITOR;
        }

        static public UserType fromType(String userTypeName) {
            UserType userType;
            switch (userTypeName) {
                case "admin":
                    userType = UserType.ADMIN;
                    break;
                case "white":
                    userType = UserType.CL_WHITE;
                    break;
                case "blue":
                    userType = UserType.CL_BLUE;
                    break;
                case "gold":
                    userType = UserType.CL_GOLD;
                    break;
                case "visitor":
                    userType = UserType.VISITOR;
                    break;
                default:
                    userType = UserType.ALL;
            }
            return userType;
        }

        public String value() {
            return this.userType;
        }

        public List<String> subTypes() {
            return this.children.stream().map(UserType::value).collect(Collectors.toList());
        }
    }
}

