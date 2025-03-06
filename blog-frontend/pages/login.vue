<template>
  <div class="login-page">
    <div class="login-container">
      <h1 class="page-title">登录</h1>
      
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username" 
            v-model="form.username" 
            placeholder="请输入用户名"
            required
            :disabled="authStore.loading"
          />
        </div>
        
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="form.password" 
            placeholder="请输入密码"
            required
            :disabled="authStore.loading"
          />
        </div>
        
        <div class="form-options">
          <label class="remember-me">
            <input type="checkbox" v-model="form.remember" :disabled="authStore.loading" />
            <span>记住我</span>
          </label>
          <NuxtLink to="/reset-password" class="forget-password">忘记密码？</NuxtLink>
        </div>
        
        <div v-if="error" class="error-message">
          {{ error }}
        </div>
        
        <button 
          type="submit" 
          class="login-button" 
          :disabled="authStore.loading"
        >
          <span v-if="!authStore.loading">登录</span>
          <span v-else class="loading-spinner"></span>
        </button>
        
        <div class="register-link">
          还没有账号？<NuxtLink to="/register">立即注册</NuxtLink>
        </div>
      </form>
      
      <div class="social-login">
        <div class="divider">
          <span>或使用第三方账号登录</span>
        </div>
        <div class="social-buttons">
          <button class="social-button github" aria-label="使用GitHub登录">
            <i class="fab fa-github"></i>
          </button>
          <button class="social-button google" aria-label="使用Google登录">
            <i class="fab fa-google"></i>
          </button>
          <button class="social-button weibo" aria-label="使用微博登录">
            <i class="fab fa-weibo"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '~/stores/auth';

const router = useRouter();
const authStore = useAuthStore();

// 表单数据
const form = reactive({
  username: '',
  password: '',
  remember: false
});

// 错误信息
const error = ref('');

// 登录处理
const handleLogin = async () => {
  if (!form.username || !form.password) {
    error.value = '请输入用户名和密码';
    return;
  }
  
  error.value = '';
  
  try {
    await authStore.login({
      username: form.username,
      password: form.password
    });
    
    // 登录成功后重定向到首页
    router.push('/');
  } catch (err) {
    error.value = err.message || '登录失败，请稍后重试';
  }
};

// 如果用户已登录，重定向到首页
onMounted(() => {
  authStore.initializeAuth();
  
  if (authStore.isLoggedIn) {
    router.push('/');
  }
});
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 180px);
  padding: 20px;
  background-color: var(--bg-color-secondary);
}

.login-container {
  width: 100%;
  max-width: 420px;
  padding: 30px;
  background-color: var(--bg-color);
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.page-title {
  text-align: center;
  margin-bottom: 25px;
  font-size: 1.8rem;
  color: var(--text-color-primary);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 0.95rem;
  color: var(--text-color-primary);
}

.form-group input {
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  background-color: var(--bg-color);
  color: var(--text-color-primary);
  font-size: 1rem;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.form-group input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(var(--primary-color-rgb), 0.2);
  outline: none;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.forget-password {
  color: var(--primary-color);
  text-decoration: none;
}

.forget-password:hover {
  text-decoration: underline;
}

.error-message {
  color: var(--error-color);
  font-size: 0.9rem;
  padding: 8px 0;
}

.login-button {
  padding: 12px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
  position: relative;
}

.login-button:hover {
  background-color: var(--primary-color-dark);
}

.login-button:disabled {
  background-color: var(--disabled-color);
  cursor: not-allowed;
}

.loading-spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.register-link {
  text-align: center;
  margin-top: 15px;
  font-size: 0.9rem;
}

.register-link a {
  color: var(--primary-color);
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

.social-login {
  margin-top: 30px;
}

.divider {
  position: relative;
  text-align: center;
  margin-bottom: 20px;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background-color: var(--border-color);
}

.divider span {
  position: relative;
  display: inline-block;
  padding: 0 15px;
  background-color: var(--bg-color);
  font-size: 0.9rem;
  color: var(--text-color-secondary);
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.social-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 1px solid var(--border-color);
  background-color: transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.social-button:hover {
  background-color: rgba(var(--primary-color-rgb), 0.1);
  transform: translateY(-3px);
}

.social-button i {
  font-size: 1.2rem;
}

.github i {
  color: #24292e;
}

.google i {
  color: #DB4437;
}

.weibo i {
  color: #E6162D;
}
</style> 