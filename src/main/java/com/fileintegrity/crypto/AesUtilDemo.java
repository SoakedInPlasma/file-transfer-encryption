/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

import java.util.Base64;
import javax.crypto.SecretKey;

/**
 *
 * @author Sam
 */
public class AesUtilDemo {
    public static void main(String[] agrs) {
        byte[] original = "This is a scret file".getBytes();
        
        SecretKey key = AesUtil.generateKey();
        byte[] iv = AesUtil.generateIv();
        
        // encrypt
        byte[] encrypted = AesUtil.encrypt(original, key, iv);
        System.out.println("Encrypted: "+Base64.getEncoder().encodeToString(encrypted));
        
        // decrypt
        byte[] decrypted = AesUtil.decrypt(encrypted, key, iv);
        System.out.println("Decrypted: "+ new String(decrypted));
        
        // Wrong key test
        try {
            AesUtil.decrypt(encrypted, AesUtil.generateKey(), iv);
            System.out.println("Wrong key");
        } catch (SecurityException e) {
            System.out.println("Wrong key: "+e.getMessage());
        }
    }
}