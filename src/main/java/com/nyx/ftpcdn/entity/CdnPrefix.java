package com.nyx.ftpcdn.entity;

import java.time.LocalDateTime;

/**
 * CDN前缀配置实体类
 * 
 * @author nyx
 */
public class CdnPrefix {
    
    private Long id;
    private String name;              // 前缀名称
    private String prefix;            // CDN前缀URL
    private String description;       // 描述
    private Boolean isDefault;        // 是否为默认前缀
    private Boolean isActive;         // 是否启用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    
    public CdnPrefix() {}
    
    public CdnPrefix(String name, String prefix, String description, Boolean isDefault, Boolean isActive) {
        this.name = name;
        this.prefix = prefix;
        this.description = description;
        this.isDefault = isDefault;
        this.isActive = isActive != null ? isActive : true;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "CdnPrefix{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", description='" + description + '\'' +
                ", isDefault=" + isDefault +
                ", isActive=" + isActive +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}