<template>
  <div class="admin-dashboard min-h-screen bg-gray-50">
    <!-- 顶部导航 -->
    <div class="bg-white border-b border-gray-200 fixed top-0 left-0 right-0 z-30">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <NuxtLink to="/" class="flex-shrink-0 flex items-center">
              <span class="text-xl font-bold text-rose-500">Blog</span>
              <span class="ml-2 text-gray-500 hidden md:inline">管理控制台</span>
            </NuxtLink>
          </div>
          
          <div class="flex items-center">
            <div class="ml-4 flex items-center md:ml-6">
              <span class="text-sm text-gray-600 mr-2">管理员</span>
              <img 
                :src="authStore?.user?.avatar || '/img/default-avatar.png'" 
                alt="管理员头像" 
                class="h-8 w-8 rounded-full"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="pt-16 flex h-screen">
      <!-- 侧边导航栏 -->
      <div class="w-64 bg-white shadow-sm h-full fixed left-0 top-16 overflow-y-auto">
        <nav class="mt-5 px-2">
          <div class="space-y-1">
            <NuxtLink 
              to="/admin" 
              class="group flex items-center px-2 py-2 text-base font-medium rounded-md"
              :class="[$route.path === '/admin' ? 'bg-rose-100 text-rose-600' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900']"
              exact-active-class="bg-rose-100 text-rose-600"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="mr-4 h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 8v8m-4-5v5m-4-2v2m-2 4h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              仪表盘
            </NuxtLink>
            
            <NuxtLink 
              to="/admin/posts" 
              class="group flex items-center px-2 py-2 text-base font-medium rounded-md"
              :class="[$route.path.startsWith('/admin/posts') ? 'bg-rose-100 text-rose-600' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900']"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="mr-4 h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
              </svg>
              文章管理
            </NuxtLink>
            
            <NuxtLink 
              to="/admin/comments" 
              class="group flex items-center px-2 py-2 text-base font-medium rounded-md"
              :class="[$route.path.startsWith('/admin/comments') ? 'bg-rose-100 text-rose-600' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900']"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="mr-4 h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
              </svg>
              评论管理
            </NuxtLink>
            
            <NuxtLink 
              to="/admin/users" 
              class="group flex items-center px-2 py-2 text-base font-medium rounded-md"
              :class="[$route.path.startsWith('/admin/users') ? 'bg-rose-100 text-rose-600' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900']"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="mr-4 h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
              用户管理
            </NuxtLink>
            
            <NuxtLink 
              to="/admin/settings" 
              class="group flex items-center px-2 py-2 text-base font-medium rounded-md"
              :class="[$route.path.startsWith('/admin/settings') ? 'bg-rose-100 text-rose-600' : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900']"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="mr-4 h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              系统设置
            </NuxtLink>
          </div>
        </nav>
        
        <!-- 返回前台 -->
        <div class="mt-8 px-2">
          <NuxtLink 
            to="/" 
            class="group flex items-center px-2 py-2 text-base font-medium rounded-md text-gray-600 hover:bg-gray-50 hover:text-gray-900"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="mr-4 h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 17l-5-5m0 0l5-5m-5 5h12" />
            </svg>
            返回博客
          </NuxtLink>
        </div>
      </div>
      
      <!-- 主内容区域 -->
      <div class="flex-1 ml-64">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <slot />
          <NuxtPage />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeMount } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

// 权限检查
onBeforeMount(() => {
  if (!authStore.isLoggedIn) {
    // 如果未登录，重定向到登录页
    router.push('/login');
    return;
  }
  
  // 如果不是管理员，禁止访问
  if (authStore.user && authStore.user.role !== 'admin') {
    // 显示无权限信息并重定向
    alert('您没有管理员权限');
    router.push('/');
  }
});
</script>

<style scoped>
/* 管理控制台的特定样式可以在这里添加 */
</style> 