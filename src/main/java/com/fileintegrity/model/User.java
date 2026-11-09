/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.model;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author Sam
 */

public class User {
    private final String username;
    private final byte[] passwordSalt; // random salt from PasswordUtil
    private final String passwordHash; // hashed password from PasswordUtil
    private final KeyPair keyPair; // RSA Public and private key from RsaUtil

    public User(String username, byte[] passwordSalt, String passwordHash, KeyPair keyPair) {
        this.username = username;
        this.passwordSalt = passwordSalt;
        this.passwordHash = passwordHash;
        this.keyPair = keyPair;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
        
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }
    
    public PrivateKey getPrivataeKey() {
        return keyPair.getPrivate();
    }
}
