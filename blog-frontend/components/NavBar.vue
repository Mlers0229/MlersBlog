<template>
  <header class="bg-white shadow-sm sticky top-0 z-50">
    <div class="container mx-auto px-4">
      <div class="flex justify-between items-center h-16">
        <!-- 博客标题/Logo -->
        <div class="flex items-center">
          <NuxtLink to="/" class="flex items-center">
            <span class="text-2xl font-bold text-rose-500">博客</span>
            <span class="text-2xl font-bold text-gray-700">系统</span>
          </NuxtLink>
        </div>
        
        <!-- 导航链接 - 桌面版 -->
        <nav class="hidden md:flex space-x-8">
          <NuxtLink to="/" class="text-gray-700 hover:text-rose-500 px-3 py-2 text-sm font-medium">首页</NuxtLink>
          <NuxtLink to="/tags" class="text-gray-700 hover:text-rose-500 px-3 py-2 text-sm font-medium">标签</NuxtLink>
          <NuxtLink to="/archives" class="text-gray-700 hover:text-rose-500 px-3 py-2 text-sm font-medium">归档</NuxtLink>
          <NuxtLink to="/about" class="text-gray-700 hover:text-rose-500 px-3 py-2 text-sm font-medium">关于</NuxtLink>
        </nav>
        
        <!-- 用户区域 -->
        <div class="flex items-center space-x-4">
          <!-- 搜索按钮 -->
          <button @click="toggleSearch" class="text-gray-500 hover:text-rose-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </button>
          
          <!-- 通知按钮 - 仅在登录后显示 -->
          <NotificationCenter v-if="authStore.isLoggedIn" ref="notificationCenter" />
          
          <!-- 创建文章按钮 - 仅在登录后显示 -->
          <NuxtLink v-if="authStore.isLoggedIn" to="/post/create" class="hidden md:flex items-center bg-rose-500 text-white hover:bg-rose-600 px-3 py-2 rounded-md text-sm font-medium">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            <span>写文章</span>
          </NuxtLink>
          
          <!-- 用户菜单（登录状态） -->
          <div v-if="authStore.isLoggedIn" class="relative user-menu-container">
            <button @click="toggleUserMenu" class="flex items-center focus:outline-none">
              <img 
                :src="authStore.user?.avatar || 'https://ui-avatars.com/api/?name=' + encodeURIComponent(authStore.user?.nickname || 'User')" 
                :alt="authStore.user?.nickname || '用户'" 
                class="h-8 w-8 rounded-full object-cover border-2 border-rose-200"
              >
            </button>
            
            <!-- 用户下拉菜单 -->
            <div v-if="showUserMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50">
              <div class="px-4 py-2 border-b border-gray-100">
                <div class="font-semibold text-gray-800">{{ authStore.user?.nickname || '用户' }}</div>
                <div class="text-xs text-gray-500">{{ authStore.user?.email || '' }}</div>
              </div>
              <NuxtLink to="/profile" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">个人主页</NuxtLink>
              <NuxtLink v-if="authStore.isAdmin" to="/dashboard" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">管理控制台</NuxtLink>
              <NuxtLink to="/settings" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">设置</NuxtLink>
              <button @click="logout" class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">退出登录</button>
            </div>
          </div>
          
          <!-- 登录/注册按钮（未登录状态） -->
          <div v-else class="hidden md:flex items-center space-x-2">
            <NuxtLink to="/login" class="text-rose-500 hover:text-rose-600 px-3 py-2 text-sm font-medium">登录</NuxtLink>
            <NuxtLink to="/register" class="bg-rose-500 hover:bg-rose-600 text-white px-3 py-2 rounded-md text-sm font-medium">注册</NuxtLink>
          </div>
          
          <!-- 移动端菜单按钮 -->
          <button @click="toggleMobileMenu" class="md:hidden text-gray-500 hover:text-rose-500">
            <svg v-if="!showMobileMenu" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
            <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>
      
      <!-- 移动端菜单 -->
      <div v-if="showMobileMenu" class="md:hidden pt-2 pb-4 border-t border-gray-200">
        <NuxtLink to="/" class="block px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">首页</NuxtLink>
        <NuxtLink to="/tags" class="block px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">标签</NuxtLink>
        <NuxtLink to="/archives" class="block px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">归档</NuxtLink>
        <NuxtLink to="/about" class="block px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">关于</NuxtLink>
        
        <!-- 登录菜单项 - 仅在未登录时显示 -->
        <div v-if="!authStore.isLoggedIn" class="mt-3 pt-3 border-t border-gray-200">
          <NuxtLink to="/login" class="block px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">登录</NuxtLink>
          <NuxtLink to="/register" class="block px-3 py-2 text-base font-medium text-rose-500 hover:bg-rose-600 hover:bg-gray-50 rounded-md">注册</NuxtLink>
        </div>
        
        <!-- 用户菜单项 - 仅在登录后显示 -->
        <div v-else class="mt-3 pt-3 border-t border-gray-200">
          <NuxtLink to="/profile" class="block px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">个人主页</NuxtLink>
          <NuxtLink to="/post/create" class="block px-3 py-2 text-base font-medium text-white bg-rose-500 hover:bg-rose-600 rounded-md flex items-center justify-center my-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            写文章
          </NuxtLink>
          <NuxtLink v-if="authStore.isAdmin" to="/dashboard" class="block px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">管理控制台</NuxtLink>
          <NuxtLink to="/settings" class="block px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">设置</NuxtLink>
          <button @click="logout" class="block w-full text-left px-3 py-2 text-base font-medium text-gray-700 hover:text-rose-500 hover:bg-gray-50 rounded-md">退出登录</button>
        </div>
      </div>
      
      <!-- 搜索栏 -->
      <div v-if="showSearch" class="py-4 border-t border-gray-200">
        <div class="relative">
          <input type="text" placeholder="搜索文章..." class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-300">
          <button class="absolute right-0 top-0 h-full px-4 text-gray-500 hover:text-rose-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </button>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useAuthStore } from '~/stores/auth';
import NotificationCenter from './NotificationCenter.vue';

// 使用 auth store
const authStore = useAuthStore();
const notificationCenter = ref(null);

// 控制UI显示
const showMobileMenu = ref(false); // 控制移动端菜单显示
const showUserMenu = ref(false); // 控制用户下拉菜单显示
const showSearch = ref(false); // 控制搜索栏显示

// 方法
const toggleMobileMenu = () => {
  showMobileMenu.value = !showMobileMenu.value;
  // 关闭其他菜单
  showUserMenu.value = false;
  showSearch.value = false;
};

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value;
};

const toggleSearch = () => {
  showSearch.value = !showSearch.value;
  // 关闭其他菜单
  showMobileMenu.value = false;
};

const logout = () => {
  // 调用 auth store 的登出方法
  authStore.logout();
  showUserMenu.value = false;
  
  // 可以添加一个成功通知
  notificationCenter.value?.addNotification({
    type: 'system',
    title: '退出成功',
    content: '您已成功退出登录',
    actor: { nickname: '系统' },
    target: { id: 0, title: '系统通知' }
  });
};

// 在组件挂载时初始化认证状态
onMounted(() => {
  authStore.initializeAuth();
});

// 点击外部关闭用户菜单
const closeUserMenu = (e) => {
  if (showUserMenu.value && !e.target.closest('.user-menu-container')) {
    showUserMenu.value = false;
  }
};

// 监听点击事件关闭菜单
onMounted(() => {
  document.addEventListener('click', closeUserMenu);
});

// 组件卸载时移除事件监听
onUnmounted(() => {
  document.removeEventListener('click', closeUserMenu);
});
</script> 