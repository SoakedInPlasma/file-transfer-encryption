package com.fileintegrity.model;

public class FileDownloadResponse {
    private String fileName;
    private String fileDataBase64;

    public FileDownloadResponse(String fileName, String fileDataBase64) {
        this.fileName = fileName;
        this.fileDataBase64 = fileDataBase64;
    }

    public FileDownloadResponse() {
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
    
}
