package com.fileintegrity.model;
/*
*@author Martin
*DTO for the package summary when user lists packages to choose from
*/
public class PackageSummaryDTO {
    private String id;
    private String sender;
    private String fileName;
    private String originalHash; // we will use this hash to verify the integrity of the file after download

    public PackageSummaryDTO(String id, String sender, String fileName, String originalHash) {
        this.id = id;
        this.sender = sender;
        this.fileName = fileName;
        this.originalHash = originalHash;
    }

    public PackageSummaryDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalHash() {
        return originalHash;
    }

    public void setOriginalHash(String originalHash) {
        this.originalHash = originalHash;
    }
    
}
