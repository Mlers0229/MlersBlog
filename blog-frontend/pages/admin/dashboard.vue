<template>
  <div>
    <h1 class="text-2xl font-bold text-gray-800 mb-6">管理控制台</h1>
    
    <!-- 统计卡片 -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <!-- 总访问量 -->
      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-500">总访问量</h3>
          <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
          </svg>
        </div>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.totalViews.toLocaleString() }}</p>
        <p class="flex items-center text-sm text-green-600 mt-1">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18" />
          </svg>
          {{ stats.viewsGrowth }}% 较上月
        </p>
      </div>
      
      <!-- 总用户数 -->
      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-500">总用户数</h3>
          <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
          </svg>
        </div>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.totalUsers.toLocaleString() }}</p>
        <p class="flex items-center text-sm text-green-600 mt-1">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18" />
          </svg>
          {{ stats.usersGrowth }}% 较上月
        </p>
      </div>
      
      <!-- 文章数量 -->
      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-500">文章数量</h3>
          <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-amber-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
          </svg>
        </div>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.totalPosts.toLocaleString() }}</p>
        <p class="flex items-center text-sm text-green-600 mt-1">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18" />
          </svg>
          {{ stats.postsGrowth }}% 较上月
        </p>
      </div>
      
      <!-- 评论数量 -->
      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-gray-500">评论数量</h3>
          <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
          </svg>
        </div>
        <p class="text-3xl font-bold text-gray-800 mt-2">{{ stats.totalComments.toLocaleString() }}</p>
        <p class="flex items-center text-sm text-green-600 mt-1">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18" />
          </svg>
          {{ stats.commentsGrowth }}% 较上月
        </p>
      </div>
    </div>
    
    <!-- 访问趋势图表 -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-medium text-gray-800 mb-4 flex justify-between items-center">
          <span>访问趋势</span>
          <div class="flex space-x-2">
            <button 
              @click="changeChartPeriod('visits', 'week')" 
              class="text-xs px-2 py-1 rounded"
              :class="chartPeriods.visits === 'week' ? 'bg-rose-100 text-rose-600' : 'bg-gray-100 text-gray-600'"
            >
              周
            </button>
            <button 
              @click="changeChartPeriod('visits', 'month')" 
              class="text-xs px-2 py-1 rounded"
              :class="chartPeriods.visits === 'month' ? 'bg-rose-100 text-rose-600' : 'bg-gray-100 text-gray-600'"
            >
              月
            </button>
            <button 
              @click="changeChartPeriod('visits', 'year')" 
              class="text-xs px-2 py-1 rounded"
              :class="chartPeriods.visits === 'year' ? 'bg-rose-100 text-rose-600' : 'bg-gray-100 text-gray-600'"
            >
              年
            </button>
          </div>
        </h3>
        <div class="relative" style="height: 280px;">
          <canvas ref="visitsChart"></canvas>
        </div>
      </div>
      
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-medium text-gray-800 mb-4 flex justify-between items-center">
          <span>用户增长</span>
          <div class="flex space-x-2">
            <button 
              @click="changeChartPeriod('users', 'week')" 
              class="text-xs px-2 py-1 rounded"
              :class="chartPeriods.users === 'week' ? 'bg-rose-100 text-rose-600' : 'bg-gray-100 text-gray-600'"
            >
              周
            </button>
            <button 
              @click="changeChartPeriod('users', 'month')" 
              class="text-xs px-2 py-1 rounded"
              :class="chartPeriods.users === 'month' ? 'bg-rose-100 text-rose-600' : 'bg-gray-100 text-gray-600'"
            >
              月
            </button>
            <button 
              @click="changeChartPeriod('users', 'year')" 
              class="text-xs px-2 py-1 rounded"
              :class="chartPeriods.users === 'year' ? 'bg-rose-100 text-rose-600' : 'bg-gray-100 text-gray-600'"
            >
              年
            </button>
          </div>
        </h3>
        <div class="relative" style="height: 280px;">
          <canvas ref="usersChart"></canvas>
        </div>
      </div>
    </div>
    
    <!-- 内容分析和热门文章 -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-medium text-gray-800 mb-4">内容发布分析</h3>
        <div class="relative" style="height: 280px;">
          <canvas ref="contentChart"></canvas>
        </div>
      </div>
      
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-medium text-gray-800 mb-4">热门内容</h3>
        <div class="relative" style="height: 280px;">
          <canvas ref="popularContentChart"></canvas>
        </div>
      </div>
    </div>

    <!-- 用户活跃度和设备分析 -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-medium text-gray-800 mb-4">用户活跃度</h3>
        <div class="relative" style="height: 280px;">
          <canvas ref="userActivityChart"></canvas>
        </div>
      </div>
      
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-medium text-gray-800 mb-4">访问设备分析</h3>
        <div class="relative" style="height: 280px;">
          <canvas ref="deviceChart"></canvas>
        </div>
      </div>
    </div>

    <!-- 内容分析和待审核内容 -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- 内容分布 -->
      <div class="bg-white rounded-lg shadow-sm p-6">
        <h3 class="text-lg font-medium text-gray-800 mb-4">内容分布</h3>
        <div class="grid grid-cols-1 gap-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <div class="w-3 h-3 bg-blue-500 rounded-full mr-2"></div>
              <span class="text-gray-700">文章</span>
            </div>
            <div class="flex items-center">
              <span class="text-gray-800 font-medium">{{ stats.totalPosts.toLocaleString() }}</span>
              <span class="text-gray-500 text-sm ml-2">({{ Math.round(stats.totalPosts / (stats.totalPosts + stats.totalComments) * 100) }}%)</span>
            </div>
          </div>
          
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <div class="w-3 h-3 bg-green-500 rounded-full mr-2"></div>
              <span class="text-gray-700">评论</span>
            </div>
            <div class="flex items-center">
              <span class="text-gray-800 font-medium">{{ stats.totalComments.toLocaleString() }}</span>
              <span class="text-gray-500 text-sm ml-2">({{ Math.round(stats.totalComments / (stats.totalPosts + stats.totalComments) * 100) }}%)</span>
            </div>
          </div>
          
          <div class="flex items-center justify-between mt-2">
            <div class="flex items-center">
              <div class="w-3 h-3 bg-rose-500 rounded-full mr-2"></div>
              <span class="text-gray-700">用户</span>
            </div>
            <div class="flex items-center">
              <span class="text-gray-800 font-medium">{{ stats.totalUsers.toLocaleString() }}</span>
            </div>
          </div>
          
          <div class="mt-4">
            <h4 class="text-sm font-medium text-gray-500 mb-2">内容类型分布</h4>
            <div class="w-full bg-gray-200 rounded-full h-2.5">
              <div class="bg-blue-500 h-2.5 rounded-full" style="width: 45%"></div>
            </div>
            <div class="flex justify-between mt-2 text-xs text-gray-500">
              <span>文章</span>
              <span>评论</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 待审核内容 -->
      <div class="bg-white rounded-lg shadow-sm p-6">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-medium text-gray-800">待审核内容</h3>
          <div>
            <button class="text-sm text-blue-600 hover:text-blue-800">查看全部</button>
          </div>
        </div>
        
        <div class="space-y-3">
          <div v-if="pendingItems.length === 0" class="text-center py-8 text-gray-500">
            目前没有待审核的内容
          </div>
          
          <div 
            v-for="(item, index) in pendingItems" 
            :key="index"
            class="flex items-start p-3 border border-gray-100 rounded-lg"
          >
            <div class="flex-shrink-0 mt-1">
              <div 
                class="w-2 h-2 rounded-full mr-3" 
                :class="{
                  'bg-amber-500': item.type === 'post',
                  'bg-green-500': item.type === 'comment'
                }"
              ></div>
            </div>
            <div class="flex-grow">
              <div class="flex items-center">
                <span 
                  class="text-xs rounded px-2 py-0.5 mr-2" 
                  :class="{
                    'bg-amber-100 text-amber-800': item.type === 'post',
                    'bg-green-100 text-green-800': item.type === 'comment'
                  }"
                >
                  {{ item.type === 'post' ? '文章' : '评论' }}
                </span>
                <span class="text-sm text-gray-500">{{ item.createdAt }}</span>
              </div>
              <p class="text-sm text-gray-800 mt-1">{{ item.content }}</p>
              <div class="flex items-center mt-2">
                <button class="text-xs bg-rose-100 text-rose-600 rounded-full px-3 py-1 mr-2 hover:bg-rose-200">
                  拒绝
                </button>
                <button class="text-xs bg-green-100 text-green-600 rounded-full px-3 py-1 hover:bg-green-200">
                  通过
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useAuthStore } from '@/stores/auth';
import Chart from 'chart.js/auto';

