<template>
  <div>
    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-gray-800">控制台</h1>
        <NuxtLink v-if="authStore.isLoggedIn" to="/post/create" class="px-4 py-2 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          创建文章
        </NuxtLink>
      </div>
      
      <div v-if="!authStore.isLoggedIn" class="text-center py-16">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
        </svg>
        <h3 class="mt-4 text-xl font-medium text-gray-800">需要登录</h3>
        <p class="mt-2 text-gray-600">请先登录后再访问控制台</p>
        <div class="mt-6">
          <NuxtLink to="/login" class="px-6 py-2 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors inline-block">
            去登录
          </NuxtLink>
        </div>
      </div>
      
      <div v-else>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div class="bg-rose-50 rounded-lg p-6">
            <div class="flex items-center justify-between">
              <h3 class="text-lg font-medium text-gray-800">我的文章</h3>
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
            </div>
            <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.posts || 0 }}</p>
            <p class="text-sm text-gray-600 mt-1">已发布文章数量</p>
          </div>
          
          <div class="bg-blue-50 rounded-lg p-6">
            <div class="flex items-center justify-between">
              <h3 class="text-lg font-medium text-gray-800">总阅读量</h3>
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
            </div>
            <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.views || 0 }}</p>
            <p class="text-sm text-gray-600 mt-1">文章总阅读量</p>
          </div>
          
          <div class="bg-green-50 rounded-lg p-6">
            <div class="flex items-center justify-between">
              <h3 class="text-lg font-medium text-gray-800">收获点赞</h3>
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" />
              </svg>
            </div>
            <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.likes || 0 }}</p>
            <p class="text-sm text-gray-600 mt-1">收获点赞数量</p>
          </div>
        </div>
        
        <div class="mb-8">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-medium text-gray-800">最近发布</h3>
            <NuxtLink to="/post/create" class="text-rose-500 hover:text-rose-600 transition-colors">
              写文章
            </NuxtLink>
          </div>
          
          <div v-if="loading" class="space-y-4">
            <div v-for="i in 3" :key="i" class="animate-pulse">
              <div class="h-6 bg-gray-200 rounded w-3/4 mb-2"></div>
              <div class="h-4 bg-gray-200 rounded w-1/2"></div>
            </div>
          </div>
          
          <div v-else-if="!recentPosts.length" class="bg-gray-50 rounded-lg p-6 text-center">
            <p class="text-gray-600">暂无文章，快去发布你的第一篇文章吧！</p>
            <NuxtLink to="/post/create" class="mt-4 px-6 py-2 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors inline-block">
              写文章
            </NuxtLink>
          </div>
          
          <div v-else class="space-y-3">
            <div 
              v-for="post in recentPosts" 
              :key="post.id"
              class="bg-gray-50 rounded-lg p-4 flex justify-between items-center"
            >
              <div>
                <h4 class="font-medium text-gray-800">{{ post.title }}</h4>
                <div class="flex items-center text-xs text-gray-500 mt-1">
                  <span>{{ formatDate(post.createdAt) }}</span>
                  <span class="mx-2">·</span>
                  <span class="flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                    {{ post.viewCount || 0 }}
                  </span>
                </div>
              </div>
              <div class="flex space-x-2">
                <NuxtLink :to="`/post/${post.id}`" class="p-2 text-blue-500 hover:bg-blue-100 rounded">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                </NuxtLink>
                <NuxtLink :to="`/post/edit/${post.id}`" class="p-2 text-amber-500 hover:bg-amber-100 rounded">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </NuxtLink>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale';

const authStore = useAuthStore();
const loading = ref(true);
const stats = ref({
  posts: 0,
  views: 0,
  likes: 0
});
const recentPosts = ref([]);

// 获取统计数据
const fetchDashboardData = async () => {
  if (!authStore.isLoggedIn) return;
  
  loading.value = true;
  
  try {
    // 实际环境中应调用API
    const response = await $fetch('/api/user/dashboard', {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      stats.value = response.data.stats;
      recentPosts.value = response.data.recentPosts;
    }
  } catch (error) {
    console.error('获取控制台数据失败:', error);
    
    // 使用模拟数据
    setTimeout(() => {
      stats.value = {
        posts: 8,
        views: 1254,
        likes: 42
      };
      
      recentPosts.value = [
        {
          id: 1,
          title: '如何使用Nuxt 3构建高性能博客系统',
          createdAt: new Date(Date.now() - 2*86400000).toISOString(),
          viewCount: 145
        },
        {
          id: 2,
          title: 'Vue.js与React的对比：如何选择适合你的框架',
          createdAt: new Date(Date.now() - 5*86400000).toISOString(),
          viewCount: 238
        },
        {
          id: 3,
          title: '前端开发中的设计模式应用',
          createdAt: new Date(Date.now() - 10*86400000).toISOString(),
          viewCount: 86
        }
      ];
      
      loading.value = false;
    }, 1000);
  } finally {
    loading.value = false;
  }
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  const now = new Date();
  
  // 如果是今天
  if (date.toDateString() === now.toDateString()) {
    return '今天';
  }
  
  // 如果是昨天
  const yesterday = new Date();
  yesterday.setDate(yesterday.getDate() - 1);
  if (date.toDateString() === yesterday.toDateString()) {
    return '昨天';
  }
  
  // 如果是一周内
  const oneWeekAgo = new Date();
  oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);
  if (date > oneWeekAgo) {
    return format(date, 'EEEE', { locale: zhCN });
  }
  
  // 其他日期
  return format(date, 'yyyy年MM月dd日', { locale: zhCN });
};

onMounted(() => {
  fetchDashboardData();
});
</script> 