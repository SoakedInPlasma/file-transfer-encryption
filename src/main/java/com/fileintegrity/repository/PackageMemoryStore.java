package com.fileintegrity.repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fileintegrity.model.EncryptedPackage;
/* 
 * @author Martin
 */

//this will be our in memory store for packages that are sent and received, lightweight method of saving our package info when we start server ***DOES NOT PERSIST WHEN RESTARTING SERVER
public class PackageMemoryStore {
    private static final ConcurrentHashMap<String, EncryptedPackage> packageDb = new ConcurrentHashMap<>();


    public static boolean savePackage(EncryptedPackage encryptedPackage){
        //returns false if package already exists
        //putIfAbsent, either returns the value assosciated with key, or null if no mapping for key exists
        //null == null returns true, meaning the package was saved successfully, otherwise returns false 
        return packageDb.putIfAbsent(encryptedPackage.getId(), encryptedPackage) == null;
    }
    
    //returns package if it exists, otherwise returns null
    public static EncryptedPackage findById(String id){
        return packageDb.get(id);
    }

    public static List<EncryptedPackage> findByReceiver(String receiver){
        //returns a list of encrypted package objects, which are checked with filter and a predicate to see if the receiver matches the receiver of the package, then collects them into a list and returns it
        return packageDb.values().stream().filter(encryptedPackage -> encryptedPackage.getReceiver().equalsIgnoreCase(receiver)).collect(Collectors.toList());
        
    }
    
}
