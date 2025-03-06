<template>
  <div class="container mx-auto px-4 py-8 max-w-5xl">
    <h1 class="text-3xl font-bold mb-8 text-gray-800 dark:text-gray-100">用户设置</h1>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="space-y-6">
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow p-6 animate-pulse">
        <div class="h-6 bg-gray-200 dark:bg-gray-700 rounded w-1/4 mb-4"></div>
        <div class="h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4 mb-6"></div>
        <div class="h-10 bg-gray-200 dark:bg-gray-700 rounded mb-4"></div>
        <div class="h-10 bg-gray-200 dark:bg-gray-700 rounded mb-4"></div>
        <div class="h-10 bg-gray-200 dark:bg-gray-700 rounded w-1/3 mt-6"></div>
      </div>
      
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow p-6 animate-pulse">
        <div class="h-6 bg-gray-200 dark:bg-gray-700 rounded w-1/4 mb-4"></div>
        <div class="h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4 mb-6"></div>
        <div class="h-10 bg-gray-200 dark:bg-gray-700 rounded mb-4"></div>
        <div class="h-10 bg-gray-200 dark:bg-gray-700 rounded mb-4"></div>
      </div>
    </div>
    
    <!-- 设置内容 -->
    <div v-else class="space-y-6">
      <!-- 账户安全设置 -->
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
        <h2 class="text-xl font-semibold mb-4 text-gray-800 dark:text-gray-100">账户安全设置</h2>
        <p class="text-gray-600 dark:text-gray-400 mb-6">管理您的密码和账户安全选项</p>
        
        <form @submit.prevent="updatePassword" class="space-y-4">
          <div>
            <label for="currentPassword" class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">当前密码</label>
            <input 
              type="password" 
              id="currentPassword" 
              v-model="passwordForm.currentPassword"
              class="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
              required
            />
          </div>
          
          <div>
            <label for="newPassword" class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">新密码</label>
            <input 
              type="password" 
              id="newPassword" 
              v-model="passwordForm.newPassword"
              class="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
              required
            />
            <p class="text-xs text-gray-500 mt-1">密码至少包含8个字符，并包含字母、数字和特殊字符</p>
          </div>
          
          <div>
            <label for="confirmPassword" class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">确认新密码</label>
            <input 
              type="password" 
              id="confirmPassword" 
              v-model="passwordForm.confirmPassword"
              class="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:text-white"
              required
            />
          </div>
          
          <div v-if="passwordError" class="text-red-500 text-sm">{{ passwordError }}</div>
          <div v-if="passwordSuccess" class="text-green-500 text-sm">{{ passwordSuccess }}</div>
          
          <button 
            type="submit" 
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors disabled:opacity-50"
            :disabled="passwordLoading"
          >
            <span v-if="passwordLoading">更新中...</span>
            <span v-else>更新密码</span>
          </button>
        </form>
      </div>
      
      <!-- 通知设置 -->
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
        <h2 class="text-xl font-semibold mb-4 text-gray-800 dark:text-gray-100">通知设置</h2>
        <p class="text-gray-600 dark:text-gray-400 mb-6">管理您接收的各种通知</p>
        
        <div class="space-y-4">
          <div class="flex items-center justify-between py-2">
            <div>
              <h3 class="font-medium text-gray-800 dark:text-gray-200">文章评论</h3>
              <p class="text-sm text-gray-600 dark:text-gray-400">当有人评论您的文章时通知您</p>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input type="checkbox" v-model="notificationSettings.comments" class="sr-only peer">
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
            </label>
          </div>
          
          <div class="flex items-center justify-between py-2">
            <div>
              <h3 class="font-medium text-gray-800 dark:text-gray-200">点赞与收藏</h3>
              <p class="text-sm text-gray-600 dark:text-gray-400">当有人点赞或收藏您的文章时通知您</p>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input type="checkbox" v-model="notificationSettings.likes" class="sr-only peer">
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
            </label>
          </div>
          
          <div class="flex items-center justify-between py-2">
            <div>
              <h3 class="font-medium text-gray-800 dark:text-gray-200">系统通知</h3>
              <p class="text-sm text-gray-600 dark:text-gray-400">接收系统更新、维护和新功能的通知</p>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input type="checkbox" v-model="notificationSettings.system" class="sr-only peer">
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
            </label>
          </div>
          
          <div class="flex items-center justify-between py-2">
            <div>
              <h3 class="font-medium text-gray-800 dark:text-gray-200">邮件通知</h3>
              <p class="text-sm text-gray-600 dark:text-gray-400">将通知发送到您的邮箱</p>
            </div>
            <label class="relative inline-flex items-center cursor-pointer">
              <input type="checkbox" v-model="notificationSettings.email" class="sr-only peer">
              <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
            </label>
          </div>
          
          <div class="mt-6">
            <button 
              @click="saveNotificationSettings" 
              class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors disabled:opacity-50"
              :disabled="notificationLoading"
            >
              <span v-if="notificationLoading">保存中...</span>
              <span v-else>保存通知设置</span>
            </button>
          </div>
        </div>
      </div>
      
      <!-- 个性化设置 -->
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
        <h2 class="text-xl font-semibold mb-4 text-gray-800 dark:text-gray-100">个性化设置</h2>
        <p class="text-gray-600 dark:text-gray-400 mb-6">自定义您的博客体验</p>
        
        <div class="space-y-6">
          <div>
            <h3 class="font-medium text-gray-800 dark:text-gray-200 mb-3">主题偏好</h3>
            <div class="flex space-x-4">
              <label class="block cursor-pointer">
                <input 
                  type="radio" 
                  name="theme" 
                  value="light" 
                  v-model="preferences.theme"
                  class="sr-only" 
                />
                <div class="flex flex-col items-center">
                  <div class="w-16 h-16 border-2 rounded-lg flex items-center justify-center" :class="preferences.theme === 'light' ? 'border-blue-500 bg-blue-50 dark:bg-blue-900' : 'border-gray-300 dark:border-gray-600'">
                    <span class="text-2xl">☀️</span>
                  </div>
                  <span class="mt-1 text-sm text-gray-700 dark:text-gray-300">亮色</span>
                </div>
              </label>
              
              <label class="block cursor-pointer">
                <input 
                  type="radio" 
                  name="theme" 
                  value="dark" 
                  v-model="preferences.theme"
                  class="sr-only" 
                />
                <div class="flex flex-col items-center">
                  <div class="w-16 h-16 border-2 rounded-lg flex items-center justify-center" :class="preferences.theme === 'dark' ? 'border-blue-500 bg-blue-50 dark:bg-blue-900' : 'border-gray-300 dark:border-gray-600'">
                    <span class="text-2xl">🌙</span>
                  </div>
                  <span class="mt-1 text-sm text-gray-700 dark:text-gray-300">暗色</span>
                </div>
              </label>
              
              <label class="block cursor-pointer">
                <input 
                  type="radio" 
                  name="theme" 
                  value="system" 
                  v-model="preferences.theme"
                  class="sr-only" 
                />
                <div class="flex flex-col items-center">
                  <div class="w-16 h-16 border-2 rounded-lg flex items-center justify-center" :class="preferences.theme === 'system' ? 'border-blue-500 bg-blue-50 dark:bg-blue-900' : 'border-gray-300 dark:border-gray-600'">
                    <span class="text-2xl">🖥️</span>
                  </div>
                  <span class="mt-1 text-sm text-gray-700 dark:text-gray-300">跟随系统</span>
                </div>
              </label>
            </div>
          </div>
          
          <div>
            <h3 class="font-medium text-gray-800 dark:text-gray-200 mb-3">字体大小</h3>
            <div class="flex items-center space-x-2">
              <span class="text-sm text-gray-700 dark:text-gray-300">A</span>
              <input 
                type="range" 
                min="80" 
                max="120" 
                step="10" 
                v-model="preferences.fontSize"
                class="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer dark:bg-gray-700"
              />
              <span class="text-lg text-gray-700 dark:text-gray-300">A</span>
            </div>
          </div>
          
          <div class="mt-6">
            <button 
              @click="savePreferences" 
              class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors disabled:opacity-50"
              :disabled="preferencesLoading"
            >
              <span v-if="preferencesLoading">保存中...</span>
              <span v-else>保存个性化设置</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '~/stores/auth';

