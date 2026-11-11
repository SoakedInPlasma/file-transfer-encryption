/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

/**
 *
 * @author Sam
 */
public class DecryptResult {
    private final byte[] file;
    private final boolean hashMatches;
    private final boolean signatureValid;
    private final String message;

    public DecryptResult(byte[] file, boolean hashMatches, boolean signatureValid, String message) {
        this.file = file;
        this.hashMatches = hashMatches;
        this.signatureValid = signatureValid;
        this.message = message;
    }

    public byte[] getFile() {
        return file;
    }

    public boolean isHashMatches() {
        return hashMatches;
    }

    public boolean isSignatureValid() {
        return signatureValid;
    }

    public String getMessage() {
        return message;
    }
    
    // creates a failed result with no file
    public static DecryptResult failure(String message) {
        return new DecryptResult(null, false, false, message);
    }
    
    public boolean isVerified() {
        return hashMatches && signatureValid;
    }
}
