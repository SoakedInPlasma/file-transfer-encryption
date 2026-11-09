/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.model;

import java.util.UUID;

/**
 *
 * @author Sam
 */
public class EncryptedPackage {
    private final String id;
    private final String sender;
    private final String receiver;
    private final String fileName;
    private final byte[] enccryptedFile; // file locked with AES
    private final byte[] encryptedAesKey; // AES key locked with RSA PK
    private final byte[] iv;
    private final String originalHash;
    private final byte[] signataure;
    private final String senderPublicKey;

    public EncryptedPackage(String id, String sender, String receiver, String fileName, byte[] enccryptedFile, byte[] encryptedAesKey, byte[] iv, String originalHash, byte[] signataure, String senderPublicKey) {
        this.id = UUID.randomUUID().toString(); // gives package a unique ID
        this.sender = sender;
        this.receiver = receiver;
        this.fileName = fileName;
        this.enccryptedFile = enccryptedFile;
        this.encryptedAesKey = encryptedAesKey;
        this.iv = iv;
        this.originalHash = originalHash;
        this.signataure = signataure;
        this.senderPublicKey = senderPublicKey;
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getEnccryptedFile() {
        return enccryptedFile;
    }

    public byte[] getEncryptedAesKey() {
        return encryptedAesKey;
    }

    public byte[] getIv() {
        return iv;
    }

    public String getOriginalHash() {
        return originalHash;
    }

    public byte[] getSignataure() {
        return signataure;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }  
}
