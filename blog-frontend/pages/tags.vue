<template>
  <div>
    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <h1 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
        </svg>
        标签云
      </h1>
      
      <div v-if="loading" class="animate-pulse">
        <div class="flex flex-wrap gap-4">
          <div v-for="i in 10" :key="i" class="h-8 bg-gray-200 rounded-full w-24"></div>
        </div>
      </div>
      
      <div v-else-if="!tags.length" class="text-center py-16">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
        </svg>
        <h3 class="mt-4 text-xl font-medium text-gray-800">暂无标签</h3>
        <p class="mt-2 text-gray-600">当有文章添加标签时，将在这里显示</p>
      </div>
      
      <div v-else class="flex flex-wrap gap-4">
        <div 
          v-for="tag in tags" 
          :key="tag.id"
          :class="[
            'px-4 py-2 rounded-full text-sm hover:shadow-md transition-all cursor-pointer', 
            activeTag === tag.id 
              ? 'bg-rose-500 text-white' 
              : `bg-${getTagColor(tag.name)}-100 text-${getTagColor(tag.name)}-700 hover:bg-${getTagColor(tag.name)}-200`
          ]"
          @click="setActiveTag(tag.id)"
        >
          {{ tag.name }} ({{ tag.postCount || 0 }})
        </div>
      </div>
    </div>
    
    <!-- 标签下的文章列表 -->
    <div v-if="activeTag !== null">
      <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
        <div v-if="loadingPosts" class="space-y-6">
          <div v-for="i in 3" :key="i" class="animate-pulse">
            <div class="h-6 bg-gray-200 rounded w-3/4 mb-4"></div>
            <div class="h-4 bg-gray-200 rounded w-full mb-2"></div>
            <div class="h-4 bg-gray-200 rounded w-2/3"></div>
          </div>
        </div>
        
        <div v-else-if="!tagPosts.length" class="text-center py-8">
          <h3 class="text-xl font-medium text-gray-800">没有相关文章</h3>
          <p class="mt-2 text-gray-600">此标签下暂无文章</p>
        </div>
        
        <div v-else>
          <h2 class="text-xl font-medium text-gray-800 mb-4">
            包含 "{{ activeTagName }}" 标签的文章
          </h2>
          
          <div class="space-y-4">
            <div 
              v-for="post in tagPosts" 
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
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale';
import { useRoute } from 'vue-router';

const route = useRoute();
const loading = ref(true);
const loadingPosts = ref(false);
const activeTag = ref(null);
const tags = ref([]);
const tagPosts = ref([]);

// 标签颜色映射
const tagColors = ['rose', 'blue', 'green', 'yellow', 'purple', 'indigo', 'pink', 'cyan', 'amber'];
const tagColorMap = {};

// 获取当前选中的标签名称
const activeTagName = computed(() => {
  const tag = tags.value.find(t => t.id === activeTag.value);
  return tag ? tag.name : '';
});

// 获取标签颜色
const getTagColor = (tagName) => {
  if (!tagColorMap[tagName]) {
    // 根据标签名生成一个固定的颜色
    const hash = tagName.split('').reduce((acc, char) => {
      return char.charCodeAt(0) + ((acc << 5) - acc);
    }, 0);
    
    const index = Math.abs(hash) % tagColors.length;
    tagColorMap[tagName] = tagColors[index];
  }
  
  return tagColorMap[tagName];
};

// 设置激活的标签
const setActiveTag = (tagId) => {
  activeTag.value = tagId;
  fetchTagPosts(tagId);
};

// 获取所有标签
const fetchTags = async () => {
  loading.value = true;
  
  try {
    const response = await $fetch('/api/tags', {
      method: 'GET',
      params: {
        includePostCount: true
      }
    });
    
    if (response.code === 200) {
      tags.value = response.data;
      
      // 如果URL中有标签ID参数，则设置为激活标签
      const urlTagId = route.query.id ? parseInt(route.query.id) : null;
      if (urlTagId && tags.value.some(tag => tag.id === urlTagId)) {
        setActiveTag(urlTagId);
      }
    }
  } catch (error) {
    console.error('获取标签列表失败:', error);
    
    // 使用模拟数据
    setTimeout(() => {
      tags.value = [
        { id: 1, name: 'Vue.js', postCount: 24 },
        { id: 2, name: 'Nuxt', postCount: 18 },
        { id: 3, name: 'JavaScript', postCount: 32 },
        { id: 4, name: 'Tailwind', postCount: 15 },
        { id: 5, name: 'CSS', postCount: 20 },
        { id: 6, name: 'HTML', postCount: 14 },
        { id: 7, name: 'Node.js', postCount: 10 },
        { id: 8, name: 'React', postCount: 8 },
        { id: 9, name: '前端', postCount: 25 },
        { id: 10, name: '后端', postCount: 12 }
      ];
      
      // 如果URL中有标签ID参数，则设置为激活标签
      const urlTagId = route.query.id ? parseInt(route.query.id) : null;
      if (urlTagId && tags.value.some(tag => tag.id === urlTagId)) {
        setActiveTag(urlTagId);
      }
      
      loading.value = false;
    }, 1000);
  } finally {
    loading.value = false;
  }
};

// 获取标签下的文章
const fetchTagPosts = async (tagId) => {
  if (!tagId) return;
  
  loadingPosts.value = true;
  
  try {
    const response = await $fetch(`/api/posts/tag/${tagId}`, {
      method: 'GET'
    });
    
    if (response.code === 200) {
      tagPosts.value = response.data;
    }
  } catch (error) {
    console.error('获取标签文章失败:', error);
    
    // 使用模拟数据
    setTimeout(() => {
      if (tagId === 1) { // Vue.js
        tagPosts.value = [
          {
            id: 1,
            title: '如何使用Vue 3的Composition API',
            summary: 'Vue 3引入了Composition API，这是一种新的组件逻辑组织方式，本文详细介绍其使用方法和优势。',
            createTime: new Date(Date.now() - 3*86400000).toISOString(),
            viewCount: 145
          },
          {
            id: 2,
            title: 'Vue.js性能优化技巧',
            summary: '本文分享一些Vue.js应用的性能优化技巧，包括虚拟列表、组件懒加载等方面的最佳实践。',
            createTime: new Date(Date.now() - 10*86400000).toISOString(),
            viewCount: 238
          }
        ];
      } else if (tagId === 2) { // Nuxt
        tagPosts.value = [
          {
            id: 3,
            title: 'Nuxt 3中的新特性',
            summary: 'Nuxt 3带来了许多令人兴奋的新特性，包括更小的bundle体积、改进的TypeScript支持和更好的开发体验。',
            createTime: new Date(Date.now() - 5*86400000).toISOString(),
            viewCount: 126
          }
        ];
      } else {
        tagPosts.value = [];
      }
      
      loadingPosts.value = false;
    }, 800);
  } finally {
    loadingPosts.value = false;
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

// 监听路由变化，更新激活的标签
watch(() => route.query.id, (newId) => {
  if (newId) {
    const tagId = parseInt(newId);
    if (tags.value.some(tag => tag.id === tagId)) {
      setActiveTag(tagId);
    }
  } else {
    activeTag.value = null;
  }
});

onMounted(() => {
  if (process.client) {
    fetchTags();
  }
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