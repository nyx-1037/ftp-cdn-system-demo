package com.nyx.ftpcdn.mapper;

import com.nyx.ftpcdn.entity.CdnPrefix;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * CDN前缀Mapper接口
 * 
 * @author nyx
 */
@Mapper
public interface CdnPrefixMapper {
    
    /**
     * 插入CDN前缀配置
     */
    @Insert("INSERT INTO cdn_prefix (name, prefix, description, is_default, is_active, create_time, update_time) " +
            "VALUES (#{name}, #{prefix}, #{description}, #{isDefault}, #{isActive}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CdnPrefix cdnPrefix);
    
    /**
     * 根据ID查询CDN前缀
     */
    @Select("SELECT * FROM cdn_prefix WHERE id = #{id}")
    CdnPrefix selectById(Long id);
    
    /**
     * 查询所有启用的CDN前缀
     */
    @Select("SELECT * FROM cdn_prefix WHERE is_active = 1 ORDER BY is_default DESC, create_time ASC")
    List<CdnPrefix> selectAllActive();
    
    /**
     * 查询所有CDN前缀
     */
    @Select("SELECT * FROM cdn_prefix ORDER BY is_default DESC, create_time ASC")
    List<CdnPrefix> selectAll();
    
    /**
     * 查询默认CDN前缀
     */
    @Select("SELECT * FROM cdn_prefix WHERE is_default = 1 AND is_active = 1 LIMIT 1")
    CdnPrefix selectDefault();
    
    /**
     * 更新CDN前缀
     */
    @Update("UPDATE cdn_prefix SET name = #{name}, prefix = #{prefix}, description = #{description}, " +
            "is_default = #{isDefault}, is_active = #{isActive}, update_time = #{updateTime} WHERE id = #{id}")
    int update(CdnPrefix cdnPrefix);
    
    /**
     * 设置默认前缀（先清除所有默认标记）
     */
    @Update("UPDATE cdn_prefix SET is_default = 0")
    int clearAllDefault();
    
    /**
     * 设置指定前缀为默认
     */
    @Update("UPDATE cdn_prefix SET is_default = 1 WHERE id = #{id}")
    int setDefault(Long id);
    
    /**
     * 根据ID删除CDN前缀
     */
    @Delete("DELETE FROM cdn_prefix WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 检查前缀是否已存在
     */
    @Select("SELECT COUNT(*) FROM cdn_prefix WHERE prefix = #{prefix} AND id != #{excludeId}")
    int countByPrefix(@Param("prefix") String prefix, @Param("excludeId") Long excludeId);
    
    /**
     * 检查名称是否已存在
     */
    @Select("SELECT COUNT(*) FROM cdn_prefix WHERE name = #{name} AND id != #{excludeId}")
    int countByName(@Param("name") String name, @Param("excludeId") Long excludeId);
}