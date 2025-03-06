<template>
  <div class="min-h-screen flex bg-gray-100">
    <!-- 左侧菜单栏 -->
    <div class="w-64 bg-gray-800 text-white flex flex-col">
      <!-- 管理控制台标题 -->
      <div class="p-4 text-xl font-bold border-b border-gray-700 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
        </svg>
        博客管理系统
      </div>

      <!-- 管理菜单 -->
      <nav class="flex-grow py-4">
        <NuxtLink to="/admin/dashboard" class="flex items-center px-4 py-3 hover:bg-gray-700" :class="{ 'bg-gray-700': $route.path === '/admin/dashboard' }">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
          </svg>
          仪表盘
        </NuxtLink>

        <NuxtLink to="/admin/articles" class="flex items-center px-4 py-3 hover:bg-gray-700" :class="{ 'bg-gray-700': $route.path.startsWith('/admin/articles') }">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
          </svg>
          文章管理
        </NuxtLink>

        <NuxtLink to="/admin/comments" class="flex items-center px-4 py-3 hover:bg-gray-700" :class="{ 'bg-gray-700': $route.path.startsWith('/admin/comments') }">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
          </svg>
          评论管理
        </NuxtLink>

        <NuxtLink to="/admin/users" class="flex items-center px-4 py-3 hover:bg-gray-700" :class="{ 'bg-gray-700': $route.path.startsWith('/admin/users') }">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
          </svg>
          用户管理
        </NuxtLink>

        <NuxtLink to="/admin/statistics" class="flex items-center px-4 py-3 hover:bg-gray-700" :class="{ 'bg-gray-700': $route.path === '/admin/statistics' }">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
          </svg>
          统计分析
        </NuxtLink>
      </nav>

      <!-- 返回前台和退出登录 -->
      <div class="p-4 border-t border-gray-700">
        <NuxtLink to="/" class="flex items-center text-gray-300 hover:text-white mb-4">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 17l-5-5m0 0l5-5m-5 5h12" />
          </svg>
          返回前台
        </NuxtLink>
        <button @click="logoutAdmin" class="flex items-center text-gray-300 hover:text-white">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
          </svg>
          退出登录
        </button>
      </div>
    </div>

    <!-- 右侧内容区域 -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- 顶部导航栏 -->
      <header class="bg-white shadow-sm">
        <div class="px-6 py-4 flex items-center justify-between">
          <h1 class="text-xl font-semibold text-gray-800">{{ pageTitle }}</h1>
          <div class="flex items-center">
            <span class="text-gray-700 mr-4">{{ authStore.userInfo.nickname || authStore.userInfo.username }}</span>
            <div class="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center text-gray-700 overflow-hidden">
              <img v-if="authStore.userInfo.avatar" :src="authStore.userInfo.avatar" alt="头像" class="w-full h-full object-cover">
              <span v-else>{{ authStore.userInfo.nickname?.[0] || authStore.userInfo.username?.[0] || 'A' }}</span>
            </div>
          </div>
        </div>
      </header>

      <!-- 主内容 -->
      <main class="flex-1 overflow-y-auto p-6">
        <slot/>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

// 计算页面标题
const pageTitle = computed(() => {
  const routePath = route.path;
  if (routePath === '/admin/dashboard') return '管理控制台';
  if (routePath.startsWith('/admin/articles')) return '文章管理';
  if (routePath.startsWith('/admin/comments')) return '评论管理';
  if (routePath.startsWith('/admin/users')) return '用户管理';
  if (routePath === '/admin/statistics') return '统计分析';
  return '管理控制台';
});

// 检查用户权限
onMounted(async () => {
  if (!authStore.isLoggedIn) {
    // 未登录，重定向到登录页
    router.push('/login?redirect=' + encodeURIComponent(route.fullPath));
    return;
  }

  // 不是管理员，重定向到首页
  if (!authStore.isAdmin) {
    router.push('/');
  }
});

// 退出登录
const logoutAdmin = () => {
  authStore.logout();
  router.push('/login');
};
</script> 