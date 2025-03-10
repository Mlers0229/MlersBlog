# MlersBlog - 现代博客系统

MlersBlog是一个功能完善的现代博客系统，采用前后端分离架构设计，提供丰富的博客功能和良好的用户体验。

## 项目概述

本项目是一个完整的博客平台，包含文章发布、评论互动、用户管理等功能，适合个人博主或小型社区使用。系统分为前端和后端两个主要部分：

- **前端**：基于Vue.js和Nuxt.js构建的现代化单页应用
- **后端**：基于Spring Boot构建的RESTful API服务

## 技术栈

### 前端技术栈
- **框架**：Vue.js、Nuxt.js 3
- **状态管理**：Pinia
- **UI组件**：Element Plus、Tailwind CSS
- **编辑器**：Markdown编辑器(editor.md)
- **HTTP客户端**：Axios
- **工具库**：dayjs、date-fns、@vueuse/core

### 后端技术栈
- **框架**：Spring Boot 3.4.3
- **ORM**：Spring Data JPA
- **安全**：Spring Security
- **数据库**：MySQL (推荐)
- **构建工具**：Maven

## 功能特性

### 用户功能
- 用户注册与登录
- 个人资料管理
- 通知中心
- 用户设置

### 内容功能
- 文章创建、编辑、发布
- Markdown编辑器支持
- 文章分类与标签
- 文章归档
- 评论系统

### 管理功能
- 用户管理
- 内容审核
- 系统设置

## 项目结构

```
MlersBlog/
├── blog-frontend/           # 前端项目
│   ├── assets/              # 静态资源
│   ├── components/          # 可复用组件
│   ├── composables/         # 组合式函数
│   ├── layouts/             # 页面布局
│   ├── pages/               # 页面组件
│   ├── public/              # 公共资源
│   ├── server/              # 服务端渲染相关
│   ├── stores/              # Pinia状态管理
│   └── nuxt.config.ts       # Nuxt配置文件
│
└── blog-backend/            # 后端项目
    ├── src/                 # 源代码
    │   └── main/
    │       ├── java/        # Java代码
    │       └── resources/   # 配置文件
    └── pom.xml              # Maven配置文件
```

## 安装与运行

### 前端部分

1. 进入前端目录
```bash
cd blog-frontend
```

2. 安装依赖
```bash
npm install
```

3. 开发环境运行
```bash
npm run dev
```

4. 构建生产环境
```bash
npm run build
```

5. 预览生产构建
```bash
npm run preview
```

### 后端部分

1. 进入后端目录
```bash
cd blog-backend
```

2. 使用Maven构建项目
```bash
mvn clean install
```

3. 运行Spring Boot应用
```bash
mvn spring-boot:run
```

## 配置说明

### 前端配置
前端配置主要在`nuxt.config.ts`文件中，可以根据需要修改API基础URL等配置。

### 后端配置
后端配置在`application.properties`或`application.yml`文件中，主要包括：
- 数据库连接配置
- 服务器端口配置
- 日志配置
- 安全配置

## 部署指南

### 前端部署
1. 构建生产版本
```bash
cd blog-frontend
npm run build
```

2. 将生成的`.output`目录内容部署到Web服务器

### 后端部署
1. 构建JAR包
```bash
cd blog-backend
mvn clean package
```

2. 运行JAR包
```bash
java -jar target/blog-backend-1.0.0.jar
```

## 开发指南

### 添加新页面
1. 在`pages`目录下创建新的`.vue`文件
2. 路由将自动生成

### 添加新API
1. 在后端创建新的Controller
2. 在前端使用Axios调用新API

## 贡献指南
欢迎提交Issue和Pull Request来改进项目。

## 许可证
本项目采用MIT许可证。