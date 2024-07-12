package com.web.cloudtube.core.util;

import com.google.common.hash.Hashing;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PasswordBasicCipherUtilityTest {
    static final Logger logger = LoggerFactory.getLogger(PasswordBasicCipherUtilityTest.class);

    @Test
    public void validateEncryptPasswordWithDES() {
        try {
            SecretKey secretKey = PasswordBasicCipherUtility.generateSecretKey("DES");
            String secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            SecretKeySpec secretSpec = new SecretKeySpec(Base64.getDecoder().decode(secret),"DES");
            String hashedPass = Hashing.sha256()
                    .hashString("i_am_a_token", StandardCharsets.UTF_8)
                    .toString();
            String encrypted = PasswordBasicCipherUtility.encryptPasswordWithDES(hashedPass, secretSpec);
            logger.debug(encrypted);
        }catch (Exception e) {
            logger.error("Test validateEncryptPasswordWithDES failed: " + e);
        }
    }
}
