# 🚀 FTP-CDN系统

一个基于Spring Boot的现代化文件存储与CDN分发系统，支持文件上传、存储、CDN加速分发和下载统计功能。

## 📋 项目简介

FTP-CDN系统是一个企业级的文件存储与分发解决方案，通过集成FTP服务器和CDN技术，为用户提供稳定、高效的文件存储和全球分发服务。系统采用前后端分离架构，支持多种文件类型，具备完善的权限管理和统计分析功能。

## ✨ 功能特性

### 🎯 核心功能
- **文件上传管理**：支持单文件和多文件上传，自动生成唯一文件名
- **CDN分发**：支持自定义CDN前缀，灵活配置多个CDN节点
- **文件下载**：直接下载文件，自动统计下载次数
- **文件预览**：支持图片、文档等多种格式的在线预览
- **统计分析**：实时统计文件上传量、下载量等关键指标

### 🔧 管理功能
- **CDN配置管理**：动态配置和管理CDN节点
- **文件信息管理**：查看和管理所有上传的文件
- **系统监控**：实时监控文件存储和分发状态

### 🛡️ 安全特性
- **文件类型校验**：严格检查上传文件类型，防止恶意文件
- **文件大小限制**：可配置文件上传大小限制
- **唯一文件名**：自动生成UUID文件名，防止文件冲突
- **访问日志**：完整的操作日志记录

## 🏗️ 技术架构

### 后端技术栈
- **框架**：Spring Boot 2.7.18
- **模板引擎**：Thymeleaf
- **数据库**：MySQL 8.0
- **ORM框架**：MyBatis
- **连接池**：HikariCP
- **FTP客户端**：Apache Commons Net
- **文件上传**：Spring Multipart
- **日志框架**：SLF4J + Logback

### 前端技术栈
- **模板引擎**：Thymeleaf
- **UI框架**：Bootstrap 5
- **JavaScript**：原生JavaScript + jQuery
- **图标库**：Font Awesome
- **弹窗组件**：SweetAlert2

### 系统架构
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   用户浏览器     │────│   Spring Boot   │────│     MySQL       │
│   (前端页面)     │    │   应用服务器     │    │   数据库        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                │
                       ┌─────────────────┐
                       │   FTP服务器     │
                       │   (文件存储)    │
                       └─────────────────┘
                                │
                                │
                       ┌─────────────────┐
                       │   CDN网络       │
                       │   (全球分发)    │
                       └─────────────────┘
```

## 📁 项目结构

```
ftp-cdn-system/
├── src/main/java/com/nyx/ftpcdn/
│   ├── controller/          # 控制器层
│   │   ├── FileController.java      # 文件管理控制器
│   │   ├── CdnPrefixController.java   # CDN配置控制器
│   │   └── IndexController.java     # 首页控制器
│   ├── service/            # 业务逻辑层
│   │   ├── FileService.java         # 文件服务
│   │   ├── FtpService.java         # FTP服务
│   │   └── CdnPrefixService.java   # CDN配置服务
│   ├── mapper/             # 数据访问层
│   │   ├── FileInfoMapper.java     # 文件信息映射器
│   │   └── CdnPrefixMapper.java    # CDN配置映射器
│   ├── model/              # 数据模型
│   │   ├── FileInfo.java          # 文件信息实体
│   │   └── CdnPrefix.java         # CDN配置实体
│   └── FtpCdnSystemApplication.java  # 启动类
├── src/main/resources/
│   ├── templates/          # 页面模板
│   │   ├── index.html             # 首页
│   │   ├── file-detail.html       # 文件详情页
│   │   ├── upload.html            # 上传页面
│   │   └── cdn-config.html        # CDN配置页面
│   ├── static/             # 静态资源
│   │   ├── css/                   # 样式文件
│   │   ├── js/                    # JavaScript文件
│   │   └── images/                # 图片资源
│   ├── application.yml     # 应用配置文件
│   └── mapper/             # MyBatis映射文件
│       ├── FileInfoMapper.xml      # 文件信息SQL映射
│       └── CdnPrefixMapper.xml     # CDN配置SQL映射
├── database.sql           # 数据库初始化脚本
└── pom.xml               # Maven配置文件
```

## 🚀 快速开始

### 环境要求
- Java 8 或更高版本
- Maven 3.6+
- MySQL 8.0+
- FTP服务器（支持FTP/SFTP协议）

### 1. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE ftp_cdn_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 导入数据表
```bash
mysql -u root -p ftp_cdn_system < database.sql
```

### 2. 配置文件设置

编辑 `src/main/resources/application.yml`：

```yaml
# 服务器配置
server:
  port: 8765

# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ftp_cdn_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

# FTP配置
ftp:
  host: your-ftp-server.com
  port: 21
  username: your_ftp_username
  password: your_ftp_password
  base-path: /uploads