const authStore = useAuthStore();

// 加载状态
const loading = ref(true);

// 密码表单
const passwordForm = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
});
const passwordLoading = ref(false);
const passwordError = ref('');
const passwordSuccess = ref('');

// 通知设置
const notificationSettings = ref({
  comments: true,
  likes: true,
  system: true,
  email: false
});
const notificationLoading = ref(false);

// 个性化偏好
const preferences = ref({
  theme: 'system',
  fontSize: 100
});
const preferencesLoading = ref(false);

// 更新密码
const updatePassword = async () => {
  passwordError.value = '';
  passwordSuccess.value = '';
  
  // 表单验证
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    passwordError.value = '两次输入的新密码不一致';
    return;
  }
  
  if (passwordForm.value.newPassword.length < 8) {
    passwordError.value = '新密码至少需要8个字符';
    return;
  }
  
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
  if (!passwordRegex.test(passwordForm.value.newPassword)) {
    passwordError.value = '密码必须包含字母、数字和特殊字符';
    return;
  }
  
  passwordLoading.value = true;
  
  try {
    // 实际调用API更新密码
    // const response = await $fetch('/api/users/password', {
    //   method: 'PUT',
    //   headers: {
    //     Authorization: `Bearer ${authStore.getToken()}`
    //   },
    //   body: {
    //     currentPassword: passwordForm.value.currentPassword,
    //     newPassword: passwordForm.value.newPassword
    //   }
    // });
    
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 800));
    
    // 重置表单
    passwordForm.value = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
    
    passwordSuccess.value = '密码更新成功';
  } catch (error) {
    console.error('更新密码失败:', error);
    passwordError.value = '密码更新失败，请检查当前密码是否正确';
  } finally {
    passwordLoading.value = false;
  }
};