const authStore = useAuthStore();
const visitsChart = ref(null);
const usersChart = ref(null);
const contentChart = ref(null);
const popularContentChart = ref(null);
const userActivityChart = ref(null);
const deviceChart = ref(null);

// 图表实例
const charts = {
  visits: null,
  users: null,
  content: null,
  popularContent: null,
  userActivity: null,
  device: null
};

// 图表周期
const chartPeriods = ref({
  visits: 'month',
  users: 'month'
});

// 改变图表周期
const changeChartPeriod = (chartType, period) => {
  chartPeriods.value[chartType] = period;
  
  if (chartType === 'visits') {
    initVisitsChart();
  } else if (chartType === 'users') {
    initUsersChart();
  }
};

// 自动刷新数据的定时器
let refreshTimer = null;

// 统计数据
const stats = ref({
  totalViews: 15672,
  viewsGrowth: 12.5,
  totalUsers: 843,
  usersGrowth: 8.3,
  totalPosts: 126,
  postsGrowth: 4.2,
  totalComments: 538,
  commentsGrowth: 15.7
});

// 待审核内容
const pendingItems = ref([
  {
    type: 'post',
    content: '如何优化Vue.js应用性能的10个技巧',
    createdAt: '今天 14:23',
    author: '张三'
  },
  {
    type: 'comment',
    content: '这篇文章很有帮助，感谢分享！',
    createdAt: '今天 12:05',
    author: '李四'
  },
  {
    type: 'post',
    content: 'TypeScript高级类型体操详解与应用',
    createdAt: '昨天 18:30',
    author: '王五'
  }
]);

