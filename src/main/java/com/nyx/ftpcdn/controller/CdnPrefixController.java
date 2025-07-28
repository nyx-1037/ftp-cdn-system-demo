package com.nyx.ftpcdn.controller;

import com.nyx.ftpcdn.entity.CdnPrefix;
import com.nyx.ftpcdn.service.CdnPrefixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CDN前缀管理控制器
 * 
 * @author nyx
 */
@Controller
@RequestMapping("/cdn")
public class CdnPrefixController {
    
    @Autowired
    private CdnPrefixService cdnPrefixService;
    
    /**
     * CDN配置页面
     */
    @GetMapping
    public String cdnConfig(Model model) {
        List<CdnPrefix> cdnPrefixes = cdnPrefixService.getAllPrefixes();
        model.addAttribute("cdnPrefixes", cdnPrefixes);
        return "cdn-config";
    }
    
    /**
     * 添加CDN前缀
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCdnPrefix(
            @RequestParam String name,
            @RequestParam String prefix,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "false") Boolean isDefault,
            @RequestParam(required = false, defaultValue = "true") Boolean isActive) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证参数
            if (name == null || name.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "前缀名称不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            if (prefix == null || prefix.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "前缀URL不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 验证URL格式
            if (!prefix.startsWith("http://") && !prefix.startsWith("https://")) {
                result.put("success", false);
                result.put("message", "前缀URL必须以http://或https://开头");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 添加CDN前缀
            CdnPrefix cdnPrefix = cdnPrefixService.addPrefix(
                name.trim(), 
                prefix.trim(), 
                description != null ? description.trim() : "", 
                isDefault,
                isActive
            );
            
            result.put("success", true);
            result.put("message", "CDN前缀添加成功");
            result.put("data", cdnPrefix);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加CDN前缀失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 更新CDN前缀
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCdnPrefix(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String prefix,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "false") Boolean isDefault,
            @RequestParam(required = false, defaultValue = "true") Boolean isActive) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证参数
            if (name == null || name.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "前缀名称不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            if (prefix == null || prefix.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "前缀URL不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 验证URL格式
            if (!prefix.startsWith("http://") && !prefix.startsWith("https://")) {
                result.put("success", false);
                result.put("message", "前缀URL必须以http://或https://开头");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 更新CDN前缀
            boolean success = cdnPrefixService.updatePrefix(
                id,
                name.trim(),
                prefix.trim(),
                description != null ? description.trim() : "",
                isDefault,
                isActive
            );
            
            if (success) {
                result.put("success", true);
                result.put("message", "CDN前缀更新成功");
            } else {
                result.put("success", false);
                result.put("message", "CDN前缀更新失败");
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新CDN前缀失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 设置默认CDN前缀
     */
    @PostMapping("/{id}/default")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setDefaultPrefix(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = cdnPrefixService.setDefaultPrefix(id);
            if (success) {
                result.put("success", true);
                result.put("message", "默认CDN前缀设置成功");
            } else {
                result.put("success", false);
                result.put("message", "默认CDN前缀设置失败");
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "设置默认CDN前缀失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 删除CDN前缀
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCdnPrefix(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = cdnPrefixService.deletePrefix(id);
            if (success) {
                result.put("success", true);
                result.put("message", "CDN前缀删除成功");
            } else {
                result.put("success", false);
                result.put("message", "CDN前缀删除失败");
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除CDN前缀失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 获取所有CDN前缀API
     */
    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<CdnPrefix>> getCdnPrefixList() {
        List<CdnPrefix> cdnPrefixes = cdnPrefixService.getAllPrefixes();
        return ResponseEntity.ok(cdnPrefixes);
    }
    
    /**
     * 获取启用的CDN前缀API
     */
    @GetMapping("/api/active")
    @ResponseBody
    public ResponseEntity<List<CdnPrefix>> getActiveCdnPrefixes() {
        List<CdnPrefix> cdnPrefixes = cdnPrefixService.getAllActivePrefixes();
        return ResponseEntity.ok(cdnPrefixes);
    }
    
    /**
     * 根据ID获取CDN前缀
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CdnPrefix> getCdnPrefix(@PathVariable Long id) {
        try {
            CdnPrefix cdnPrefix = cdnPrefixService.getPrefixById(id);
            if (cdnPrefix != null) {
                return ResponseEntity.ok(cdnPrefix);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取默认CDN前缀API
     */
    @GetMapping("/api/default")
    @ResponseBody
    public ResponseEntity<CdnPrefix> getDefaultCdnPrefix() {
        CdnPrefix defaultPrefix = cdnPrefixService.getDefaultPrefix();
        return ResponseEntity.ok(defaultPrefix);
    }
}