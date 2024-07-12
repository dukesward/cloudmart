package com.web.cloudtube.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordBasicCipherUtility {
    static final Logger logger = LoggerFactory.getLogger(PasswordBasicCipherUtility.class);

    public static SecretKey generateSecretKey(String algorithm) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        SecureRandom secRandom = new SecureRandom();
        keyGen.init(secRandom);
        return keyGen.generateKey();
    }

    public static String encryptPasswordWithDES(String original) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecureRandom secRandom = new SecureRandom();
        keyGen.init(secRandom);
        SecretKey secretKey = keyGen.generateKey();
        return encryptPasswordWithDES(original, secretKey);
    }

    public static String encryptPasswordWithDES(String original, String secretKey) throws Exception {
        SecretKeySpec secret = new SecretKeySpec(Base64.getDecoder().decode(secretKey),"DES");
        return encryptPasswordWithDES(original, secret);
    }

    public static String encryptPasswordWithDES(String original, SecretKey secretKey) throws Exception {
        // get cipher utility for DES algorithm
        Cipher cipher = Cipher.getInstance("DES");
        // create a random secret key based on original token to be stored in db
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encipherByte = cipher.doFinal(original.getBytes());
        String encode = Base64.getEncoder().encodeToString(encipherByte);
        logger.debug("Encrypted cipher is " + encode);
        return encode;
    }

    /*public String decryptWithPrivateKey(String encryptedData, String pubKeyFp, String asymmPadding) throws Exception {
        loadKeystores();
        String alias = fingerPrintAliasMap.get(pubKeyFp);

        KeyPair keyPair = getKeyPair(alias);
        Cipher cipher = Cipher.getInstance(asymmPadding);

        OAEPParameterSpec oParamSpec = new OAEPParameterSpec(SHA256, MGF1, MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate(), oParamSpec);

        byte[] decoded = Hex.decodeHex(encryptedData.toCharArray());
        byte[] decrypted = cipher.doFinal(decoded);
        System.out.println("decoded and decrypted key length: " + decrypted.length); // 24 on windows, random on mac

        return new String(Hex.encodeHex(decrypted));
    }*/
}
