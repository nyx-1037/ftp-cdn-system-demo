package com.nyx.ftpcdn.controller;

import com.github.pagehelper.PageInfo;
import com.nyx.ftpcdn.entity.CdnPrefix;
import com.nyx.ftpcdn.entity.FileInfo;
import com.nyx.ftpcdn.service.CdnPrefixService;
import com.nyx.ftpcdn.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件管理控制器
 * 
 * @author nyx
 */
@Controller
@RequestMapping("/")
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private CdnPrefixService cdnPrefixService;
    
    /**
     * 首页 - 文件管理主页
     */
    @GetMapping
    public String index(Model model,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String keyword) {
        
        // 获取CDN前缀列表
        List<CdnPrefix> cdnPrefixes = cdnPrefixService.getAllActivePrefixes();
        model.addAttribute("cdnPrefixes", cdnPrefixes);
        
        // 获取默认CDN前缀
        CdnPrefix defaultPrefix = cdnPrefixService.getDefaultPrefix();
        model.addAttribute("defaultPrefix", defaultPrefix);
        
        // 获取文件列表（分页）
        PageInfo<FileInfo> pageInfo;
        if (keyword != null && !keyword.trim().isEmpty()) {
            pageInfo = fileService.searchFiles(keyword.trim(), page, size);
            model.addAttribute("keyword", keyword);
        } else {
            pageInfo = fileService.getFileList(page, size);
        }
        
        model.addAttribute("pageInfo", pageInfo);
        
        return "index";
    }
    
    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("cdnPrefix") String cdnPrefix) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择要上传的文件");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 验证文件大小（限制为50MB）
            if (file.getSize() > 50 * 1024 * 1024) {
                result.put("success", false);
                result.put("message", "文件大小不能超过50MB");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 上传文件
            FileInfo fileInfo = fileService.uploadFile(file, description, cdnPrefix);
            
            result.put("success", true);
            result.put("message", "文件上传成功");
            result.put("data", fileInfo);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "文件上传失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 文件详情页
     */
    @GetMapping("/file/{id}")
    public String fileDetail(@PathVariable Long id, Model model) {
        FileInfo fileInfo = fileService.getFileById(id);
        if (fileInfo == null) {
            return "redirect:/?error=文件不存在";
        }
        
        model.addAttribute("fileInfo", fileInfo);
        return "file-detail";
    }
    
    /**
     * 更新文件描述
     */
    @PostMapping("/file/{id}/description")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateFileDescription(
            @PathVariable Long id,
            @RequestParam String description) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = fileService.updateFileDescription(id, description);
            if (success) {
                result.put("success", true);
                result.put("message", "描述更新成功");
            } else {
                result.put("success", false);
                result.put("message", "描述更新失败");
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "描述更新失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/file/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = fileService.deleteFile(id);
            if (success) {
                result.put("success", true);
                result.put("message", "文件删除成功");
            } else {
                result.put("success", false);
                result.put("message", "文件删除失败");
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "文件删除失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 增加下载次数
     */
    @PostMapping("/file/{id}/download")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> incrementDownloadCount(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            fileService.incrementDownloadCount(id);
            result.put("success", true);
            result.put("message", "下载次数已更新");
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新下载次数失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 下载文件
     */
    @GetMapping("/file/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        try {
            // 获取文件信息
            FileInfo fileInfo = fileService.getFileById(id);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 从FTP服务器下载文件
            byte[] fileContent = fileService.downloadFileFromFtp(fileInfo.getGeneratedFileName());
            if (fileContent == null) {
                return ResponseEntity.internalServerError().build();
            }
            
            // 增加下载次数
            fileService.incrementDownloadCount(id);
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(fileContent.length);
            headers.set("Content-Disposition", "attachment; filename*=UTF-8''" + java.net.URLEncoder.encode(fileInfo.getOriginalFileName(), StandardCharsets.UTF_8.toString()));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取文件列表API
     */
    @GetMapping("/api/files")
    @ResponseBody
    public ResponseEntity<PageInfo<FileInfo>> getFileList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        
        PageInfo<FileInfo> pageInfo;
        if (keyword != null && !keyword.trim().isEmpty()) {
            pageInfo = fileService.searchFiles(keyword.trim(), page, size);
        } else {
            pageInfo = fileService.getFileList(page, size);
        }
        
        return ResponseEntity.ok(pageInfo);
    }
}