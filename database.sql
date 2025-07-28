-- FTP-CDN系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ftp_cdn_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ftp_cdn_system;

-- 创建CDN前缀配置表
CREATE TABLE IF NOT EXISTS cdn_prefix (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(100) NOT NULL UNIQUE COMMENT '前缀名称',
    prefix VARCHAR(255) NOT NULL UNIQUE COMMENT 'CDN前缀URL',
    description TEXT COMMENT '描述',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否为默认前缀',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_is_default (is_default),
    INDEX idx_is_active (is_active),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CDN前缀配置表';

-- 创建文件信息表
CREATE TABLE IF NOT EXISTS file_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    original_file_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    generated_file_name VARCHAR(255) NOT NULL UNIQUE COMMENT '生成的文件名(UUID+时间戳)',
    file_extension VARCHAR(50) COMMENT '文件扩展名',
    file_size BIGINT NOT NULL COMMENT '文件大小(字节)',
    description TEXT COMMENT '文件描述',
    cdn_prefix VARCHAR(255) NOT NULL COMMENT 'CDN前缀',
    full_url VARCHAR(500) NOT NULL COMMENT '完整的CDN访问URL',
    ftp_path VARCHAR(500) NOT NULL COMMENT 'FTP服务器上的路径',
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    INDEX idx_original_file_name (original_file_name),
    INDEX idx_generated_file_name (generated_file_name),
    INDEX idx_upload_time (upload_time),
    INDEX idx_file_extension (file_extension),
    FULLTEXT idx_search (original_file_name, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';

-- 插入默认CDN前缀数据
INSERT INTO cdn_prefix (name, prefix, description, is_default, is_active) VALUES 
('默认CDN', 'https://cdn.example.cn', '系统默认CDN前缀，请根据实际情况修改', TRUE, TRUE),
('备用CDN', 'https://backup-cdn.example.cn', '备用CDN前缀', FALSE, TRUE),
('测试CDN', 'https://test-cdn.example.cn', '测试环境CDN前缀', FALSE, FALSE);

-- 插入示例文件数据（可选）
INSERT INTO file_info (original_file_name, generated_file_name, file_extension, file_size, description, cdn_prefix, full_url, ftp_path, upload_time, download_count) VALUES 
('示例文档.pdf', 'a1b2c3d4_20241201120000.pdf', '.pdf', 1024000, '这是一个示例PDF文档', 'https://cdn.example.cn', 'https://cdn.example.cn/a1b2c3d4_20241201120000.pdf', '/uploads/a1b2c3d4_20241201120000.pdf', '2024-12-01 12:00:00', 5),
('图片示例.jpg', 'e5f6g7h8_20241201130000.jpg', '.jpg', 512000, '这是一个示例图片文件', 'https://cdn.example.cn', 'https://cdn.example.cn/e5f6g7h8_20241201130000.jpg', '/uploads/e5f6g7h8_20241201130000.jpg', '2024-12-01 13:00:00', 12),
('压缩包.zip', 'i9j0k1l2_20241201140000.zip', '.zip', 2048000, '这是一个示例压缩包文件', 'https://backup-cdn.example.cn', 'https://backup-cdn.example.cn/i9j0k1l2_20241201140000.zip', '/uploads/i9j0k1l2_20241201140000.zip', '2024-12-01 14:00:00', 3);

-- 创建用户和授权（可选，根据实际需要调整）
-- CREATE USER 'ftp_cdn_user'@'localhost' IDENTIFIED BY 'ftp_cdn_password';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ftp_cdn_system.* TO 'ftp_cdn_user'@'localhost';
-- FLUSH PRIVILEGES;

-- 显示表结构
SHOW TABLES;
DESCRIBE cdn_prefix;
DESCRIBE file_info;

-- 查询初始数据
SELECT * FROM cdn_prefix;
SELECT * FROM file_info;

COMMIT;