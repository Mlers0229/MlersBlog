<template>
  <div>
    <div class="bg-white rounded-lg shadow-sm overflow-hidden mb-6">
      <!-- 头部背景 -->
      <div class="h-40 bg-gradient-to-r from-rose-400 to-indigo-400"></div>
      
      <!-- 用户信息区 -->
      <div class="relative px-6 py-8">
        <div class="absolute -top-16 left-6">
          <img 
            :src="userInfo.avatar || '/images/default-avatar.png'" 
            alt="用户头像" 
            class="w-32 h-32 rounded-full border-4 border-white object-cover shadow-md" 
          />
        </div>
        
        <div class="mt-16 md:mt-0 md:ml-36">
          <div class="flex flex-col md:flex-row md:items-center md:justify-between">
            <div>
              <h1 class="text-2xl font-bold text-gray-800">{{ userInfo.nickname || '用户昵称' }}</h1>
              <p class="text-gray-600 mt-1">{{ userInfo.bio || '这个人很懒，没有留下任何介绍' }}</p>
            </div>
            
            <button class="mt-4 md:mt-0 px-4 py-2 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors text-sm">
              编辑资料
            </button>
          </div>
          
          <div class="flex gap-6 mt-6">
            <div class="text-center">
              <div class="text-xl font-bold text-gray-800">{{ userStats.postCount || 0 }}</div>
              <div class="text-sm text-gray-500">文章</div>
            </div>
            <div class="text-center">
              <div class="text-xl font-bold text-gray-800">{{ userStats.followerCount || 0 }}</div>
              <div class="text-sm text-gray-500">关注者</div>
            </div>
            <div class="text-center">
              <div class="text-xl font-bold text-gray-800">{{ userStats.followingCount || 0 }}</div>
              <div class="text-sm text-gray-500">关注中</div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 标签栏 -->
      <div class="border-t border-gray-100">
        <div class="flex">
          <button 
            v-for="(tab, index) in tabs" 
            :key="index"
            :class="[
              'flex-1 py-3 font-medium', 
              activeTab === index 
                ? 'text-rose-500 border-b-2 border-rose-500' 
                : 'text-gray-500 hover:text-gray-700'
            ]"
            @click="activeTab = index"
          >
            {{ tab }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- 我的文章 -->
    <div v-if="activeTab === 0">
      <div v-if="loading" class="space-y-6">
        <div v-for="i in 3" :key="i" class="bg-white rounded-lg shadow-sm p-4 animate-pulse">
          <div class="h-6 bg-gray-200 rounded w-3/4 mb-4"></div>
          <div class="h-4 bg-gray-200 rounded w-full mb-2"></div>
          <div class="h-4 bg-gray-200 rounded w-2/3"></div>
        </div>
      </div>
      
      <div v-else-if="!userPosts.length" class="bg-white rounded-lg shadow-sm p-12 text-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
        </svg>
        <h3 class="mt-4 text-xl font-medium text-gray-800">暂无文章</h3>
        <p class="mt-2 text-gray-600">点击下方按钮发布您的第一篇文章</p>
        <NuxtLink to="/post/create" class="mt-4 inline-block px-6 py-2 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors">
          写文章
        </NuxtLink>
      </div>
      
      <div v-else class="space-y-4">
        <div 
          v-for="post in userPosts" 
          :key="post.id"
          class="bg-white rounded-lg shadow-sm p-4 hover:shadow-md transition-shadow"
        >
          <div class="flex justify-between items-start">
            <div>
              <h3 class="text-lg font-medium text-gray-800 hover:text-rose-500 transition-colors">
                <NuxtLink :to="`/post/${post.id}`">{{ post.title }}</NuxtLink>
              </h3>
              <p class="text-sm text-gray-600 mt-1 line-clamp-2">{{ post.summary }}</p>
              <div class="flex items-center text-xs text-gray-500 mt-2">
                <span>{{ formatDate(post.createTime || post.createdAt) }}</span>
                <span class="mx-2">·</span>
                <span class="flex items-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                  {{ post.viewCount || 0 }}
                </span>
                <span class="mx-2">·</span>
                <span class="flex items-center">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z" />
                  </svg>
                  {{ post.commentCount || 0 }}
                </span>
              </div>
            </div>
            
            <div class="flex space-x-2">
              <NuxtLink :to="`/post/edit/${post.id}`" class="text-gray-500 hover:text-gray-700">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
              </NuxtLink>
              <button class="text-gray-500 hover:text-gray-700" @click="confirmDelete(post.id)">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 收藏文章 -->
    <div v-else-if="activeTab === 1">
      <div v-if="!favorites.length" class="bg-white rounded-lg shadow-sm p-12 text-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" />
        </svg>
        <h3 class="mt-4 text-xl font-medium text-gray-800">暂无收藏</h3>
        <p class="mt-2 text-gray-600">浏览文章时点击收藏按钮将文章添加到此处</p>
      </div>
      
      <div v-else class="space-y-4">
        <!-- 与文章列表类似的结构 -->
      </div>
    </div>
    
    <!-- 我的评论 -->
    <div v-else>
      <div v-if="!comments.length" class="bg-white rounded-lg shadow-sm p-12 text-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z" />
        </svg>
        <h3 class="mt-4 text-xl font-medium text-gray-800">暂无评论</h3>
        <p class="mt-2 text-gray-600">您的评论记录将显示在这里</p>
      </div>
      
      <div v-else class="space-y-4">
        <!-- 评论列表 -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const loading = ref(true);
const activeTab = ref(0);
const tabs = ['我的文章', '收藏文章', '我的评论'];

const userInfo = ref({
  nickname: '技术达人',
  avatar: '',
  bio: '热爱技术，热爱分享'
});

const userStats = ref({
  postCount: 5,
  followerCount: 128,
  followingCount: 65
});

const userPosts = ref([
  {
    id: 1,
    title: '如何使用Nuxt 3构建高性能博客',
    summary: 'Nuxt 3提供了许多新特性，包括改进的性能、更小的包大小和更好的开发体验。本文将探讨如何利用Nuxt 3构建高性能的博客系统。',
    createTime: new Date(Date.now() - 2*86400000).toISOString(),
    viewCount: 156,
    commentCount: 23
  },
  {
    id: 2,
    title: 'Tailwind CSS与传统CSS框架的比较',
    summary: '本文比较了Tailwind CSS与Bootstrap等传统CSS框架的差异，分析它们各自的优缺点，以及适用场景。',
    createTime: new Date(Date.now() - 5*86400000).toISOString(),
    viewCount: 89,
    commentCount: 7
  }
]);

const favorites = ref([]);
const comments = ref([]);

// 获取用户信息
const fetchUserInfo = async () => {
  if (!authStore.isLoggedIn) return;
  
  try {
    const response = await $fetch('/api/users/current', {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      userInfo.value = response.data;
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
};

// 获取用户统计数据
const fetchUserStats = async () => {
  if (!authStore.isLoggedIn) return;
  
  try {
    const response = await $fetch('/api/users/stats', {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      userStats.value = response.data;
    }
  } catch (error) {
    console.error('获取用户统计数据失败:', error);
  }
};

// 获取用户文章
const fetchUserPosts = async () => {
  if (!authStore.isLoggedIn) return;
  
  loading.value = true;
  try {
    const response = await $fetch('/api/posts/user', {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      userPosts.value = response.data;
    }
  } catch (error) {
    console.error('获取用户文章失败:', error);
  } finally {
    loading.value = false;
  }
};

// 确认删除文章
const confirmDelete = (postId) => {
  if (process.client) {
    if (confirm('确定要删除这篇文章吗？此操作不可撤销。')) {
      deletePost(postId);
    }
  }
};

// 删除文章
const deletePost = async (postId) => {
  try {
    const response = await $fetch(`/api/posts/${postId}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      userPosts.value = userPosts.value.filter(post => post.id !== postId);
      alert('文章删除成功');
    }
  } catch (error) {
    console.error('删除文章失败:', error);
    alert('删除文章失败，请稍后再试');
  }
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  const now = new Date();
  const diffInDays = Math.floor((now - date) / (1000 * 60 * 60 * 24));
  
  if (diffInDays < 1) {
    return '今天';
  } else if (diffInDays < 2) {
    return '昨天';
  } else if (diffInDays < 7) {
    return `${diffInDays}天前`;
  } else if (diffInDays < 30) {
    return `${Math.floor(diffInDays / 7)}周前`;
  } else {
    return format(date, 'yyyy年MM月dd日', { locale: zhCN });
  }
};

onMounted(() => {
  // 在现阶段仅使用模拟数据
  setTimeout(() => {
    loading.value = false;
  }, 1000);
  
  // 真实API调用在后端可用时使用
  /*
  if (process.client) {
    fetchUserInfo();
    fetchUserStats();
    fetchUserPosts();
  }
  */
});
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;  
  overflow: hidden;
}

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
</style> 