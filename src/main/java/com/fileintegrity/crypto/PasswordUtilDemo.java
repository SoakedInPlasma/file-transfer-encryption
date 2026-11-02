/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

/**
 *
 * @author Sam
 */
public class PasswordUtilDemo {
    public static void main(String[] args) {
        byte[] salt = PasswordUtil.generateSalt();
        String stored = PasswordUtil.hashPassword("password123", salt);
        System.out.println("Stored Hash: "+ stored);
        
        System.out.println("Correct password: " + PasswordUtil.checkPassword("password123", salt, stored));
        System.out.println("Wrong password: " + PasswordUtil.checkPassword("wrongpass", salt, stored));
        
        // same pass different salt
        byte[] otherSalt = PasswordUtil.generateSalt();
        System.out.println("Same password new salt: " + PasswordUtil.hashPassword("password123", otherSalt));
    }
}