# 文件上传配置
spring:
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
```

### 3. 编译和运行

#### 编译项目
```bash
mvn clean compile
```

#### 运行项目
```bash
mvn spring-boot:run
```

#### 访问系统
打开浏览器访问：http://localhost:8765

## 📖 使用指南

### 文件上传
1. 访问首页 http://localhost:8765
2. 点击"选择文件"按钮选择要上传的文件
3. 填写文件描述（可选）
4. 点击"开始上传"按钮
5. 上传完成后，系统会显示文件的CDN访问链接

### CDN配置管理
1. 访问CDN配置页面：http://localhost:8765/cdn-config
2. 点击"添加CDN前缀"按钮
3. 填写CDN信息：
   - 名称：CDN节点名称
   - 前缀：CDN访问域名
   - 描述：节点描述信息
4. 点击"保存"完成配置

### 文件管理
1. 在首页查看所有已上传的文件列表
2. 点击文件名查看详细信息
3. 点击"下载"按钮下载文件
4. 查看下载次数统计

## 🔍 API接口文档

### 文件管理接口
- `GET /` - 首页，显示文件列表
- `GET /file/{id}` - 获取文件详细信息
- `POST /file/upload` - 上传文件
- `GET /file/{id}/download` - 下载文件
- `POST /file/{id}/download` - 增加下载次数（已废弃）

### CDN配置接口
- `GET /cdn-config` - CDN配置页面
- `GET /api/cdn-prefixes` - 获取所有CDN配置
- `GET /api/cdn-prefixes/active` - 获取活动的CDN配置
- `POST /api/cdn-prefixes` - 添加CDN配置
- `PUT /api/cdn-prefixes/{id}` - 更新CDN配置
- `DELETE /api/cdn-prefixes/{id}` - 删除CDN配置
- `PUT /api/cdn-prefixes/{id}/default` - 设为默认CDN

## 🗄️ 数据库设计

### 文件信息表 (file_info)
| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | BIGINT | 主键ID |
| original_file_name | VARCHAR(255) | 原始文件名 |
| generated_file_name | VARCHAR(255) | 生成的唯一文件名 |
| file_extension | VARCHAR(20) | 文件扩展名 |
| file_size | BIGINT | 文件大小（字节） |
| description | TEXT | 文件描述 |
| cdn_prefix | VARCHAR(255) | CDN前缀 |
| full_url | VARCHAR(500) | 完整访问URL |
| ftp_path | VARCHAR(500) | FTP存储路径 |
| upload_time | DATETIME | 上传时间 |
| download_count | INT | 下载次数 |

### CDN配置表 (cdn_prefix)
| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | BIGINT | 主键ID |
| name | VARCHAR(100) | CDN名称 |
| prefix | VARCHAR(255) | CDN前缀 |
| description | TEXT | 描述信息 |
| is_active | BOOLEAN | 是否激活 |
| is_default | BOOLEAN | 是否默认 |
| create_time | DATETIME | 创建时间 |

## 🛠️ 开发指南

### 代码规范
- 遵循阿里巴巴Java开发规范
- 使用Lombok简化POJO类
- 统一异常处理
- RESTful API设计规范

### 日志配置
系统使用SLF4J + Logback日志框架，日志文件位于：
- `logs/ftp-cdn-system.log` - 主日志文件
- `logs/ftp-cdn-system.log.YYYY-MM-DD.0.gz` - 历史日志

### 调试技巧
1. 开启SQL日志：在application.yml中设置`logging.level.com.nyx.ftpcdn.mapper=DEBUG`
2. 查看FTP操作日志：检查控制台输出的FTP操作信息
3. 浏览器调试：使用F12开发者工具查看网络请求

## 🐛 常见问题

### Q1: 文件上传失败
**问题描述**：上传文件时出现错误
**解决方案**：
1. 检查FTP服务器连接是否正常
2. 确认FTP用户有足够权限
3. 检查文件大小是否超过限制
4. 查看日志文件获取详细错误信息

### Q2: CDN链接无法访问
**问题描述**：生成的CDN链接无法打开
**解决方案**：
1. 检查CDN前缀配置是否正确
2. 确认CDN服务已启动
3. 检查文件是否已成功上传到FTP服务器
4. 验证CDN域名解析是否正常

### Q3: 数据库连接失败
**问题描述**：启动时报数据库连接错误
**解决方案**：
1. 检查MySQL服务是否启动
2. 确认数据库连接配置正确
3. 检查数据库用户权限
4. 验证数据库字符集设置

## 📄 许可证

本项目采用MIT许可证，详见 [LICENSE](LICENSE) 文件。

## 🤝 贡献指南

欢迎提交Issue和Pull Request来改进项目！

### 提交规范
- 使用清晰的提交信息
- 添加必要的单元测试
- 更新相关文档
- 遵循代码规范

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 [GitHub Issue](https://github.com/your-username/ftp-cdn-system/issues)
- 发送邮件至：your-email@example.com

---

**⭐ 如果这个项目对您有帮助，请给个Star支持一下！**