package com.fileintegrity.services;

import java.util.Base64;
import java.util.List;

import com.fileintegrity.crypto.CryptoService;
import com.fileintegrity.crypto.DecryptResult;
import com.fileintegrity.model.EncryptedPackage;
import com.fileintegrity.model.FileDownloadResponse;
import com.fileintegrity.model.FileUploadRequest;
import com.fileintegrity.model.PackageSummaryDTO;
import com.fileintegrity.model.User;
import com.fileintegrity.repository.PackageMemoryStore;
import com.fileintegrity.repository.UserMemoryStore;

/*
* @author martin
 */
public class FileTransferService {


    //send method use fileupload request and senders username to send the file (RECEIVER IN FILEUPLOADREQUEST)
    public String sendFile(FileUploadRequest request, String senderUserName){
        //validate the request object
        if(request == null || request.getReceiver() == null || request.getFileDataBase64() == null){
            throw new IllegalArgumentException("Missing required file upload fields..");
        }

        //validation of sender and receiver
        User sender = UserMemoryStore.findByUserName(senderUserName);
        User receiver = UserMemoryStore.findByUserName(request.getReceiver());
        if(sender == null || receiver == null){
            throw new IllegalArgumentException("Sender or receiver does not exist..");
        }

        //convert base64 to byte array
        byte[] rawFileBytes = Base64.getDecoder().decode(request.getFileDataBase64());
        
        //encryption process using EncrpytForRecipient method from CryptoService
        EncryptedPackage encryptedPacakge = CryptoService.encryptForRecipient(rawFileBytes, request.getFileName(),sender, receiver);

        //store the encrypted package in memory
        PackageMemoryStore.savePackage(encryptedPacakge);
        
        //return the package ID to the sender
        return encryptedPacakge.getId();
    }

    //returns a list of packages by receiver username which will be the logged in user
    public List<PackageSummaryDTO> getInboxForUser(String receiverUserName){
        //validate the receiver username
        User receiver = UserMemoryStore.findByUserName(receiverUserName);
        if(receiver == null){
            throw new IllegalArgumentException("Receiver does not exist..");
        }
        //returns list of packages for logged in user, if no packages exist, returns empty list
        return PackageMemoryStore.findByReceiver(receiverUserName).stream().map(encryptedPackage -> new PackageSummaryDTO(encryptedPackage.getId(), encryptedPackage.getSender(), encryptedPackage.getFileName(), encryptedPackage.getOriginalHash())).toList();

    }
    //download and verify method takes in the package id and the receiver username, finds the package by id, validates the receiver, decrypts and verifies the package using CryptoService, and returns a FileDownloadResponse object with the file name and base64 encoded file data if successful, otherwise throws an exception
    public FileDownloadResponse downloadAndVerify(String packageId, String receiverUserName){
        EncryptedPackage encryptedPackage = PackageMemoryStore.findById(packageId);
        if(encryptedPackage == null){
            throw new IllegalArgumentException("Package does not exist..");
        }

        User receiver = UserMemoryStore.findByUserName(receiverUserName);
        if(receiver == null){
            throw new SecurityException("Authenticated user does not exist..");
        }

        //decryption process, hashing and signature verification using decryptAndVerify method from CryptoService
        DecryptResult result = CryptoService.decryptAndVerify(encryptedPackage, receiver);

        //if the decryption and verification fails, throw an exception with the message from the DecryptResult object
        if(!result.isVerified()){
            throw new SecurityException(result.getMessage());
        }

        //if successfull, convert the raw file bytes to base64 and return a FileDownloadResponse object with the file name and base64 encoded file data
        String base64DecryptedFile = Base64.getEncoder().encodeToString(result.getFile());
        return new FileDownloadResponse(encryptedPackage.getFileName(), base64DecryptedFile);
        
    }
    
}
