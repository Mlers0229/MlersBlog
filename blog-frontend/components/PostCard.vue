<template>
  <article class="bg-white rounded-lg shadow-sm overflow-hidden hover:shadow-md transition-shadow duration-300">
    <!-- 文章封面图 -->
    <div class="aspect-w-16 aspect-h-9 bg-gray-100">
      <img
        v-if="post.coverImage"
        :src="post.coverImage"
        :alt="post.title"
        class="object-cover w-full h-full"
      />
      <div v-else class="w-full h-full flex items-center justify-center bg-gradient-to-br from-rose-50 to-indigo-50">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
        </svg>
      </div>
    </div>
    
    <!-- 文章内容 -->
    <div class="p-6">
      <!-- 分类和日期 -->
      <div class="flex justify-between items-center mb-3 text-sm">
        <span v-if="post.category" class="text-rose-500 font-medium">{{ post.category }}</span>
        <time :datetime="post.createdAt" class="text-gray-500">{{ formatDate(post.createdAt) }}</time>
      </div>
      
      <!-- 标题 -->
      <h2 class="text-xl font-bold mb-3 text-gray-800 line-clamp-2">
        <NuxtLink :to="`/post/${post.id}`" class="hover:text-rose-500">
          {{ post.title }}
        </NuxtLink>
      </h2>
      
      <!-- 摘要 -->
      <p class="text-gray-600 mb-4 line-clamp-3">{{ post.summary || truncateContent(post.content) }}</p>
      
      <!-- 底部信息 -->
      <div class="flex items-center justify-between">
        <!-- 作者信息 -->
        <div class="flex items-center">
          <img :src="post.author?.avatar || 'https://randomuser.me/api/portraits/men/1.jpg'" alt="作者头像" class="h-8 w-8 rounded-full mr-2 object-cover">
          <span class="text-sm text-gray-700">{{ post.author?.name || '博主' }}</span>
        </div>
        
        <!-- 统计信息 -->
        <div class="flex space-x-3 text-gray-500 text-sm">
          <div class="flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
            <span>{{ post.views || 0 }}</span>
          </div>
          <div class="flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
            </svg>
            <span>{{ post.comments || 0 }}</span>
          </div>
          <div class="flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
            </svg>
            <span>{{ post.likes || 0 }}</span>
          </div>
        </div>
      </div>
    </div>
  </article>
</template>

<script setup>
import { format, parseISO } from 'date-fns';
import { zhCN } from 'date-fns/locale';

// 通过props接收文章数据
const props = defineProps({
  post: {
    type: Object,
    required: true,
    default: () => ({
      id: '',
      title: '',
      content: '',
      summary: '',
      coverImage: '',
      createdAt: new Date().toISOString(),
      views: 0,
      comments: 0,
      likes: 0,
      category: '',
      author: {
        name: '',
        avatar: ''
      }
    })
  }
});

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  try {
    return format(new Date(dateString), 'yyyy年MM月dd日', { locale: zhCN });
  } catch (error) {
    console.error('日期格式化错误:', error);
    return '';
  }
};

// 截断内容
const truncateContent = (content) => {
  if (!content) return '';
  
  // 移除HTML标签并截断
  const plainText = content.replace(/<[^>]*>/g, '');
  return plainText.length > 120 ? plainText.substring(0, 120) + '...' : plainText;
};
</script> 