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
    │       ├── java/com/mlersblog/  # Java代码
    │       │   ├── model/           # 实体类
    │       │   ├── repository/      # 数据访问层
    │       │   ├── service/         # 业务逻辑层
    │       │   ├── controller/      # 控制器层
    │       │   ├── exception/       # 异常处理
    │       │   ├── config/          # 配置类
    │       │   ├── util/            # 工具类
    │       │   ├── security/        # 安全相关
    │       │   ├── dto/             # 数据传输对象
    │       │   └── vo/              # 视图对象
    │       └── resources/           # 配置文件
    └── pom.xml                      # Maven配置文件
```

## 后端设计

### 实体模型（Model）

系统已实现以下核心实体类：

- **User**: 用户信息，包含基本个人信息和认证信息
- **UserSetting**: 用户个性化设置
- **Category**: 文章分类
- **Tag**: 文章标签
- **Post**: 博客文章，包含内容、状态等
- **Comment**: 评论信息
- **ViewLog**: 文章浏览记录

### 数据访问层（Repository）

使用Spring Data JPA实现数据访问，已实现的Repository包括：

- **UserRepository**: 用户数据访问
- **CategoryRepository**: 分类数据访问
- **TagRepository**: 标签数据访问
- **PostRepository**: 文章数据访问
- **CommentRepository**: 评论数据访问
- **ViewLogRepository**: 浏览记录数据访问

### 业务逻辑层（Service）

实现业务逻辑和事务管理，已实现的Service包括：

- **UserService**: 用户管理服务，包含注册、登录、权限验证等
- **CategoryService**: 分类管理服务
- **TagService**: 标签管理服务
- **PostService**: 文章管理服务，处理文章的CRUD、状态管理等
- **CommentService**: 评论管理服务，包含评论的发布、审核等

### 异常处理（Exception）

系统实现了统一的异常处理机制，包括：

- **ResourceNotFoundException**: 资源不存在异常
- **UserAlreadyExistsException**: 用户已存在异常
- **CategoryAlreadyExistsException**: 分类已存在异常
- **UnauthorizedException**: 未授权操作异常

## 前后端连接

### API接口设计

前后端通过RESTful API进行通信，主要接口包括：

- **认证接口**：`/api/auth/*` - 处理登录、注册、令牌刷新等
- **用户接口**：`/api/users/*` - 处理用户信息管理
- **文章接口**：`/api/posts/*` - 处理文章的CRUD操作
- **分类接口**：`/api/categories/*` - 处理分类管理
- **标签接口**：`/api/tags/*` - 处理标签管理
- **评论接口**：`/api/comments/*` - 处理评论管理
- **互动接口**：`/api/posts/{id}/like`, `/api/posts/{id}/favorite` - 处理点赞、收藏等互动
- **文件上传接口**：`/api/upload/*` - 处理图片、文件上传
- **统计接口**：`/api/stats/*` - 处理数据统计与分析
- **通知接口**：`/api/notifications/*` - 处理用户通知

### 前端API调用

前端使用Axios进行API调用，主要实现在`blog-frontend/api/`目录下：

- **index.js**: 配置Axios实例，设置拦截器
- **auth.js**: 认证相关API调用
- **post.js**: 文章相关API调用
- **categories.js**: 分类相关API调用
- **tags.js**: 标签相关API调用
- **comments.js**: 评论相关API调用
- **user.js**: 用户信息管理API调用
- **interaction.js**: 用户互动（点赞、收藏）API调用
- **upload.js**: 文件上传API调用
- **stats.js**: 统计分析API调用
- **notification.js**: 通知系统API调用

每个API模块都以两种方式导出：
1. 单独导出每个API函数，如`export const getPostById = (id) => {...}`
2. 导出一个包含所有方法的对象，如`export const posts = {...}`

这样可以根据需要使用不同的导入方式：
```javascript
// 方式1：导入特定函数
import { getPostById } from '~/api/post'

// 方式2：导入整个API模块
import { posts } from '~/api/post'
posts.getPostById(1)

// 方式3：通过索引导入
import { apiModules } from '~/api'
apiModules.posts.getPostById(1)
```

### 认证流程

1. 用户登录时，前端调用`/api/auth/login`接口
2. 后端验证用户名密码，生成JWT令牌返回
3. 前端存储令牌到localStorage
4. 后续请求通过请求拦截器自动添加令牌到请求头
5. 后端通过JWT过滤器验证令牌有效性

### 模拟数据迁移

项目初期使用模拟数据进行开发，现已完全迁移到真实后端API：

1. 移除了所有页面中的模拟数据代码
2. 使用Axios调用真实后端API
3. 确保处理API响应和错误的一致性
4. 保留了错误处理机制，以便在API请求失败时提供友好的用户体验

### 跨域处理

后端已配置CORS支持，允许前端跨域请求：

```properties
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
spring.web.cors.max-age=3600
```

前端开发环境通过Nuxt.js的代理配置处理跨域：

```javascript
// nuxt.config.ts
export default defineNuxtConfig({
  // ...
  nitro: {
    devProxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  // ...
})
```

## 开发进度

### 已完成

- [x] 实体模型设计与实现
- [x] 数据访问层实现
- [x] 业务逻辑层实现
  - [x] UserService
  - [x] CategoryService
  - [x] TagService
  - [x] PostService
  - [x] CommentService
- [x] 异常处理机制
- [x] 控制器层部分实现
  - [x] UserController
  - [x] AuthController
  - [x] CategoryController
- [x] 安全认证与授权
  - [x] JWT令牌生成与验证
  - [x] Spring Security配置
  - [x] 基于角色的权限控制
- [x] 前后端连接
  - [x] API调用实现
  - [x] 认证流程实现
  - [x] 跨域处理
  - [x] 模拟数据替换为真实API调用
  - [x] 完成所有API接口对接

### 进行中

- [ ] 控制器层其他部分实现
  - [x] PostController (已完成)
  - [x] TagController (已完成)
  - [x] CommentController (已完成)
  - [ ] StatsController
  - [ ] NotificationController
  - [ ] UploadController
- [ ] 前端页面开发
  - [x] 主页
  - [x] 文章详情页
  - [x] 用户认证页面
  - [ ] 文章管理页面
  - [ ] 用户管理页面
  - [ ] 统计分析页面
  - [ ] 通知管理页面

### 待实现

- [ ] 文件上传服务
- [ ] 通知系统
- [ ] 搜索功能
- [ ] 统计分析
- [ ] 缓存机制

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
1. 在后端创建新的控制器方法
2. 在前端相应的API文件中添加调用方法

## 常见问题与解决方案

### 跨域问题
如果遇到跨域问题，请检查：
1. 后端CORS配置是否正确
2. 前端请求URL是否正确
3. 开发环境是否配置了代理

### 认证问题
如果遇到认证问题，请检查：
1. JWT令牌是否正确生成和存储
2. 请求头中是否正确添加了Authorization
3. 后端JWT验证是否正常工作

## 项目维护与更新

本项目将持续更新和维护，计划添加的功能包括：
1. 更丰富的文章编辑功能
2. 更完善的用户权限管理
3. 性能优化和缓存机制
4. 移动端适配优化
5. 更多第三方集成