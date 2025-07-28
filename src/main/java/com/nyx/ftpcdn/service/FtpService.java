package com.nyx.ftpcdn.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FTP服务类
 * 
 * @author nyx
 */
@Service
public class FtpService {
    
    @Value("${ftp.host:localhost}")
    private String ftpHost;
    
    @Value("${ftp.port:21}")
    private int ftpPort;
    
    @Value("${ftp.username:anonymous}")
    private String ftpUsername;
    
    @Value("${ftp.password:}")
    private String ftpPassword;
    
    @Value("${ftp.basePath:/uploads}")
    private String ftpBasePath;
    
    /**
     * 上传文件到FTP服务器
     * 
     * @param file 要上传的文件
     * @param fileName 文件名
     * @return 上传成功返回true，否则返回false
     */
    public boolean uploadFile(MultipartFile file, String fileName) {
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接FTP服务器
            ftpClient.connect(ftpHost, ftpPort);
            
            // 检查连接状态
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.err.println("FTP服务器连接失败，返回码：" + replyCode);
                return false;
            }
            
            // 登录FTP服务器
            boolean loginSuccess = ftpClient.login(ftpUsername, ftpPassword);
            if (!loginSuccess) {
                System.err.println("FTP服务器登录失败");
                return false;
            }
            
            // 设置文件传输模式为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            
            // 进入被动模式
            ftpClient.enterLocalPassiveMode();
            
            // 创建上传目录（如果不存在）
            createDirectoryIfNotExists(ftpClient, ftpBasePath);
            
            // 切换到上传目录
            ftpClient.changeWorkingDirectory(ftpBasePath);
            
            // 上传文件
            try (InputStream inputStream = file.getInputStream()) {
                boolean uploadSuccess = ftpClient.storeFile(fileName, inputStream);
                if (uploadSuccess) {
                    System.out.println("文件上传成功：" + fileName);
                    return true;
                } else {
                    System.err.println("文件上传失败：" + fileName);
                    return false;
                }
            }
            
        } catch (IOException e) {
            System.err.println("FTP上传过程中发生异常：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // 关闭FTP连接
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                System.err.println("关闭FTP连接时发生异常：" + e.getMessage());
            }
        }
    }
    
    /**
     * 删除FTP服务器上的文件
     * 
     * @param fileName 文件名
     * @return 删除成功返回true，否则返回false
     */
    public boolean deleteFile(String fileName) {
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接并登录FTP服务器
            ftpClient.connect(ftpHost, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);
            
            // 切换到上传目录
            ftpClient.changeWorkingDirectory(ftpBasePath);
            
            // 删除文件
            boolean deleteSuccess = ftpClient.deleteFile(fileName);
            if (deleteSuccess) {
                System.out.println("文件删除成功：" + fileName);
                return true;
            } else {
                System.err.println("文件删除失败：" + fileName);
                return false;
            }
            
        } catch (IOException e) {
            System.err.println("FTP删除过程中发生异常：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                System.err.println("关闭FTP连接时发生异常：" + e.getMessage());
            }
        }
    }
    
    /**
     * 创建目录（如果不存在）
     * 
     * @param ftpClient FTP客户端
     * @param dirPath 目录路径
     */
    private void createDirectoryIfNotExists(FTPClient ftpClient, String dirPath) throws IOException {
        String[] dirs = dirPath.split("/");
        String currentDir = "";
        
        for (String dir : dirs) {
            if (dir.isEmpty()) continue;
            
            currentDir += "/" + dir;
            
            // 尝试切换到目录
            if (!ftpClient.changeWorkingDirectory(currentDir)) {
                // 目录不存在，创建目录
                if (ftpClient.makeDirectory(currentDir)) {
                    System.out.println("创建目录成功：" + currentDir);
                } else {
                    System.err.println("创建目录失败：" + currentDir);
                }
            }
        }
    }
    
    /**
     * 从FTP服务器下载文件
     * 
     * @param fileName 文件名
     * @return 文件内容字节数组，下载失败返回null
     */
    public byte[] downloadFile(String fileName) {
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接FTP服务器
            ftpClient.connect(ftpHost, ftpPort);
            
            // 检查连接状态
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.err.println("FTP服务器连接失败，返回码：" + replyCode);
                return null;
            }
            
            // 登录FTP服务器
            boolean loginSuccess = ftpClient.login(ftpUsername, ftpPassword);
            if (!loginSuccess) {
                System.err.println("FTP服务器登录失败");
                return null;
            }
            
            // 设置文件传输模式为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            
            // 进入被动模式
            ftpClient.enterLocalPassiveMode();
            
            // 切换到上传目录
            ftpClient.changeWorkingDirectory(ftpBasePath);
            
            // 下载文件
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                boolean downloadSuccess = ftpClient.retrieveFile(fileName, outputStream);
                if (downloadSuccess) {
                    System.out.println("文件下载成功：" + fileName);
                    return outputStream.toByteArray();
                } else {
                    System.err.println("文件下载失败：" + fileName);
                    return null;
                }
            }
            
        } catch (IOException e) {
            System.err.println("FTP下载过程中发生异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            // 关闭FTP连接
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                System.err.println("关闭FTP连接时发生异常：" + e.getMessage());
            }
        }
    }
    
    /**
     * 获取FTP基础路径
     */
    public String getFtpBasePath() {
        return ftpBasePath;
    }
}