// 保存通知设置
const saveNotificationSettings = async () => {
  notificationLoading.value = true;
  
  try {
    // 实际调用API保存通知设置
    // const response = await $fetch('/api/users/notifications', {
    //   method: 'PUT',
    //   headers: {
    //     Authorization: `Bearer ${authStore.getToken()}`
    //   },
    //   body: notificationSettings.value
    // });
    
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 600));
    
    alert('通知设置已保存');
  } catch (error) {
    console.error('保存通知设置失败:', error);
    alert('保存通知设置失败，请稍后再试');
  } finally {
    notificationLoading.value = false;
  }
};

// 保存个性化设置
const savePreferences = async () => {
  preferencesLoading.value = true;
  
  try {
    // 实际调用API保存个性化设置
    // const response = await $fetch('/api/users/preferences', {
    //   method: 'PUT',
    //   headers: {
    //     Authorization: `Bearer ${authStore.getToken()}`
    //   },
    //   body: preferences.value
    // });
    
    // 实时应用主题设置
    applyTheme(preferences.value.theme);
    
    // 应用字体大小
    document.documentElement.style.fontSize = `${preferences.value.fontSize}%`;
    
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 600));
    
    // 保存到本地存储
    if (process.client) {
      localStorage.setItem('theme', preferences.value.theme);
      localStorage.setItem('fontSize', preferences.value.fontSize);
    }
    
    alert('个性化设置已保存');
  } catch (error) {
    console.error('保存个性化设置失败:', error);
    alert('保存个性化设置失败，请稍后再试');
  } finally {
    preferencesLoading.value = false;
  }
};

// 应用主题
const applyTheme = (theme) => {
  if (!process.client) return;
  
  const isDark = 
    theme === 'dark' || 
    (theme === 'system' && window.matchMedia('(prefers-color-scheme: dark)').matches);
  
  if (isDark) {
    document.documentElement.classList.add('dark');
  } else {
    document.documentElement.classList.remove('dark');
  }
};

// 初始化
onMounted(() => {
  // 确保用户已登录
  if (!authStore.isLoggedIn) {
    // 如果是客户端环境，跳转到登录页
    if (process.client) {
      navigateTo('/login');
    }
    return;
  }
  
  // 从本地存储或API加载设置
  const fetchSettings = async () => {
    try {
      // 实际项目中从API获取设置
      // const response = await $fetch('/api/users/settings', {
      //   method: 'GET',
      //   headers: {
      //     Authorization: `Bearer ${authStore.getToken()}`
      //   }
      // });
      
      // 从本地存储加载设置
      if (process.client) {
        const storedTheme = localStorage.getItem('theme');
        const storedFontSize = localStorage.getItem('fontSize');
        
        if (storedTheme) {
          preferences.value.theme = storedTheme;
        }
        
        if (storedFontSize) {
          preferences.value.fontSize = parseInt(storedFontSize);
        }
        
        // 应用主题和字体大小
        applyTheme(preferences.value.theme);
        document.documentElement.style.fontSize = `${preferences.value.fontSize}%`;
      }
      
      // 模拟API延迟
      await new Promise(resolve => setTimeout(resolve, 800));
      
    } catch (error) {
      console.error('获取设置失败:', error);
    } finally {
      loading.value = false;
    }
  };
  
  fetchSettings();
});
</script>

<style scoped>
@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}
.animate-pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

/* 自定义范围滑块样式 */
input[type=range]::-webkit-slider-thumb {
  @apply appearance-none w-4 h-4 rounded-full bg-blue-600;
}
input[type=range]::-moz-range-thumb {
  @apply w-4 h-4 rounded-full bg-blue-600 border-none;
}
</style> 