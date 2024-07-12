package com.web.cloudtube.core.apps.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserGroupTest {

    @Test
    void testUserTypeIncludes() {
        UserGroup all = new UserGroup("all");
        boolean includes = all.includesType("white");
        System.out.println(includes);
        // Assertions.assertTrue(all.includesType("white"));
    }
}
