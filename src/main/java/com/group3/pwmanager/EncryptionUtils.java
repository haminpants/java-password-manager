package com.group3.pwmanager;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {
    private static final int AES_KEY_SIZE = 256;
    private static final int IV_SIZE = 12; // 96 bits, recommended for GCM
    private static final int TAG_BIT_LENGTH = 128;

    public static SecretKey generateKeyFromString (String keyString) throws NoSuchAlgorithmException {
        // Generate a fixed-length (256-bit) hash based on the key string
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] keyStringHash = messageDigest.digest(keyString.getBytes());

        // Use the key string to create the secret key
        return new SecretKeySpec(keyStringHash, "AES");
    }

    // Generate a random AES 256-bit key
    public static SecretKey generateKey () throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE);
        return keyGen.generateKey();
    }

    // Generate a random 12-byte IV
    public static byte[] generateIV () {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static String encrypt (String plainText, SecretKey key) throws Exception {
        byte[] iv = generateIV();
        GCMParameterSpec spec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        byte[] encryptedData = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, encryptedData, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt (String encryptedText, SecretKey key) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(encryptedText);

        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(encryptedData, 0, iv, 0, iv.length);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] cipherText = new byte[encryptedData.length - iv.length];
        System.arraycopy(encryptedData, iv.length, cipherText, 0, cipherText.length);

        return new String(cipher.doFinal(cipherText));
    }

    // Helper: Convert SecretKey to base64 string
    public static String keyToBase64 (SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Helper: Rebuild SecretKey from base64
    public static SecretKey base64ToKey (String keyStr) {
        byte[] decoded = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(decoded, 0, decoded.length, "AES");
    }
}