package com.nyx.ftpcdn.mapper;

import com.nyx.ftpcdn.entity.FileInfo;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 文件信息Mapper接口
 * 
 * @author nyx
 */
@Mapper
public interface FileInfoMapper {
    
    /**
     * 插入文件信息
     */
    @Insert("INSERT INTO file_info (original_file_name, generated_file_name, file_extension, " +
            "file_size, description, cdn_prefix, full_url, ftp_path, upload_time, download_count) " +
            "VALUES (#{originalFileName}, #{generatedFileName}, #{fileExtension}, #{fileSize}, " +
            "#{description}, #{cdnPrefix}, #{fullUrl}, #{ftpPath}, #{uploadTime}, #{downloadCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FileInfo fileInfo);
    
    /**
     * 根据ID查询文件信息
     */
    @Select("SELECT * FROM file_info WHERE id = #{id}")
    FileInfo selectById(Long id);
    
    /**
     * 查询所有文件信息（分页）
     */
    @Select("SELECT * FROM file_info ORDER BY upload_time DESC")
    List<FileInfo> selectAll();
    
    /**
     * 根据关键词搜索文件（模糊查询）
     */
    @Select("SELECT * FROM file_info WHERE original_file_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR description LIKE CONCAT('%', #{keyword}, '%') ORDER BY upload_time DESC")
    List<FileInfo> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 更新文件描述
     */
    @Update("UPDATE file_info SET description = #{description} WHERE id = #{id}")
    int updateDescription(@Param("id") Long id, @Param("description") String description);
    
    /**
     * 增加下载次数
     */
    @Update("UPDATE file_info SET download_count = download_count + 1 WHERE id = #{id}")
    int incrementDownloadCount(Long id);
    
    /**
     * 根据ID删除文件信息
     */
    @Delete("DELETE FROM file_info WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 统计文件总数
     */
    @Select("SELECT COUNT(*) FROM file_info")
    long countAll();
    
    /**
     * 根据关键词统计搜索结果数量
     */
    @Select("SELECT COUNT(*) FROM file_info WHERE original_file_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR description LIKE CONCAT('%', #{keyword}, '%')")
    long countByKeyword(@Param("keyword") String keyword);
}