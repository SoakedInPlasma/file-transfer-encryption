/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
/**
 *
 * @author Sam
 */
public class SignatureUtil {
    // SHA-256 hashes the data then RSA signs that hash
    private static final String ALGORITHM = "SHA256withRSA";
    
    // signs the data with the senders private key
    public static byte[] sign(byte[] data, PrivateKey senderPrivateKey) {
        try {
            Signature signer = Signature.getInstance(ALGORITHM);
            signer.initSign(senderPrivateKey);
            signer.update(data);
            return signer.sign();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Signing failed", e);
        }
    }
    
    // checks the signature with the public key and returns true if the sender signed the exact data
    public static boolean verify(byte[] data, byte[] signature, PublicKey senderPublicKey) {
        try {
            Signature verifier = Signature.getInstance(ALGORITHM);
            verifier.initVerify(senderPublicKey);
            verifier.update(data);
            return verifier.verify(signature);
        } catch(SignatureException e) {
            return false;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Verification failed", e);
        }
    }
}
