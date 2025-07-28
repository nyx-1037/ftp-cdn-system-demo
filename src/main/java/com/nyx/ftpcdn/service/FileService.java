package com.nyx.ftpcdn.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyx.ftpcdn.entity.FileInfo;
import com.nyx.ftpcdn.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 文件服务类
 * 
 * @author nyx
 */
@Service
public class FileService {
    
    @Autowired
    private FileInfoMapper fileInfoMapper;
    
    @Autowired
    private FtpService ftpService;
    
    /**
     * 上传文件
     * 
     * @param file 上传的文件
     * @param description 文件描述
     * @param cdnPrefix CDN前缀
     * @return 上传成功的文件信息
     */
    public FileInfo uploadFile(MultipartFile file, String description, String cdnPrefix) {
        try {
            // 获取原始文件名和扩展名
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);
            
            // 生成新的文件名：UUID + 时间戳 + 扩展名
            String generatedFileName = generateFileName(fileExtension);
            
            // 上传文件到FTP服务器
            boolean uploadSuccess = ftpService.uploadFile(file, generatedFileName);
            if (!uploadSuccess) {
                throw new RuntimeException("文件上传到FTP服务器失败");
            }
            
            // 构建完整的CDN访问URL
            String fullUrl = buildFullUrl(cdnPrefix, generatedFileName);
            
            // 构建FTP路径
            String ftpPath = ftpService.getFtpBasePath() + "/" + generatedFileName;
            
            // 创建文件信息对象
            FileInfo fileInfo = new FileInfo(
                originalFileName,
                generatedFileName,
                fileExtension,
                file.getSize(),
                description,
                cdnPrefix,
                fullUrl,
                ftpPath
            );
            
            // 保存到数据库
            int result = fileInfoMapper.insert(fileInfo);
            if (result > 0) {
                System.out.println("文件信息保存成功：" + fileInfo);
                return fileInfo;
            } else {
                throw new RuntimeException("文件信息保存到数据库失败");
            }
            
        } catch (Exception e) {
            System.err.println("文件上传过程中发生异常：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }
    
    /**
     * 分页查询文件列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public PageInfo<FileInfo> getFileList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfo> fileList = fileInfoMapper.selectAll();
        return new PageInfo<>(fileList);
    }
    
    /**
     * 搜索文件（分页）
     * 
     * @param keyword 搜索关键词
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public PageInfo<FileInfo> searchFiles(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileInfo> fileList = fileInfoMapper.searchByKeyword(keyword);
        return new PageInfo<>(fileList);
    }
    
    /**
     * 根据ID查询文件信息
     * 
     * @param id 文件ID
     * @return 文件信息
     */
    public FileInfo getFileById(Long id) {
        return fileInfoMapper.selectById(id);
    }
    
    /**
     * 更新文件描述
     * 
     * @param id 文件ID
     * @param description 新的描述
     * @return 更新是否成功
     */
    public boolean updateFileDescription(Long id, String description) {
        int result = fileInfoMapper.updateDescription(id, description);
        return result > 0;
    }
    
    /**
     * 增加文件下载次数
     * 
     * @param id 文件ID
     */
    public void incrementDownloadCount(Long id) {
        fileInfoMapper.incrementDownloadCount(id);
    }
    
    /**
     * 从FTP服务器下载文件
     * 
     * @param fileName 文件名
     * @return 文件内容字节数组
     */
    public byte[] downloadFileFromFtp(String fileName) {
        return ftpService.downloadFile(fileName);
    }
    
    /**
     * 删除文件
     * 
     * @param id 文件ID
     * @return 删除是否成功
     */
    public boolean deleteFile(Long id) {
        try {
            // 先查询文件信息
            FileInfo fileInfo = fileInfoMapper.selectById(id);
            if (fileInfo == null) {
                return false;
            }
            
            // 删除FTP服务器上的文件
            boolean ftpDeleteSuccess = ftpService.deleteFile(fileInfo.getGeneratedFileName());
            if (!ftpDeleteSuccess) {
                System.err.println("删除FTP文件失败，但继续删除数据库记录");
            }
            
            // 删除数据库记录
            int result = fileInfoMapper.deleteById(id);
            return result > 0;
            
        } catch (Exception e) {
            System.err.println("删除文件过程中发生异常：" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 生成文件名：UUID + 时间戳 + 扩展名
     * 
     * @param fileExtension 文件扩展名
     * @return 生成的文件名
     */
    private String generateFileName(String fileExtension) {
        // 生成UUID（取前8位）
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        
        // 生成时间戳（精确到秒）
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        // 组合文件名
        return uuid + "_" + timestamp + fileExtension;
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param fileName 文件名
     * @return 扩展名（包含点号）
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        
        return fileName.substring(lastDotIndex);
    }
    
    /**
     * 构建完整的CDN访问URL
     * 
     * @param cdnPrefix CDN前缀
     * @param fileName 文件名
     * @return 完整的URL
     */
    private String buildFullUrl(String cdnPrefix, String fileName) {
        if (cdnPrefix.endsWith("/")) {
            return cdnPrefix + fileName;
        } else {
            return cdnPrefix + "/" + fileName;
        }
    }
}