import { defineStore } from 'pinia';
import { auth as authApi } from '~/api/auth';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null,
    user: null,
    loading: false,
    error: null
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    userInfo: (state) => state.user || {},
    isAdmin: (state) => state.user?.role === 'ADMIN'
  },
  
  actions: {
    // 获取token
    getToken() {
      return this.token;
    },
    
    // 登录
    async login({ username, password }) {
      this.loading = true;
      this.error = null;
      
      try {
        // 调用实际API进行登录
        const response = await authApi.login({ username, password });
        
        // 登录成功，存储token和用户信息
        this.setToken(response.token);
        this.setUser(response.user);
        
        return response;
      } catch (error) {
        console.error('登录失败:', error);
        this.error = error.response?.data?.message || '登录失败，请稍后重试';
        throw new Error(this.error);
      } finally {
        this.loading = false;
      }
    },
    
    // 注册
    async register({ username, password, nickname, email }) {
      this.loading = true;
      this.error = null;
      
      try {
        // 调用实际API进行注册
        const response = await authApi.register({ 
          username, 
          password, 
          nickname, 
          email 
        });
        
        // 注册成功后自动登录
        await this.login({ username, password });
        
        return response;
      } catch (error) {
        console.error('注册失败:', error);
        this.error = error.response?.data?.message || '注册失败，请稍后重试';
        throw new Error(this.error);
      } finally {
        this.loading = false;
      }
    },
    
    // 退出登录
    logout() {
      // 清除状态
      this.token = null;
      this.user = null;
      
      // 清除本地存储
      if (process.client) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
      
      // 调用API的登出方法
      authApi.logout();
    },
    
    // 获取当前用户信息
    async fetchCurrentUser() {
      if (!this.token) return null;
      
      this.loading = true;
      
      try {
        // 调用API获取用户信息
        const user = await authApi.getCurrentUser();
        this.setUser(user);
        return user;
      } catch (error) {
        console.error('获取用户信息失败:', error);
        // 如果是401错误，清除token和用户信息
        if (error.response?.status === 401) {
          this.logout();
        }
        return null;
      } finally {
        this.loading = false;
      }
    },
    
    // 设置Token
    setToken(token) {
      this.token = token;
      if (process.client && token) {
        localStorage.setItem('token', token);
      }
    },
    
    // 设置用户信息
    setUser(user) {
      this.user = user;
      if (process.client && user) {
        localStorage.setItem('user', JSON.stringify(user));
      }
    },
    
    // 初始化状态（从本地存储中恢复）
    initializeAuth() {
      if (process.client) {
        const token = localStorage.getItem('token');
        const userStr = localStorage.getItem('user');
        
        if (token) {
          this.token = token;
          
          try {
            if (userStr) {
              this.user = JSON.parse(userStr);
            } else {
              // 如果有token但没有用户信息，尝试获取
              this.fetchCurrentUser();
            }
          } catch (e) {
            console.error('解析用户信息失败:', e);
            this.user = null;
          }
        }
      }
    }
  }
}); 