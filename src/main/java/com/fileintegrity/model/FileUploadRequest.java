package com.fileintegrity.model;

public class FileUploadRequest {
    private String receiver;
    private String fileName;
    private String fileDataBase64;


    //this is the DTO for the file upload request 
    public FileUploadRequest() {
    }

     public FileUploadRequest(String receiver, String fileName, String fileDataBase64) {
        this.receiver = receiver;
        this.fileName = fileName;
        this.fileDataBase64 = fileDataBase64;
    }   

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDataBase64() {
        return fileDataBase64;
    }

    public void setFileDataBase64(String fileDataBase64) {
        this.fileDataBase64 = fileDataBase64;
    }


    public String getReceiver() {
        return receiver;
    }
    

    
}
