/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.repository;

import com.fileintegrity.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author martin
 */
//this will be our in memory storage for users, lightweight method of saving our user info when we start server
//DOES NOT PERSIST WHEN RESTARTING SERVER
public class UserMemoryStore {
    private static final Map<String, User> userDb = new ConcurrentHashMap<>();
    
    public static boolean saveUser(User user){
        //returns false if user already exists
        return userDb.putIfAbsent(user.getUsername(), user) == null;
    }
    
    public static User findByUserName(String userName){
        return userDb.get(userName);
    }

    //return registered usernames without exposing user credentials
    public static java.util.List<String> getAllUsernames() {
        return userDb.keySet().stream()
            .sorted()
            .toList();
    }
    
    
}