// 生成周期数据标签
const generateLabels = (period) => {
  const labels = [];
  const now = new Date();
  
  if (period === 'week') {
    // 最近7天
    for (let i = 6; i >= 0; i--) {
      const date = new Date(now);
      date.setDate(date.getDate() - i);
      labels.push(date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' }));
    }
  } else if (period === 'month') {
    // 最近30天，每3天一个点
    for (let i = 29; i >= 0; i -= 3) {
      const date = new Date(now);
      date.setDate(date.getDate() - i);
      labels.push(date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' }));
    }
  } else if (period === 'year') {
    // 过去12个月
    for (let i = 11; i >= 0; i--) {
      const date = new Date(now);
      date.setMonth(date.getMonth() - i);
      labels.push(date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'short' }));
    }
  }
  
  return labels;
};

// 生成随机数据
const generateRandomData = (min, max, count) => {
  return Array.from({ length: count }, () => Math.floor(Math.random() * (max - min + 1)) + min);
};

// 初始化访问趋势图表
const initVisitsChart = () => {
  const ctx = visitsChart.value.getContext('2d');
  const period = chartPeriods.value.visits;
  const labels = generateLabels(period);
  
  // 生成不同周期的数据点数量
  const dataPointCount = period === 'week' ? 7 : period === 'month' ? 10 : 12;
  
  // 生成随机数据 - 模拟访问数据
  const pageViewsData = generateRandomData(1000, 5000, dataPointCount);
  const uniqueVisitorsData = generateRandomData(500, 2500, dataPointCount);
  
  // 销毁旧图表
  if (charts.visits) {
    charts.visits.destroy();
  }
  
  // 创建新图表
  charts.visits = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [
        {
          label: '页面浏览量',
          data: pageViewsData,
          borderColor: 'rgb(59, 130, 246)',
          backgroundColor: 'rgba(59, 130, 246, 0.1)',
          borderWidth: 2,
          tension: 0.3,
          fill: true
        },
        {
          label: '独立访客',
          data: uniqueVisitorsData,
          borderColor: 'rgb(16, 185, 129)',
          backgroundColor: 'rgba(16, 185, 129, 0.1)',
          borderWidth: 2,
          tension: 0.3,
          fill: true
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top',
        },
        tooltip: {
          mode: 'index',
          intersect: false
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          grid: {
            drawBorder: false,
            color: 'rgba(0, 0, 0, 0.05)'
          }
        },
        x: {
          grid: {
            display: false
          }
        }
      }
    }
  });
};

