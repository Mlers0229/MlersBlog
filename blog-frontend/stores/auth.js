import { defineStore } from 'pinia';

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
        // 实际项目中调用API进行登录
        // const response = await $fetch('/api/auth/login', {
        //   method: 'POST',
        //   body: { username, password }
        // });
        
        // 模拟登录成功响应
        await new Promise(resolve => setTimeout(resolve, 800));
        const response = {
          code: 200,
          data: {
            token: 'mock_token_' + Date.now(),
            user: {
              id: 1,
              username: username,
              nickname: username === 'admin' ? '管理员' : '普通用户',
              avatar: null,
              role: username === 'admin' ? 'ADMIN' : 'USER'
            }
          }
        };
        
        // 登录成功，存储token和用户信息
        this.setToken(response.data.token);
        this.setUser(response.data.user);
        
        return response;
      } catch (error) {
        console.error('登录失败:', error);
        this.error = error.message || '登录失败，请稍后重试';
        throw error;
      } finally {
        this.loading = false;
      }
    },
    
    // 注册
    async register({ username, password, nickname, email }) {
      this.loading = true;
      this.error = null;
      
      try {
        // 实际项目中调用API进行注册
        // const response = await $fetch('/api/auth/register', {
        //   method: 'POST',
        //   body: { username, password, nickname, email }
        // });
        
        // 模拟注册成功响应
        await new Promise(resolve => setTimeout(resolve, 800));
        const response = {
          code: 200,
          data: {
            token: 'mock_token_' + Date.now(),
            user: {
              id: Date.now(),
              username,
              nickname: nickname || username,
              email,
              avatar: null,
              role: 'USER'
            }
          }
        };
        
        // 注册成功，存储token和用户信息
        this.setToken(response.data.token);
        this.setUser(response.data.user);
        
        return response;
      } catch (error) {
        console.error('注册失败:', error);
        this.error = error.message || '注册失败，请稍后重试';
        throw error;
      } finally {
        this.loading = false;
      }
    },
    
    // 退出登录
    logout() {
      // 实际项目中可能需要调用API
      
      // 清除状态
      this.token = null;
      this.user = null;
      
      // 清除本地存储
      if (process.client) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    },
    
    // 获取当前用户信息
    async fetchCurrentUser() {
      if (!this.token) return null;
      
      this.loading = true;
      
      try {
        // 实际项目中调用API获取用户信息
        // const response = await $fetch('/api/users/me', {
        //   method: 'GET',
        //   headers: {
        //     Authorization: `Bearer ${this.token}`
        //   }
        // });
        
        // 模拟获取用户信息成功
        await new Promise(resolve => setTimeout(resolve, 400));
        const response = {
          code: 200,
          data: this.user || {
            id: 1,
            username: 'user',
            nickname: '用户',
            avatar: null,
            role: 'USER'
          }
        };
        
        this.setUser(response.data);
        return response.data;
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