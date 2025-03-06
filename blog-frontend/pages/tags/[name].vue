<template>
  <div>
    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <h1 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
        </svg>
        标签: {{ tagName }}
      </h1>
      
      <NuxtLink to="/tags" class="text-rose-500 hover:text-rose-600 transition-colors flex items-center mb-6">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
        </svg>
        返回所有标签
      </NuxtLink>
    </div>
    
    <!-- 标签下的文章列表 -->
    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <div v-if="loading" class="space-y-6">
        <div v-for="i in 3" :key="i" class="animate-pulse">
          <div class="h-6 bg-gray-200 rounded w-3/4 mb-4"></div>
          <div class="h-4 bg-gray-200 rounded w-full mb-2"></div>
          <div class="h-4 bg-gray-200 rounded w-2/3"></div>
        </div>
      </div>
      
      <div v-else-if="!posts.length" class="text-center py-8">
        <h3 class="text-xl font-medium text-gray-800">没有相关文章</h3>
        <p class="mt-2 text-gray-600">此标签下暂无文章</p>
      </div>
      
      <div v-else>
        <h2 class="text-xl font-medium text-gray-800 mb-4">
          包含 "{{ tagName }}" 标签的文章
        </h2>
        
        <div class="space-y-4">
          <div 
            v-for="post in posts" 
            :key="post.id"
            class="border-b border-gray-100 pb-4 last:border-0 last:pb-0"
          >
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
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale';
import { useRoute } from 'vue-router';

const route = useRoute();
const loading = ref(true);
const tagName = ref('');
const posts = ref([]);

// 获取标签文章
const fetchTagPosts = async () => {
  loading.value = true;
  tagName.value = route.params.name ? decodeURIComponent(route.params.name) : '';
  
  if (!tagName.value) {
    loading.value = false;
    return;
  }
  
  try {
    // 实际环境中应该按标签名称查询
    const response = await $fetch(`/api/posts/tag-name/${encodeURIComponent(tagName.value)}`, {
      method: 'GET'
    });
    
    if (response.code === 200) {
      posts.value = response.data;
    }
  } catch (error) {
    console.error('获取标签文章失败:', error);
    
    // 使用模拟数据
    setTimeout(() => {
      // 根据标签名生成一些模拟文章
      if (tagName.value.toLowerCase() === 'vue' || tagName.value.toLowerCase() === 'vue.js') {
        posts.value = [
          {
            id: 1,
            title: '如何使用Vue 3的Composition API',
            summary: 'Vue 3引入了Composition API，这是一种新的组件逻辑组织方式，本文详细介绍其使用方法和优势。',
            createdAt: new Date(Date.now() - 3*86400000).toISOString(),
            viewCount: 145
          },
          {
            id: 2,
            title: 'Vue.js性能优化技巧',
            summary: '本文分享一些Vue.js应用的性能优化技巧，包括虚拟列表、组件懒加载等方面的最佳实践。',
            createdAt: new Date(Date.now() - 10*86400000).toISOString(),
            viewCount: 238
          }
        ];
      } else if (tagName.value.toLowerCase() === 'nuxt') {
        posts.value = [
          {
            id: 3,
            title: 'Nuxt 3中的新特性',
            summary: 'Nuxt 3带来了许多令人兴奋的新特性，包括更小的bundle体积、改进的TypeScript支持和更好的开发体验。',
            createdAt: new Date(Date.now() - 5*86400000).toISOString(),
            viewCount: 126
          }
        ];
      } else if (tagName.value.toLowerCase() === 'javascript') {
        posts.value = [
          {
            id: 4,
            title: 'JavaScript异步编程全解析',
            summary: '从回调函数到Promise，再到async/await，本文全面解析JavaScript中的异步编程模式。',
            createdAt: new Date(Date.now() - 15*86400000).toISOString(),
            viewCount: 320
          },
          {
            id: 5,
            title: '深入理解JavaScript闭包',
            summary: '闭包是JavaScript中的一个强大特性，本文将帮助你深入理解闭包的原理和应用场景。',
            createdAt: new Date(Date.now() - 22*86400000).toISOString(),
            viewCount: 185
          }
        ];
      } else {
        posts.value = [];
      }
      
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
  fetchTagPosts();
});
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style> 