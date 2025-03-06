<template>
  <div class="register-page">
    <div class="register-container">
      <h1 class="page-title">注册账号</h1>
      
      <form @submit.prevent="handleRegister" class="register-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username" 
            v-model="form.username" 
            placeholder="请输入用户名（5-20个字符）"
            required
            :disabled="authStore.loading"
          />
          <span v-if="validationErrors.username" class="validation-error">
            {{ validationErrors.username }}
          </span>
        </div>
        
        <div class="form-group">
          <label for="email">邮箱</label>
          <input 
            type="email" 
            id="email" 
            v-model="form.email" 
            placeholder="请输入邮箱"
            required
            :disabled="authStore.loading"
          />
          <span v-if="validationErrors.email" class="validation-error">
            {{ validationErrors.email }}
          </span>
        </div>
        
        <div class="form-group">
          <label for="nickname">昵称</label>
          <input 
            type="text" 
            id="nickname" 
            v-model="form.nickname" 
            placeholder="请输入昵称（选填）"
            :disabled="authStore.loading"
          />
        </div>
        
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="form.password" 
            placeholder="请输入密码（至少8个字符）"
            required
            :disabled="authStore.loading"
          />
          <span v-if="validationErrors.password" class="validation-error">
            {{ validationErrors.password }}
          </span>
        </div>
        
        <div class="form-group">
          <label for="confirm-password">确认密码</label>
          <input 
            type="password" 
            id="confirm-password" 
            v-model="form.confirmPassword" 
            placeholder="请再次输入密码"
            required
            :disabled="authStore.loading"
          />
          <span v-if="validationErrors.confirmPassword" class="validation-error">
            {{ validationErrors.confirmPassword }}
          </span>
        </div>
        
        <div class="form-agreement">
          <label class="agreement-label">
            <input type="checkbox" v-model="form.agreement" :disabled="authStore.loading" />
            <span>我已阅读并同意<NuxtLink to="/terms">用户协议</NuxtLink>和<NuxtLink to="/privacy">隐私政策</NuxtLink></span>
          </label>
          <span v-if="validationErrors.agreement" class="validation-error">
            {{ validationErrors.agreement }}
          </span>
        </div>
        
        <div v-if="error" class="error-message">
          {{ error }}
        </div>
        
        <button 
          type="submit" 
          class="register-button" 
          :disabled="authStore.loading || !form.agreement"
        >
          <span v-if="!authStore.loading">注册</span>
          <span v-else class="loading-spinner"></span>
        </button>
        
        <div class="login-link">
          已有账号？<NuxtLink to="/login">立即登录</NuxtLink>
        </div>
      </form>
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
  email: '',
  nickname: '',
  password: '',
  confirmPassword: '',
  agreement: false
});

// 错误信息
const error = ref('');
const validationErrors = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  agreement: ''
});

// 验证表单
const validateForm = () => {
  let isValid = true;
  
  // 清除之前的验证错误
  Object.keys(validationErrors).forEach(key => {
    validationErrors[key] = '';
  });
  
  // 验证用户名
  if (!form.username) {
    validationErrors.username = '请输入用户名';
    isValid = false;
  } else if (form.username.length < 5 || form.username.length > 20) {
    validationErrors.username = '用户名长度应为5-20个字符';
    isValid = false;
  } else if (!/^[a-zA-Z0-9_]+$/.test(form.username)) {
    validationErrors.username = '用户名只能包含字母、数字和下划线';
    isValid = false;
  }
  
  // 验证邮箱
  if (!form.email) {
    validationErrors.email = '请输入邮箱';
    isValid = false;
  } else if (!/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(form.email)) {
    validationErrors.email = '请输入有效的邮箱地址';
    isValid = false;
  }
  
  // 验证密码
  if (!form.password) {
    validationErrors.password = '请输入密码';
    isValid = false;
  } else if (form.password.length < 8) {
    validationErrors.password = '密码长度至少为8个字符';
    isValid = false;
  }
  
  // 验证确认密码
  if (!form.confirmPassword) {
    validationErrors.confirmPassword = '请确认密码';
    isValid = false;
  } else if (form.password !== form.confirmPassword) {
    validationErrors.confirmPassword = '两次输入的密码不一致';
    isValid = false;
  }
  
  // 验证协议同意
  if (!form.agreement) {
    validationErrors.agreement = '请阅读并同意用户协议和隐私政策';
    isValid = false;
  }
  
  return isValid;
};

// 注册处理
const handleRegister = async () => {
  if (!validateForm()) {
    return;
  }
  
  error.value = '';
  
  try {
    await authStore.register({
      username: form.username,
      email: form.email,
      nickname: form.nickname || form.username,
      password: form.password
    });
    
    // 注册成功后重定向到首页
    router.push('/');
  } catch (err) {
    error.value = err.message || '注册失败，请稍后重试';
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
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 180px);
  padding: 20px;
  background-color: var(--bg-color-secondary);
}

.register-container {
  width: 100%;
  max-width: 500px;
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

.register-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
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

.validation-error {
  color: var(--error-color);
  font-size: 0.85rem;
  margin-top: 2px;
}

.form-agreement {
  margin-top: 5px;
}

.agreement-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 0.9rem;
}

.agreement-label a {
  color: var(--primary-color);
  text-decoration: none;
}

.agreement-label a:hover {
  text-decoration: underline;
}

.error-message {
  color: var(--error-color);
  font-size: 0.9rem;
  padding: 8px 0;
}

.register-button {
  padding: 12px;
  margin-top: 10px;
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

.register-button:hover {
  background-color: var(--primary-color-dark);
}

.register-button:disabled {
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

.login-link {
  text-align: center;
  margin-top: 15px;
  font-size: 0.9rem;
}

.login-link a {
  color: var(--primary-color);
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style> 