package aes.test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCMExample {
    // Constants
    private static final int AES_KEY_SIZE = 256;
    private static final int IV_SIZE = 12; // 96 bits, recommended for GCM
    private static final int TAG_BIT_LENGTH = 128;

    // Generate a random AES 256-bit key
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE);
        return keyGen.generateKey();
    }

    // Generate a random 12-byte IV
    public static byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Encrypt plaintext
    public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        return cipher.doFinal(plaintext);
    }

    // Decrypt ciphertext
    public static byte[] decrypt(byte[] ciphertext, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(ciphertext);
    }

    // Helper: Convert SecretKey to base64 string
    public static String keyToBase64(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Helper: Rebuild SecretKey from base64
    public static SecretKey base64ToKey(String keyStr) {
        byte[] decoded = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(decoded, 0, decoded.length, "AES");
    }

    // Demo
    public static void main(String[] args) throws Exception {
        String message = "whats up!";
        SecretKey key = generateKey();
        byte[] iv = generateIV();

        // Encrypt
        byte[] cipherText = encrypt(message.getBytes(), key, iv);

        // Decrypt
        byte[] decrypted = decrypt(cipherText, key, iv);

        // Output
        System.out.println("Original: " + message);
        System.out.println("Encrypted (Base64): " + Base64.getEncoder().encodeToString(cipherText));
        System.out.println("Decrypted: " + new String(decrypted));
        System.out.println("Key (Base64): " + keyToBase64(key));
        System.out.println("IV (Base64): " + Base64.getEncoder().encodeToString(iv));
    }
}
