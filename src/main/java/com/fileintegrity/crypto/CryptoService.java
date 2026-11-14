/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import com.fileintegrity.model.EncryptedPackage;
import com.fileintegrity.model.User;

/**
 *
 * @author Sam
 */
public class CryptoService {
    // Encrypts the file for the receiver, signs it with the senders private key and returns an EncryptedPackage
    public static EncryptedPackage encryptForRecipient(byte[] file, String fileName, User sender, User receiver) {
        // 1. Fingerprinting the file
        String hash = HashUtil.sha256Hex(file);
        
        // 2. Sender signs the fingerprint with their PK
        byte[] signature = SignatureUtil.sign(hash.getBytes(StandardCharsets.UTF_8), sender.getPrivateKey());
        
        // 3. New AES key and IV
        SecretKey aesKey = AesUtil.generateKey();
        byte[] iv = AesUtil.generateIv();
        
        // 4. Locks with AES
        byte[] encryptedFile = AesUtil.encrypt(file, aesKey, iv);
        
        // 5. Lock the AES key with receivers PK
        byte[] encryptedAesKey = RsaUtil.encryptKey(aesKey, receiver.getPublicKey());
        
        // 6. Attachs the senders PK to check signature
        String senderPublicKey = RsaUtil.publicKeyToString(sender.getPublicKey());
        
        // 7. Return the EncryptedPackage to be sent to the receiver
        return new EncryptedPackage(sender.getUsername(), receiver.getUsername(), fileName, encryptedFile, encryptedAesKey, iv, hash, signature, senderPublicKey);
    }
    
    // Decrypts the file for the receiver, verifies the signature with the senders public key and returns a DecryptResult
    public static DecryptResult decryptAndVerify(EncryptedPackage pkg, User receiver) {
        // if sent to the wrong user
        if(!pkg.getReceiver().equals(receiver.getUsername())) {
            return DecryptResult.failure("Wrong User");
        }
        
        // 1. Unlock the AES key with receiver PK
        SecretKey aesKey;
        try {
            aesKey = RsaUtil.decryptKey(pkg.getEncryptedAesKey(), receiver.getPrivateKey());
        } catch (SecurityException e) {
            return DecryptResult.failure("Could not unlock file key");
        }
        
        // 2. Decrypt the file
        byte[] file;
        try {
            file = AesUtil.decrypt(pkg.getEncryptedFile(), aesKey, pkg.getIv());
        } catch (SecurityException e) {
            return DecryptResult.failure("File was modified in transit or storage");
        }
        
        // 3. rehash the decrypted file and compare to the original hash
        String newHash = HashUtil.sha256Hex(file);
        boolean hashMatches = newHash.equals(pkg.getOriginalHash());
        
        // 4. check the senders signature with senders PK
        PublicKey senderKey = RsaUtil.stringToPublicKey(pkg.getSenderPublicKey());
        boolean signatureValid = SignatureUtil.verify(pkg.getOriginalHash().getBytes(StandardCharsets.UTF_8), pkg.getSignature(), senderKey);
        
        // 5. Final check
        if(!hashMatches) {
            return new DecryptResult(null,false,signatureValid,"Integrity failed: different file");
        }
        if(!signatureValid) {
            return new DecryptResult(null,true,false,"Signature Invalid: sender could not be verified");
        }
        // 6. Return the decrypted file and verification results
        return new DecryptResult(file,true,true,"File Verified");
    }
}
