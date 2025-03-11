// blog-frontend/api/auth.js
import api from './index'

// 设置请求拦截器添加认证令牌
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

/**
 * 登录认证API
 * @param {Object} credentials - 登录凭证
 * @param {string} credentials.username - 用户名
 * @param {string} credentials.password - 密码
 * @returns {Promise<Object>} 登录结果，包含token和用户信息
 */
export const login = async (credentials) => {
  const response = await api.post('/api/auth/login', credentials);
  // 保存令牌到本地存储
  if (response.token) {
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(response.user));
  }
  return response;
};

/**
 * 注册API
 * @param {Object} userData - 用户数据
 * @param {string} userData.username - 用户名
 * @param {string} userData.password - 密码
 * @param {string} userData.nickname - 昵称
 * @param {string} userData.email - 邮箱
 * @returns {Promise<Object>} 注册结果
 */
export const register = (userData) => {
  return api.post('/api/users/register', userData);
};

/**
 * 登出
 */
export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};

/**
 * 获取当前用户信息
 * @returns {Promise<Object>} 用户信息
 */
export const getCurrentUser = () => {
  return api.get('/api/users/me');
};

/**
 * 检查是否已登录
 * @returns {boolean} 是否已登录
 */
export const isAuthenticated = () => {
  return !!localStorage.getItem('token');
};

/**
 * 获取存储的用户信息
 * @returns {Object|null} 用户信息
 */
export const getStoredUser = () => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    try {
      return JSON.parse(userStr);
    } catch (e) {
      return null;
    }
  }
  return null;
};

export const auth = {
  login,
  register,
  logout,
  getCurrentUser,
  isAuthenticated,
  getStoredUser
};