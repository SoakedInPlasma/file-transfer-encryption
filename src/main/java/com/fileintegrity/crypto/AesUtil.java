/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 *
 * @author Sam
 */
public class AesUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE_BITS = 256;
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE_BITS, RANDOM);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("AES is not available", e);
        }
    }
    
    public static byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH_BYTES];
        RANDOM.nextBytes(iv);
        return iv;
    }
    
    public static byte[] encrypt(byte[] data, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e){
            throw new IllegalStateException("AES encryption failed", e);
        }
    }
    
    public static byte[] decrypt(byte[] encrypted, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return cipher.doFinal(encrypted);
        } catch (AEADBadTagException e) {
            throw new SecurityException("Decryption failed");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("AES decryption failed", e);
        }
    }
}