// 初始化用户增长图表
const initUsersChart = () => {
  const ctx = usersChart.value.getContext('2d');
  const period = chartPeriods.value.users;
  const labels = generateLabels(period);
  
  // 生成不同周期的数据点数量
  const dataPointCount = period === 'week' ? 7 : period === 'month' ? 10 : 12;
  
  // 生成随机数据 - 模拟用户增长数据
  const newUsersData = generateRandomData(10, 50, dataPointCount);
  const totalUsersData = [];
  let totalUsers = stats.value.totalUsers - newUsersData.reduce((a, b) => a + b, 0);
  
  for (const newUsers of newUsersData) {
    totalUsers += newUsers;
    totalUsersData.push(totalUsers);
  }
  
  // 销毁旧图表
  if (charts.users) {
    charts.users.destroy();
  }
  
  // 创建新图表
  charts.users = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [
        {
          label: '总用户数',
          data: totalUsersData,
          borderColor: 'rgb(244, 63, 94)',
          backgroundColor: 'rgba(244, 63, 94, 0.1)',
          borderWidth: 2,
          tension: 0.3,
          fill: true,
          yAxisID: 'y'
        },
        {
          label: '新增用户',
          data: newUsersData,
          borderColor: 'rgb(251, 146, 60)',
          backgroundColor: 'rgb(251, 146, 60)',
          borderWidth: 0,
          tension: 0,
          type: 'bar',
          yAxisID: 'y1'
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top',
        },
        tooltip: {
          mode: 'index',
          intersect: false
        }
      },
      scales: {
        y: {
          type: 'linear',
          position: 'left',
          beginAtZero: false,
          grid: {
            drawBorder: false,
            color: 'rgba(0, 0, 0, 0.05)'
          }
        },
        y1: {
          type: 'linear',
          position: 'right',
          beginAtZero: true,
          grid: {
            drawOnChartArea: false,
          },
          ticks: {
            stepSize: 10
          }
        },
        x: {
          grid: {
            display: false
          }
        }
      }
    }
  });
};

// 初始化内容发布分析图表 (折线图)
const initContentChart = () => {
  const ctx = contentChart.value.getContext('2d');
  
  // 生成最近12个月的标签
  const labels = generateLabels('year');
  
  // 生成随机数据 - 模拟文章和评论发布数据
  const postsData = generateRandomData(5, 20, 12);
  const commentsData = generateRandomData(10, 50, 12);
  
  charts.content = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [
        {
          label: '发布文章数',
          data: postsData,
          borderColor: 'rgb(234, 179, 8)',
          backgroundColor: 'rgba(234, 179, 8, 0.5)',
          borderWidth: 2,
          tension: 0.3
        },
        {
          label: '评论数',
          data: commentsData,
          borderColor: 'rgb(34, 197, 94)',
          backgroundColor: 'rgba(34, 197, 94, 0.5)',
          borderWidth: 2,
          tension: 0.3
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top',
        },
        tooltip: {
          mode: 'index',
          intersect: false
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          grid: {
            drawBorder: false,
            color: 'rgba(0, 0, 0, 0.05)'
          }
        },
        x: {
          grid: {
            display: false
          }
        }
      }
    }
  });
};

// 初始化热门内容图表 (水平条形图)
const initPopularContentChart = () => {
  const ctx = popularContentChart.value.getContext('2d');
  
  // 模拟热门文章数据
  const topPosts = [
    { title: '10个提高代码质量的小技巧', views: 1245 },
    { title: 'Vue 3实战教程：从零搭建博客系统', views: 986 },
    { title: '后端开发者必须了解的设计模式', views: 879 },
    { title: '如何优化网站性能并提高用户体验', views: 765 },
    { title: '前端开发必备的CSS技巧', views: 682 }
  ];
  
  charts.popularContent = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: topPosts.map(post => post.title),
      datasets: [
        {
          axis: 'y',
          label: '浏览量',
          data: topPosts.map(post => post.views),
          backgroundColor: [
            'rgba(244, 63, 94, 0.8)',
            'rgba(234, 179, 8, 0.8)',
            'rgba(16, 185, 129, 0.8)',
            'rgba(59, 130, 246, 0.8)',
            'rgba(139, 92, 246, 0.8)'
          ],
          borderWidth: 0
        }
      ]
    },
    options: {
      indexAxis: 'y',
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return `浏览量: ${context.raw.toLocaleString()}`;
            }
          }
        }
      },
      scales: {
        x: {
          beginAtZero: true,
          grid: {
            drawBorder: false,
            color: 'rgba(0, 0, 0, 0.05)'
          }
        },
        y: {
          grid: {
            display: false
          }
        }
      }
    }
  });
};

