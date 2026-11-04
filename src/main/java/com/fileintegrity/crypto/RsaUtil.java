/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Sam
 */
public class RsaUtil {
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE_BITS = 2048; //RSA size standard
    private static final String TRANSFORMATION = "RSA/ECB/OAEPadding";
    private static final OAEPParameterSpec OAEP_SPEC = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
    
    // creates a new RSA key pair (public + private) each user will get 1
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(KEY_SIZE_BITS);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA is not available", e);
        }
    }
    
    // Locks the AES key with the receivers public key
    public static byte[] encryptKey(SecretKey aesKey, PublicKey receiverPublicKey) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey, OAEP_SPEC);
            return cipher.doFinal(aesKey.getEncoded());
        } catch(GeneralSecurityException e) {
            throw new IllegalStateException("RSA Encryption failed", e);
        }
    }
    
    // Unlocks the AES key with the receivers private key
    public static SecretKey decryptKey(byte[] encryptedKey, PrivateKey receiverPrivateKey) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, receiverPrivateKey, OAEP_SPEC);
            byte[] keyBytes = cipher.doFinal(encryptedKey);
            return new SecretKeySpec(keyBytes, "AES");
        } catch (BadPaddingException e) {
            throw new SecurityException("Could not unlock key", e);
        } catch(GeneralSecurityException e) {
            throw new IllegalStateException("RSA decryption failed", e);
        }
    }
}