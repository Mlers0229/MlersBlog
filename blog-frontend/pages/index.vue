<template>
  <div>
    <!-- 头部横幅 -->
    <div class="relative mb-8 bg-gradient-to-r from-rose-50 to-indigo-50 rounded-lg overflow-hidden">
      <div class="px-8 py-16 relative z-10 flex flex-col items-center text-center">
        <h1 class="text-4xl md:text-5xl font-bold mb-4 text-gray-800">欢迎来到我的博客</h1>
        <p class="text-xl text-gray-600 mb-8 max-w-2xl">
          分享编程、设计和生活中的点滴思考
        </p>
        <button class="px-6 py-3 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors shadow-md">
          开始阅读
        </button>
      </div>
      
      <!-- 装饰元素 -->
      <div class="absolute top-0 left-0 w-full h-full overflow-hidden opacity-10">
        <div class="absolute -top-10 -left-10 w-40 h-40 rounded-full bg-rose-400"></div>
        <div class="absolute bottom-10 right-10 w-24 h-24 rounded-full bg-indigo-400"></div>
        <div class="absolute top-1/2 left-1/4 w-16 h-16 rounded-full bg-amber-400"></div>
      </div>
    </div>
    
    <!-- 文章筛选 -->
    <div class="mb-8 flex flex-wrap justify-between items-center">
      <div class="flex flex-wrap gap-2 mb-4 md:mb-0">
        <button 
          v-for="category in ['全部', '技术', '设计', '生活']" 
          :key="category"
          @click="setActiveCategory(category)"
          :class="[
            'px-4 py-2 rounded-full', 
            activeCategory === category 
              ? 'bg-rose-500 text-white' 
              : 'bg-white text-gray-600 hover:bg-gray-100'
          ]"
        >
          {{ category }}
        </button>
      </div>
      
      <div class="relative">
        <select 
          v-model="sortBy"
          class="appearance-none bg-white border border-gray-200 text-gray-700 py-2 px-4 pr-8 rounded-full leading-tight focus:outline-none focus:ring-2 focus:ring-rose-300"
        >
          <option value="latest">最新发布</option>
          <option value="views">最多阅读</option>
          <option value="comments">最多评论</option>
        </select>
        <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
          </svg>
        </div>
      </div>
    </div>
    
    <!-- 文章列表 -->
    <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div v-for="i in 4" :key="i" class="animate-pulse">
        <div class="bg-white rounded-lg shadow-sm overflow-hidden">
          <div class="aspect-w-16 aspect-h-9 bg-gray-200"></div>
          <div class="p-6">
            <div class="flex justify-between items-center mb-3">
              <div class="h-4 bg-gray-200 rounded w-1/4"></div>
              <div class="h-4 bg-gray-200 rounded w-1/4"></div>
            </div>
            <div class="h-6 bg-gray-200 rounded w-3/4 mb-3"></div>
            <div class="space-y-2 mb-4">
              <div class="h-4 bg-gray-200 rounded"></div>
              <div class="h-4 bg-gray-200 rounded"></div>
              <div class="h-4 bg-gray-200 rounded w-5/6"></div>
            </div>
            <div class="flex justify-between items-center">
              <div class="flex items-center">
                <div class="h-8 w-8 bg-gray-200 rounded-full mr-2"></div>
                <div class="h-4 bg-gray-200 rounded w-20"></div>
              </div>
              <div class="flex space-x-3">
                <div class="h-4 bg-gray-200 rounded w-8"></div>
                <div class="h-4 bg-gray-200 rounded w-8"></div>
                <div class="h-4 bg-gray-200 rounded w-8"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else-if="filteredPosts.length === 0" class="text-center py-16 bg-white rounded-lg shadow-sm">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1M19 8l-7 7-7-7" />
      </svg>
      <h3 class="mt-4 text-xl font-medium text-gray-800">暂无文章</h3>
      <p class="mt-2 text-gray-600">当前分类下暂无文章，请尝试其他分类</p>
    </div>
    
    <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <PostCard v-for="post in filteredPosts" :key="post.id" :post="post" />
    </div>
    
    <!-- 分页 -->
    <div v-if="!loading && totalPages > 1" class="mt-10 flex justify-center">
      <nav class="flex items-center">
        <button 
          @click="prevPage" 
          :disabled="currentPage === 1"
          :class="[
            'px-4 py-2 rounded-l-md border border-gray-300', 
            currentPage === 1 
              ? 'bg-gray-100 text-gray-400 cursor-not-allowed' 
              : 'bg-white text-gray-700 hover:bg-gray-50'
          ]"
        >
          上一页
        </button>
        
        <div v-for="page in displayedPages" :key="page" class="hidden md:block">
          <button 
            v-if="page !== '...'"
            @click="goToPage(page)" 
            :class="[
              'px-4 py-2 border-t border-b border-gray-300', 
              currentPage === page 
                ? 'bg-rose-500 text-white border-rose-500' 
                : 'bg-white text-gray-700 hover:bg-gray-50'
            ]"
          >
            {{ page }}
          </button>
          <span v-else class="px-4 py-2 border-t border-b border-gray-300 bg-white text-gray-700">
            {{ page }}
          </span>
        </div>
        
        <button 
          @click="nextPage" 
          :disabled="currentPage === totalPages"
          :class="[
            'px-4 py-2 rounded-r-md border border-gray-300', 
            currentPage === totalPages 
              ? 'bg-gray-100 text-gray-400 cursor-not-allowed' 
              : 'bg-white text-gray-700 hover:bg-gray-50'
          ]"
        >
          下一页
        </button>
      </nav>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';

