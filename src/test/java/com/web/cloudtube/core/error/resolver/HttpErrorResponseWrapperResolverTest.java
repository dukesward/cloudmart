package com.web.cloudtube.core.error.resolver;

import com.web.cloudtube.core.apps.auth.CloudtubeCustomerProfileController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CloudtubeCustomerProfileController.class)
public class HttpErrorResponseWrapperResolverTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void resolvesCustomerProfile() throws Exception {

    }
}
