package com.fileintegrity.model;
/*
*@author Martin
*represents the response of a file download request, containing the file name and the file data in base64 format
*/
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
