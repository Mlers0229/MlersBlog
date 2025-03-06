<template>
  <div>
    <h1 class="text-2xl font-bold text-gray-800 mb-6">所有文章</h1>
    
    <!-- 筛选选项 -->
    <div class="bg-white rounded-lg shadow-sm p-4 mb-6">
      <div class="flex flex-wrap gap-4">
        <!-- 分类筛选 -->
        <div class="flex items-center">
          <span class="text-gray-600 mr-2">分类:</span>
          <select 
            v-model="selectedCategory" 
            class="border border-gray-200 rounded-lg px-3 py-1 focus:outline-none focus:ring-2 focus:ring-rose-300 focus:border-transparent"
          >
            <option value="">全部</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        
        <!-- 排序方式 -->
        <div class="flex items-center">
          <span class="text-gray-600 mr-2">排序:</span>
          <select 
            v-model="sortBy" 
            class="border border-gray-200 rounded-lg px-3 py-1 focus:outline-none focus:ring-2 focus:ring-rose-300 focus:border-transparent"
          >
            <option value="newest">最新发布</option>
            <option value="popular">最多阅读</option>
            <option value="comments">评论最多</option>
          </select>
        </div>
      </div>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="bg-white rounded-lg shadow-sm p-6">
      <div class="animate-pulse space-y-6">
        <div v-for="i in 5" :key="i" class="flex space-x-4">
          <div class="rounded-md bg-gray-200 h-24 w-32"></div>
          <div class="flex-1 space-y-4 py-1">
            <div class="h-4 bg-gray-200 rounded w-3/4"></div>
            <div class="space-y-2">
              <div class="h-4 bg-gray-200 rounded"></div>
              <div class="h-4 bg-gray-200 rounded w-5/6"></div>
            </div>
            <div class="h-4 bg-gray-200 rounded w-1/4"></div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-else-if="!posts.length" class="bg-white rounded-lg shadow-sm p-8 text-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
      </svg>
      <h3 class="text-xl font-medium text-gray-700 mt-4">暂无文章</h3>
      <p class="text-gray-500 mt-2">当前条件下没有找到任何文章</p>
      <div class="mt-4">
        <button 
          @click="resetFilters"
          class="bg-gray-100 hover:bg-gray-200 text-gray-700 px-4 py-2 rounded-lg mr-2"
        >
          重置筛选
        </button>
        <NuxtLink 
          to="/post/create" 
          class="bg-rose-500 hover:bg-rose-600 text-white px-4 py-2 rounded-lg"
        >
          写文章
        </NuxtLink>
      </div>
    </div>
    
    <!-- 文章列表 -->
    <div v-else class="space-y-4">
      <div 
        v-for="post in posts" 
        :key="post.id"
        class="bg-white rounded-lg shadow-sm overflow-hidden hover:shadow-md transition-shadow"
      >
        <div class="flex flex-col md:flex-row">
          <!-- 文章封面图 -->
          <div class="md:w-1/4 h-48 md:h-auto overflow-hidden">
            <NuxtLink :to="`/post/${post.id}`">
              <img 
                :src="post.coverImage || '/images/default-cover.jpg'" 
                alt="文章封面" 
                class="w-full h-full object-cover transition-transform hover:scale-105"
              />
            </NuxtLink>
          </div>
          
          <!-- 文章信息 -->
          <div class="p-6 md:w-3/4">
            <NuxtLink :to="`/post/${post.id}`" class="block mb-2">
              <h2 class="text-xl font-bold text-gray-800 hover:text-rose-500 transition-colors">
                {{ post.title }}
              </h2>
            </NuxtLink>
            
            <p class="text-gray-600 mb-4 line-clamp-2">{{ post.summary }}</p>
            
            <div class="flex items-center text-sm text-gray-500">
              <div class="flex items-center">
                <img 
                  :src="post.author?.avatar || '/images/default-avatar.png'" 
                  alt="作者头像" 
                  class="w-6 h-6 rounded-full mr-2 object-cover"
                />
                <span>{{ post.author?.nickname || '匿名用户' }}</span>
              </div>
              <span class="mx-2">·</span>
              <span>{{ formatDate(post.createdAt) }}</span>
              <span class="mx-2">·</span>
              <div class="flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
                <span>{{ post.viewCount || 0 }}</span>
              </div>
              <span class="mx-2">·</span>
              <div class="flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                </svg>
                <span>{{ post.comments?.length || 0 }}</span>
              </div>
            </div>
            
            <!-- 标签 -->
            <div v-if="post.tags && post.tags.length > 0" class="flex flex-wrap gap-2 mt-3">
              <NuxtLink 
                v-for="tag in post.tags.slice(0, 3)" 
                :key="tag.id"
                :to="`/tags?id=${tag.id}`"
                class="px-2 py-0.5 bg-gray-100 text-xs text-gray-600 rounded-full hover:bg-rose-100 hover:text-rose-600"
              >
                {{ tag.name }}
              </NuxtLink>
              <span v-if="post.tags.length > 3" class="text-xs text-gray-500">+{{ post.tags.length - 3 }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <div v-if="posts.length > 0" class="mt-8 flex justify-center">
      <div class="flex items-center space-x-2">
        <button 
          @click="previousPage" 
          :disabled="currentPage === 1"
          class="px-3 py-1 border border-gray-300 rounded-md text-gray-600 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          上一页
        </button>
        
        <span class="text-gray-600">第 {{ currentPage }} 页</span>
        
        <button 
          @click="nextPage" 
          :disabled="posts.length < pageSize"
          class="px-3 py-1 border border-gray-300 rounded-md text-gray-600 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watchEffect, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const loading = ref(true);
const posts = ref([]);
const categories = ref([]);
const selectedCategory = ref('');
const sortBy = ref('newest');
const currentPage = ref(1);
const pageSize = 10;

// 获取文章列表
const fetchPosts = async () => {
  loading.value = true;
  
  try {
    // 从路由查询参数中获取筛选条件
    const categoryId = route.query.category || selectedCategory.value;
    const sort = route.query.sort || sortBy.value;
    const page = Number(route.query.page) || currentPage.value;
    
    selectedCategory.value = categoryId;
    sortBy.value = sort;
    currentPage.value = page;
    
    // 更新 URL 参数
    router.push({
      query: {
        ...(categoryId && { category: categoryId }),
        ...(sort !== 'newest' && { sort }),
        ...(page > 1 && { page })
      }
    });
    
    // 模拟 API 请求
    await new Promise(resolve => setTimeout(resolve, 800)); // 模拟加载延迟
    
    // 模拟文章列表数据
    const mockPosts = Array(10).fill(null).map((_, index) => ({
      id: index + 1,
      title: `文章标题 ${index + 1}`,
      summary: `这是文章 ${index + 1} 的摘要，简要描述了文章的主要内容和观点。摘要应该简洁明了，让读者快速了解文章的核心内容。`,
      coverImage: null, // 使用默认封面
      createdAt: new Date(Date.now() - Math.random() * 10000000000).toISOString(),
      viewCount: Math.floor(Math.random() * 1000),
      comments: Array(Math.floor(Math.random() * 10)),
      author: {
        id: 1,
        nickname: '示例作者',
        avatar: null // 使用默认头像
      },
      tags: [
        { id: 1, name: 'Vue' },
        { id: 2, name: 'JavaScript' },
        { id: 3, name: 'Nuxt' }
      ]
    }));
    
    posts.value = mockPosts;
  } catch (error) {
    console.error('获取文章列表失败:', error);
    posts.value = [];
  } finally {
    loading.value = false;
  }
};

// 获取分类列表
const fetchCategories = async () => {
  try {
    // 模拟 API 请求
    await new Promise(resolve => setTimeout(resolve, 500));
    
    // 模拟分类数据
    categories.value = [
      { id: 1, name: '前端开发' },
      { id: 2, name: '后端开发' },
      { id: 3, name: '移动开发' },
      { id: 4, name: '人工智能' },
      { id: 5, name: '云计算' }
    ];
  } catch (error) {
    console.error('获取分类失败:', error);
    categories.value = [];
  }
};

// 重置筛选条件
const resetFilters = () => {
  selectedCategory.value = '';
  sortBy.value = 'newest';
  currentPage.value = 1;
  
  router.push({ query: {} });
  fetchPosts();
};

// 分页功能
const previousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    fetchPosts();
  }
};

const nextPage = () => {
  if (posts.value.length >= pageSize) {
    currentPage.value++;
    fetchPosts();
  }
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  const now = new Date();
  const diff = now - date;
  
  // 1分钟内
  if (diff < 60 * 1000) {
    return '刚刚';
  }
  
  // 1小时内
  if (diff < 60 * 60 * 1000) {
    return `${Math.floor(diff / (60 * 1000))}分钟前`;
  }
  
  // 24小时内
  if (diff < 24 * 60 * 60 * 1000) {
    return `${Math.floor(diff / (60 * 60 * 1000))}小时前`;
  }
  
  // 30天内
  if (diff < 30 * 24 * 60 * 60 * 1000) {
    return `${Math.floor(diff / (24 * 60 * 60 * 1000))}天前`;
  }
  
  // 年月日格式
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  
  // 如果是当年，只显示月日
  if (year === now.getFullYear()) {
    return `${month}月${day}日`;
  }
  
  return `${year}年${month}月${day}日`;
};

// 监听筛选条件变化
watchEffect(() => {
  if (selectedCategory.value || sortBy.value !== 'newest' || currentPage.value > 1) {
    fetchPosts();
  }
});

onMounted(() => {
  fetchCategories();
  fetchPosts();
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