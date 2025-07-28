package com.nyx.ftpcdn.entity;

import java.time.LocalDateTime;

/**
 * 文件信息实体类
 * 
 * @author nyx
 */
public class FileInfo {
    
    private Long id;
    private String originalFileName;  // 原始文件名
    private String generatedFileName; // 生成的文件名(UUID+时间戳)
    private String fileExtension;     // 文件扩展名
    private Long fileSize;            // 文件大小(字节)
    private String description;       // 文件描述
    private String cdnPrefix;         // CDN前缀
    private String fullUrl;           // 完整的CDN访问URL
    private String ftpPath;           // FTP服务器上的路径
    private LocalDateTime uploadTime; // 上传时间
    private Integer downloadCount;    // 下载次数
    
    public FileInfo() {}
    
    public FileInfo(String originalFileName, String generatedFileName, String fileExtension, 
                   Long fileSize, String description, String cdnPrefix, String fullUrl, 
                   String ftpPath) {
        this.originalFileName = originalFileName;
        this.generatedFileName = generatedFileName;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.description = description;
        this.cdnPrefix = cdnPrefix;
        this.fullUrl = fullUrl;
        this.ftpPath = ftpPath;
        this.uploadTime = LocalDateTime.now();
        this.downloadCount = 0;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalFileName() {
        return originalFileName;
    }
    
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
    
    public String getGeneratedFileName() {
        return generatedFileName;
    }
    
    public void setGeneratedFileName(String generatedFileName) {
        this.generatedFileName = generatedFileName;
    }
    
    public String getFileExtension() {
        return fileExtension;
    }
    
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCdnPrefix() {
        return cdnPrefix;
    }
    
    public void setCdnPrefix(String cdnPrefix) {
        this.cdnPrefix = cdnPrefix;
    }
    
    public String getFullUrl() {
        return fullUrl;
    }
    
    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }
    
    public String getFtpPath() {
        return ftpPath;
    }
    
    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }
    
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    public Integer getDownloadCount() {
        return downloadCount;
    }
    
    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", originalFileName='" + originalFileName + '\'' +
                ", generatedFileName='" + generatedFileName + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", fileSize=" + fileSize +
                ", description='" + description + '\'' +
                ", cdnPrefix='" + cdnPrefix + '\'' +
                ", fullUrl='" + fullUrl + '\'' +
                ", ftpPath='" + ftpPath + '\'' +
                ", uploadTime=" + uploadTime +
                ", downloadCount=" + downloadCount +
                '}';
    }
}