// 初始化用户活跃度图表 (热力图)
const initUserActivityChart = () => {
  const ctx = userActivityChart.value.getContext('2d');
  
  // 生成一周的活跃时间热力图数据
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
  const hours = Array.from({length: 24}, (_, i) => `${i}:00`);
  
  // 按小时生成一周的活跃度数据（模拟）
  const activityData = [];
  
  for (let i = 0; i < 7; i++) {
    for (let j = 0; j < 24; j++) {
      // 工作日白天活跃度更高，周末晚上活跃度更高
      let value;
      if (i < 5) { // 工作日
        if (j >= 9 && j <= 18) { // 白天工作时间
          value = Math.floor(Math.random() * 30) + 70; // 70-100
        } else if ((j >= 7 && j < 9) || (j > 18 && j <= 23)) { // 早晨和晚上
          value = Math.floor(Math.random() * 40) + 30; // 30-70
        } else { // 深夜
          value = Math.floor(Math.random() * 30); // 0-30
        }
      } else { // 周末
        if (j >= 10 && j <= 23) { // 白天和晚上
          value = Math.floor(Math.random() * 50) + 30; // 30-80
        } else { // 深夜
          value = Math.floor(Math.random() * 30); // 0-30
        }
      }
      
      activityData.push({
        day: i,
        hour: j,
        value: value
      });
    }
  }
  
  // 创建热力图
  charts.userActivity = new Chart(ctx, {
    type: 'scatter',
    data: {
      datasets: [{
        label: '用户活跃度',
        data: activityData.map(data => ({
          x: data.hour,
          y: data.day,
          v: data.value
        })),
        backgroundColor: function(context) {
          const value = context.raw.v;
          if (value < 20) return 'rgba(237, 233, 254, 0.8)'; // 低活跃度
          if (value < 40) return 'rgba(221, 214, 254, 0.8)'; 
          if (value < 60) return 'rgba(196, 181, 253, 0.8)';
          if (value < 80) return 'rgba(167, 139, 250, 0.8)';
          return 'rgba(139, 92, 246, 0.8)'; // 高活跃度
        },
        pointRadius: 12,
        pointHoverRadius: 15
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        tooltip: {
          callbacks: {
            label: function(context) {
              const day = days[context.raw.y];
              const hour = hours[context.raw.x];
              return `${day} ${hour} - 活跃度: ${context.raw.v}%`;
            }
          }
        },
        legend: {
          display: false
        }
      },
      scales: {
        y: {
          ticks: {
            callback: function(value) {
              return days[value];
            }
          },
          grid: {
            display: false
          },
          reverse: true
        },
        x: {
          ticks: {
            stepSize: 3,
            callback: function(value) {
              return `${value}:00`;
            }
          },
          grid: {
            display: false
          }
        }
      }
    }
  });
};

// 初始化设备分析图表 (饼图)
const initDeviceChart = () => {
  const ctx = deviceChart.value.getContext('2d');
  
  // 模拟设备分布数据
  const deviceData = [
    { device: '手机', percentage: 62 },
    { device: '桌面电脑', percentage: 26 },
    { device: '平板', percentage: 9 },
    { device: '其他', percentage: 3 }
  ];
  
  charts.device = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: deviceData.map(d => d.device),
      datasets: [{
        label: '访问设备',
        data: deviceData.map(d => d.percentage),
        backgroundColor: [
          'rgba(244, 63, 94, 0.8)',
          'rgba(59, 130, 246, 0.8)',
          'rgba(16, 185, 129, 0.8)',
          'rgba(107, 114, 128, 0.8)'
        ],
        borderWidth: 0,
        hoverOffset: 15
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'right'
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return `${context.label}: ${context.raw}%`;
            }
          }
        }
      }
    }
  });
};

// 刷新数据
const refreshData = async () => {
  try {
    // 模拟API调用获取新数据
    // 这里可以添加实际的API调用
    console.log('刷新数据...');
    
    // 随机小幅度变化统计数据，模拟实时更新
    stats.value.totalViews += Math.floor(Math.random() * 10);
    stats.value.totalUsers += Math.floor(Math.random() * 2);
    stats.value.totalPosts += Math.random() > 0.8 ? 1 : 0;
    stats.value.totalComments += Math.floor(Math.random() * 3);
    
    // 更新图表 (可以根据需要选择性更新)
    initVisitsChart();
    initUsersChart();
  } catch (error) {
    console.error('刷新数据失败:', error);
  }
};

// 初始化
onMounted(() => {
  initVisitsChart();
  initUsersChart();
  initContentChart();
  initPopularContentChart();
  initUserActivityChart();
  initDeviceChart();
  
  // 设置自动刷新（每60秒）
  refreshTimer = setInterval(refreshData, 60000);
});

// 组件销毁前清理
onBeforeUnmount(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer);
  }
  
  // 销毁所有图表
  Object.values(charts).forEach(chart => {
    if (chart) {
      chart.destroy();
    }
  });
});
</script>

<style scoped>
.loader {
  border: 3px solid #f3f3f3;
  border-radius: 50%;
  border-top: 3px solid #3498db;
  width: 24px;
  height: 24px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 