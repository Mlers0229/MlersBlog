// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  // 应用模式
  ssr: true, // 启用服务端渲染

  // 开发工具
  devtools: { enabled: true },

  // 兼容性日期设置
  compatibilityDate: '2025-03-04',

  // 模块
  modules: [
    '@pinia/nuxt',
    '@nuxtjs/tailwindcss',
    // '@element-plus/nuxt', // 已移除，因为未安装此模块
  ],

  // 自动导入组件
  components: [
    { path: '~/components', pathPrefix: false },
  ],

  // 插件
  plugins: [],

  // 运行时配置
  runtimeConfig: {
    // 服务器端密钥
    apiSecret: process.env.API_SECRET || 'default_secret',
    
    // 也会暴露在客户端的公共配置
    public: {
      apiBase: process.env.API_BASE || 'http://localhost:8080/api'
    }
  },

  // 应用配置
  app: {
    head: {
      title: '博客系统',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: '一个现代化的博客系统' }
      ],
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
      ]
    }
  },

  // 代理配置，用于开发环境
  nitro: {
    devProxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },

  // 构建配置
  build: {
    // transpile: ['element-plus/es'] // 已移除，因为不再使用element-plus
  },

  // 配置全局CSS
  css: [
    '@/assets/css/main.css', // 恢复对CSS文件的引用，因为我们已经创建了该文件
  ],

  // 实验性功能
  experimental: {
    reactivityTransform: false,
    inlineSSRStyles: true,
  },

  // 渲染配置
  render: {
    csp: false // 内容安全策略
  },

  // 错误处理
  hooks: {
    'app:error': (error) => {
      console.error('应用错误:', error)
    }
  },

  // 自定义路由配置
  router: {
    options: {
      sensitive: true,  // 区分大小写的路由
      strict: false    // 非严格匹配尾部斜杠
    }
  }
})