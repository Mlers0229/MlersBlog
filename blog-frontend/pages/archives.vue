<template>
  <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
      文章归档
    </h1>
    
    <div v-if="loading" class="space-y-8">
      <div v-for="i in 2" :key="i" class="animate-pulse">
        <div class="h-6 bg-gray-200 rounded w-1/4 mb-4"></div>
        <div class="border-l-2 border-gray-200 pl-4 ml-2 space-y-6">
          <div v-for="j in 2" :key="j">
            <div class="h-5 bg-gray-200 rounded w-1/6 mb-3"></div>
            <div class="space-y-3">
              <div v-for="k in 3" :key="k" class="flex items-center">
                <div class="w-24 h-4 bg-gray-200 rounded mr-2"></div>
                <div class="h-4 bg-gray-200 rounded w-1/2"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else-if="Object.keys(archiveData).length === 0" class="text-center py-16">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
      <h3 class="mt-4 text-xl font-medium text-gray-800">暂无文章</h3>
      <p class="mt-2 text-gray-600">当有发布的文章时，将在这里显示归档</p>
    </div>
    
    <div v-else class="space-y-8">
      <div v-for="(year, yearKey) in archiveData" :key="yearKey">
        <h2 class="text-xl font-bold text-gray-800 mb-4">{{ yearKey }}年</h2>
        <div class="border-l-2 border-rose-300 pl-4 ml-2 space-y-6">
          <div v-for="(month, monthKey) in year" :key="`${yearKey}-${monthKey}`">
            <h3 class="text-lg font-medium text-gray-800 mb-2">{{ monthNameMap[monthKey-1] }}</h3>
            <div class="space-y-3">
              <div v-for="post in month" :key="post.id" class="flex items-center">
                <span class="text-gray-500 text-sm w-24">{{ formatDay(post.createTime || post.createdAt) }}</span>
                <NuxtLink :to="`/post/${post.id}`" class="text-gray-700 hover:text-rose-500">{{ post.title }}</NuxtLink>
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
import { format, parseISO } from 'date-fns';
import { zhCN } from 'date-fns/locale';

const loading = ref(true);
const archiveData = ref({});
const monthNameMap = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];

// 格式化日期，只显示日
const formatDay = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  return `${date.getMonth() + 1}月${date.getDate()}日`;
};

// 获取归档数据
const fetchArchiveData = async () => {
  loading.value = true;
  
  try {
    const response = await $fetch('/api/posts/archive', {
      method: 'GET'
    });
    
    if (response.code === 200) {
      archiveData.value = organizeArchiveData(response.data);
    }
  } catch (error) {
    console.error('获取归档数据失败:', error);
    
    // 使用模拟数据
    setTimeout(() => {
      const mockPosts = [
        {
          id: 1,
          title: '如何使用Nuxt 3构建高性能博客',
          createTime: '2023-12-15T10:30:00Z'
        },
        {
          id: 2,
          title: 'Tailwind CSS与传统CSS框架的比较',
          createTime: '2023-12-10T15:45:00Z'
        },
        {
          id: 3,
          title: '提高工作效率的10个小技巧',
          createTime: '2023-11-28T09:20:00Z'
        },
        {
          id: 4,
          title: '2023年最值得学习的编程语言',
          createTime: '2023-10-05T14:10:00Z'
        },
        {
          id: 5,
          title: '如何优化网站加载速度',
          createTime: '2022-12-20T11:40:00Z'
        },
        {
          id: 6,
          title: 'JavaScript中的设计模式解析',
          createTime: '2022-11-15T16:50:00Z'
        }
      ];
      
      archiveData.value = organizeArchiveData(mockPosts);
      loading.value = false;
    }, 1000);
  } finally {
    if (process.client) {
      setTimeout(() => {
        loading.value = false;
      }, 1000);
    }
  }
};

// 整理归档数据，按年月组织
const organizeArchiveData = (posts) => {
  const archiveMap = {};
  
  posts.forEach(post => {
    const date = new Date(post.createTime || post.createdAt);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    
    if (!archiveMap[year]) {
      archiveMap[year] = {};
    }
    
    if (!archiveMap[year][month]) {
      archiveMap[year][month] = [];
    }
    
    archiveMap[year][month].push(post);
  });
  
  // 排序：年份降序，月份降序
  const sortedArchiveMap = {};
  Object.keys(archiveMap)
    .sort((a, b) => b - a) // 年份降序
    .forEach(year => {
      sortedArchiveMap[year] = {};
      Object.keys(archiveMap[year])
        .sort((a, b) => b - a) // 月份降序
        .forEach(month => {
          sortedArchiveMap[year][month] = archiveMap[year][month];
        });
    });
  
  return sortedArchiveMap;
};

onMounted(() => {
  if (process.client) {
    fetchArchiveData();
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