/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.crypto;

/**
 *
 * @author Sam
 */
public class HashUtilDemo {
    public static void main(String[] args) {
        // test the HashUtil class
        System.out.println(HashUtil.sha256Hex("abc".getBytes()));

        // test that different inputs produce different hashes
        System.out.println(HashUtil.sha256Hex("hello".getBytes())); 
        System.out.println(HashUtil.sha256Hex("hello!".getBytes()));
    }
}
