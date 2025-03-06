# 博客系统前端

这是一个基于Vue.js和Nuxt的博客系统前端项目。

## 安装依赖

```bash
npm install
```

## 开发环境运行

```bash
npm run dev
```

## 构建生产环境

```bash
npm run build
```

## 项目结构

- `pages/`: 所有页面组件
- `components/`: 可复用的Vue组件
- `layouts/`: 页面布局模板
- `stores/`: Pinia状态管理
- `public/`: 静态资源文件

## 图片资源

项目依赖以下默认图片，请确保这些文件存在：

- `public/images/default-avatar.png`: 默认用户头像
- `public/images/default-cover.jpg`: 默认文章封面图

您需要手动创建`public/images`目录并放入这些文件：

```bash
mkdir -p public/images
```

然后将适当的图片文件复制到该目录或在线下载合适的图片。

## 路由结构

- `/`: 首页
- `/post`: 文章列表页
- `/post/[id]`: 文章详情页
- `/post/create`: 创建/编辑文章页
- `/archives`: 文章归档页
- `/tags`: 标签云页
- `/profile`: 用户个人中心
- `/notifications`: 通知中心
- `/login`: 登录页
- `/register`: 注册页

## API接入

项目已配置API请求工具，但需要后端服务配合使用。当前使用模拟数据进行开发。