// 状态管理
const loading = ref(true);
const posts = ref([]);
const activeCategory = ref('全部');
const sortBy = ref('latest');
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = ref(10);

// 获取文章列表
const fetchPosts = async () => {
  loading.value = true;
  
  try {
    // 调用API获取文章列表
    // const response = await $fetch('/api/posts', {
    //   method: 'GET',
    //   query: {
    //     page: currentPage.value - 1, // 后端通常是0-indexed
    //     size: pageSize.value,
    //     sort: getSortParam(),
    //     category: activeCategory.value === '全部' ? '' : activeCategory.value
    //   }
    // });
    
    // if (response.code === 200) {
    //   posts.value = response.data.content;
    //   totalPages.value = response.data.totalPages;
    // }
    
    // 模拟数据
    setTimeout(() => {
      const mockPosts = Array.from({ length: 10 }, (_, i) => ({
        id: i + 1,
        title: '如何使用Nuxt 3构建高性能博客系统' + (i + 1),
        content: '这是一篇介绍如何使用Nuxt 3构建现代化博客系统的文章。Nuxt 3是Vue.js最新的服务器端渲染框架，它提供了许多强大的功能，如自动代码分割、静态站点生成、服务器端渲染等。本文将详细介绍如何从零开始搭建一个Nuxt 3博客系统，包括项目初始化、路由配置、数据获取、页面组件开发等内容。',
        summary: '这是一篇介绍如何使用Nuxt 3构建现代化博客系统的文章。',
        coverImage: i % 3 === 0 ? 'https://picsum.photos/id/' + (i + 30) + '/800/450' : '',
        createdAt: new Date(Date.now() - i * 86400000).toISOString(),
        views: Math.floor(Math.random() * 1000),
        comments: Math.floor(Math.random() * 50),
        likes: Math.floor(Math.random() * 100),
        category: ['技术', '设计', '生活'][i % 3],
        author: {
          name: '博主',
          avatar: 'https://randomuser.me/api/portraits/men/1.jpg'
        }
      }));
      
      posts.value = mockPosts;
      totalPages.value = 5;
      loading.value = false;
    }, 800);
    
  } catch (error) {
    console.error('获取文章列表失败:', error);
    // 使用模拟数据
    posts.value = [];
    totalPages.value = 0;
  } finally {
    if (process.client) {
      setTimeout(() => {
        loading.value = false;
      }, 800);
    }
  }
};

// 获取排序参数
const getSortParam = () => {
  switch (sortBy.value) {
    case 'views':
      return 'views,desc';
    case 'comments':
      return 'comments,desc';
    case 'latest':
    default:
      return 'createdAt,desc';
  }
};

// 根据分类和排序过滤文章
const filteredPosts = computed(() => {
  let result = [...posts.value];
  
  // 分类过滤
  if (activeCategory.value !== '全部') {
    result = result.filter(post => post.category === activeCategory.value);
  }
  
  // 排序
  result.sort((a, b) => {
    switch (sortBy.value) {
      case 'views':
        return b.views - a.views;
      case 'comments':
        return b.comments - a.comments;
      case 'latest':
      default:
        return new Date(b.createdAt) - new Date(a.createdAt);
    }
  });
  
  return result;
});

// 分页计算逻辑
const displayedPages = computed(() => {
  const pages = [];
  const maxVisiblePages = 5;
  
  if (totalPages.value <= maxVisiblePages) {
    // 总页数较少，全部显示
    for (let i = 1; i <= totalPages.value; i++) {
      pages.push(i);
    }
  } else {
    // 总页数较多，显示部分页码
    if (currentPage.value <= 3) {
      // 当前页靠前
      for (let i = 1; i <= 4; i++) {
        pages.push(i);
      }
      pages.push('...');
      pages.push(totalPages.value);
    } else if (currentPage.value >= totalPages.value - 2) {
      // 当前页靠后
      pages.push(1);
      pages.push('...');
      for (let i = totalPages.value - 3; i <= totalPages.value; i++) {
        pages.push(i);
      }
    } else {
      // 当前页在中间
      pages.push(1);
      pages.push('...');
      for (let i = currentPage.value - 1; i <= currentPage.value + 1; i++) {
        pages.push(i);
      }
      pages.push('...');
      pages.push(totalPages.value);
    }
  }
  
  return pages;
});

// 分页方法
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const goToPage = (page) => {
  currentPage.value = page;
};

// 设置活动分类
const setActiveCategory = (category) => {
  if (activeCategory.value !== category) {
    activeCategory.value = category;
    currentPage.value = 1;
  }
};

// 监听过滤和排序条件变化
watch([sortBy], () => {
  if (currentPage.value !== 1) {
    currentPage.value = 1;
  } else {
    fetchPosts();
  }
});

// 监听页码变化
watch(currentPage, () => {
  fetchPosts();
  // 滚动到页面顶部
  if (process.client) {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
});

// 组件挂载后获取数据
onMounted(() => {
  if (process.client) {
    fetchPosts();
  }
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
</style> 