/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fileintegrity.services;

import com.fileintegrity.crypto.PasswordUtil;
import com.fileintegrity.crypto.RsaUtil;
import com.fileintegrity.model.User;
import com.fileintegrity.model.UserCredentials;
import com.fileintegrity.repository.UserMemoryStore;
import java.security.KeyPair;

/**
 *
 * @author martin
 */

//register and login methods
public class AuthService {
    
    //method for registering user, return false if not valid, true when stored in memory
    public boolean registerUser(UserCredentials userCredentials) throws Exception{
        //validate to make sure fields are not null
        if(userCredentials == null || userCredentials.getUserName() == null || userCredentials.getPassword() == null){
            return false;
        }
        
        //validate to make sure fields are not empty
        String userName = userCredentials.getUserName().trim();
        if(userName.isEmpty() || userCredentials.getPassword().isEmpty()){
            return false;
        }
        
        //check if username is taken
        if(UserMemoryStore.findByUserName(userName) != null){
            return false; // user exists already
        }
        
        //generate salt and hash password, methods from passwordutil
        byte[] salt = PasswordUtil.generateSalt();
        String passwordHash = PasswordUtil.hashPassword(userCredentials.getPassword(), salt);
        
        //generate RSA keypair for user
        KeyPair keyPair = RsaUtil.generateKeyPair();
        
        //build domain user object
        User newUser = new User(userName, salt, passwordHash, keyPair);
        
        //save user entity to memory store
        return UserMemoryStore.saveUser(newUser);
        
        
    }
    //method for logging user in, return false if fields empty, user doesnt exist or if password does not match
    public boolean  loginUser(UserCredentials userCredentials){
        //validate to make sure fields are not null
        if(userCredentials == null || userCredentials.getUserName() == null || userCredentials.getPassword() == null){
            return false;
        }
        
        //retrieve user from memory store
        User storedUser = UserMemoryStore.findByUserName(userCredentials.getUserName().trim());
        if(storedUser == null){
            return false; //if no user exists return null
        }
        
        //hash incoming password to verify
        String computedHash = PasswordUtil.hashPassword(userCredentials.getPassword(), storedUser.getPasswordSalt());
        
        //compare hashed passwords
        return computedHash.equals(storedUser.getPasswordHash());
                
    }
    
    public User getUserByUserName(String userName){
        return UserMemoryStore.findByUserName(userName);
    }
    
}
