/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Sam
 */
public class PasswordUtil {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256"; // we use PBKDF2 with HMAC-SHA256 for password hashing
    private static final int ITERATIONS = 600_000;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_LENGTH_BYTES = 16;
    
    private static final SecureRandom RANDOM = new SecureRandom(); // cryptographically strong random number generator
    
    // generates a random salt for password hashing
    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        RANDOM.nextBytes(salt);
        return salt;
    }
    
    // hashes the password with the given salt and returns the hash as a Base64 encoded string
    public static String hashPassword(String password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Password hashing failed", e);
        }
    }
    
    // checks if the password matches the stored hash by hashing the password with the same salt and comparing the hashes
    public static boolean checkPassword(String password, byte[] salt, String storedHash) {
        String attemptHash = hashPassword(password, salt);
        return MessageDigest.isEqual(attemptHash.getBytes(), storedHash.getBytes());
    }
}
