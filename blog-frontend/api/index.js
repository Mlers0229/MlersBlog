// blog-frontend/api/index.js (更新)
import axios from 'axios'
import * as auth from './auth'
import * as posts from './post'
import * as categories from './categories'
import * as tags from './tags'
import * as comments from './comments'
import * as user from './user'
import * as interaction from './interaction'
import * as upload from './upload'
import * as stats from './stats'
import * as notification from './notification'

// 创建API实例
// 使用环境变量或配置文件中的API基础URL
let API_BASE_URL = 'http://localhost:8080';

// 如果在浏览器环境中
if (process.client) {
  try {
    // 尝试从localStorage获取API基础URL(如果有自定义设置)
    const storedBaseUrl = localStorage.getItem('api_base_url');
    if (storedBaseUrl) {
      API_BASE_URL = storedBaseUrl;
    }
  } catch (error) {
    console.error('无法从localStorage获取API基础URL', error);
  }
}

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 响应拦截器 - 处理错误
api.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API错误:', error.response?.data?.message || '请求失败')
    return Promise.reject(error)
  }
)

// 请求拦截器 - 添加JWT令牌
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 导出单独的API模块
export {
  api as default,
  auth,
  posts,
  categories,
  tags,
  comments,
  user,
  interaction,
  upload,
  stats,
  notification
}

// 导出所有API模块的集合
export const apiModules = {
  auth,
  posts,
  categories,
  tags,
  comments,
  user,
  interaction,
  upload,
  stats,
  notification
}