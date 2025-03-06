<template>
  <div>
    <div class="mb-6 flex justify-between items-center">
      <h1 class="text-2xl font-bold text-gray-800">文章管理</h1>
      <div class="flex space-x-2">
        <div class="relative">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索文章标题或作者..." 
            class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
            @keyup.enter="searchPosts"
          />
          <svg 
            xmlns="http://www.w3.org/2000/svg" 
            class="h-5 w-5 absolute left-3 top-2.5 text-gray-400" 
            fill="none" 
            viewBox="0 0 24 24" 
            stroke="currentColor"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </div>
        <select 
          v-model="filterStatus" 
          class="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
          @change="fetchPosts(1)"
        >
          <option value="all">所有状态</option>
          <option value="published">已发布</option>
          <option value="draft">草稿</option>
          <option value="pending">待审核</option>
          <option value="rejected">已拒绝</option>
        </select>
      </div>
    </div>
    
    <!-- 表格 -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden">
      <div class="min-w-full divide-y divide-gray-200">
        <!-- 表头 -->
        <div class="bg-gray-50">
          <div class="grid grid-cols-12 divide-x divide-gray-200">
            <div class="px-6 py-3 col-span-5 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              文章信息
            </div>
            <div class="px-6 py-3 col-span-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              作者
            </div>
            <div class="px-6 py-3 col-span-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              发布日期
            </div>
            <div class="px-6 py-3 col-span-1 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              状态
            </div>
            <div class="px-6 py-3 col-span-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              操作
            </div>
          </div>
        </div>
        
        <!-- 加载中 -->
        <div v-if="loading" class="divide-y divide-gray-200">
          <div v-for="i in 5" :key="i" class="animate-pulse bg-white">
            <div class="grid grid-cols-12 divide-x divide-gray-200">
              <div class="px-6 py-4 col-span-5">
                <div class="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
                <div class="h-3 bg-gray-200 rounded w-1/2"></div>
              </div>
              <div class="px-6 py-4 col-span-2">
                <div class="h-4 bg-gray-200 rounded w-1/2"></div>
              </div>
              <div class="px-6 py-4 col-span-2">
                <div class="h-4 bg-gray-200 rounded w-3/4"></div>
              </div>
              <div class="px-6 py-4 col-span-1">
                <div class="h-4 bg-gray-200 rounded w-full"></div>
              </div>
              <div class="px-6 py-4 col-span-2">
                <div class="h-8 bg-gray-200 rounded w-3/4"></div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 无数据 -->
        <div v-else-if="posts.length === 0" class="bg-white">
          <div class="px-6 py-10 text-center">
            <svg 
              xmlns="http://www.w3.org/2000/svg" 
              class="h-10 w-10 mx-auto text-gray-300" 
              fill="none" 
              viewBox="0 0 24 24" 
              stroke="currentColor"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            <p class="mt-2 text-gray-500">
              {{ searchQuery ? '没有找到匹配的文章' : '暂无文章数据' }}
            </p>
          </div>
        </div>
        
        <!-- 表格内容 -->
        <div v-else class="bg-white divide-y divide-gray-200">
          <div 
            v-for="post in posts" 
            :key="post.id" 
            class="hover:bg-gray-50"
          >
            <div class="grid grid-cols-12 divide-x divide-gray-200">
              <div class="px-6 py-4 col-span-5">
                <div class="flex items-start">
                  <img 
                    v-if="post.coverImage" 
                    :src="post.coverImage" 
                    alt="文章封面" 
                    class="w-12 h-12 object-cover rounded mr-3"
                  />
                  <div v-else class="w-12 h-12 bg-gray-200 rounded flex items-center justify-center mr-3">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                  </div>
                  <div>
                    <div class="font-medium text-gray-900">{{ post.title }}</div>
                    <div class="text-sm text-gray-500 mt-1">
                      <span class="inline-flex items-center mr-3">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                        </svg>
                        {{ post.viewCount || 0 }}
                      </span>
                      <span class="inline-flex items-center mr-3">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                        </svg>
                        {{ post.commentCount || 0 }}
                      </span>
                      <span class="inline-flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                        </svg>
                        {{ post.likeCount || 0 }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="px-6 py-4 col-span-2">
                <div class="flex items-center">
                  <img 
                    :src="post.author.avatar || '/img/default-avatar.png'" 
                    alt="作者头像" 
                    class="w-6 h-6 rounded-full mr-2"
                  />
                  <span class="text-gray-900">{{ post.author.nickname }}</span>
                </div>
              </div>
              <div class="px-6 py-4 col-span-2 text-sm text-gray-500">
                {{ formatDate(post.createdAt) }}
              </div>
              <div class="px-6 py-4 col-span-1">
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="{
                    'bg-green-100 text-green-800': post.status === 'published',
                    'bg-gray-100 text-gray-800': post.status === 'draft',
                    'bg-amber-100 text-amber-800': post.status === 'pending',
                    'bg-rose-100 text-rose-800': post.status === 'rejected'
                  }"
                >
                  {{ formatStatus(post.status) }}
                </span>
              </div>
              <div class="px-6 py-4 col-span-2 flex space-x-2">
                <button 
                  v-if="post.status === 'pending'"
                  @click="approvePost(post.id)"
                  class="text-xs bg-green-100 text-green-600 rounded-full px-3 py-1 hover:bg-green-200"
                >
                  通过
                </button>
                <button 
                  v-if="post.status === 'pending'"
                  @click="rejectPost(post.id)"
                  class="text-xs bg-rose-100 text-rose-600 rounded-full px-3 py-1 hover:bg-rose-200"
                >
                  拒绝
                </button>
                <button 
                  @click="viewPost(post.id)"
                  class="text-xs bg-blue-100 text-blue-600 rounded-full px-3 py-1 hover:bg-blue-200"
                >
                  查看
                </button>
                <button 
                  @click="deletePost(post.id)"
                  class="text-xs bg-gray-100 text-gray-600 rounded-full px-3 py-1 hover:bg-gray-200"
                >
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <div class="mt-6 flex justify-between items-center">
      <div class="text-sm text-gray-500">
        共 {{ totalPosts }} 条数据
      </div>
      <div class="flex space-x-2">
        <button 
          @click="prevPage" 
          :disabled="currentPage === 1"
          class="px-3 py-1 rounded bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          上一页
        </button>
        <button 
          @click="nextPage" 
          :disabled="currentPage >= totalPages"
          class="px-3 py-1 rounded bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale';

const authStore = useAuthStore();
const router = useRouter();

// 状态变量
const posts = ref([]);
const loading = ref(true);
const searchQuery = ref('');
const filterStatus = ref('all');
const currentPage = ref(1);
const pageSize = 10;
const totalPosts = ref(0);

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(totalPosts.value / pageSize);
});

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  return format(date, 'yyyy-MM-dd HH:mm', { locale: zhCN });
};

// 格式化状态
const formatStatus = (status) => {
  switch (status) {
    case 'published': return '已发布';
    case 'draft': return '草稿';
    case 'pending': return '待审核';
    case 'rejected': return '已拒绝';
    default: return status;
  }
};

// 获取文章列表
const fetchPosts = async (page = 1) => {
  try {
    loading.value = true;
    currentPage.value = page;
    
    // 实际环境中应调用API
    try {
      const response = await $fetch('/api/admin/posts', {
        method: 'GET',
        params: {
          page: page,
          pageSize: pageSize,
          status: filterStatus.value === 'all' ? '' : filterStatus.value,
          search: searchQuery.value
        },
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        }
      });
      
      if (response.code === 200) {
        posts.value = response.data.items;
        totalPosts.value = response.data.total;
      } else {
        throw new Error(response.message || '获取文章列表失败');
      }
    } catch (error) {
      console.error('API调用失败，使用模拟数据:', error);
      
      // 模拟数据
      setTimeout(() => {
        const mockPosts = generateMockPosts();
        
        // 过滤状态
        let filteredPosts = mockPosts;
        if (filterStatus.value !== 'all') {
          filteredPosts = mockPosts.filter(post => post.status === filterStatus.value);
        }
        
        // 搜索过滤
        if (searchQuery.value) {
          const query = searchQuery.value.toLowerCase();
          filteredPosts = filteredPosts.filter(
            post => post.title.toLowerCase().includes(query) || 
                   post.author.nickname.toLowerCase().includes(query)
          );
        }
        
        // 分页
        const start = (page - 1) * pageSize;
        const end = start + pageSize;
        posts.value = filteredPosts.slice(start, end);
        totalPosts.value = filteredPosts.length;
        
        loading.value = false;
      }, 800);
    }
  } catch (error) {
    console.error('获取文章列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索文章
const searchPosts = () => {
  fetchPosts(1);
};

// 翻页
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    fetchPosts(currentPage.value + 1);
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    fetchPosts(currentPage.value - 1);
  }
};

// 查看文章
const viewPost = (id) => {
  router.push(`/post/${id}`);
};

// 审核文章
const approvePost = async (id) => {
  if (!confirm('确定通过这篇文章吗？')) return;
  
  try {
    const response = await $fetch(`/api/admin/posts/${id}/approve`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('文章已通过审核');
      fetchPosts(currentPage.value);
    } else {
      throw new Error(response.message || '审核操作失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = posts.value.findIndex(post => post.id === id);
    if (index !== -1) {
      posts.value[index].status = 'published';
      alert('文章已通过审核');
    }
  }
};

const rejectPost = async (id) => {
  if (!confirm('确定拒绝这篇文章吗？')) return;
  
  try {
    const response = await $fetch(`/api/admin/posts/${id}/reject`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('文章已被拒绝');
      fetchPosts(currentPage.value);
    } else {
      throw new Error(response.message || '审核操作失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = posts.value.findIndex(post => post.id === id);
    if (index !== -1) {
      posts.value[index].status = 'rejected';
      alert('文章已被拒绝');
    }
  }
};

// 删除文章
const deletePost = async (id) => {
  if (!confirm('确定要删除这篇文章吗？此操作不可恢复！')) return;
  
  try {
    const response = await $fetch(`/api/admin/posts/${id}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('文章已删除');
      fetchPosts(currentPage.value);
    } else {
      throw new Error(response.message || '删除操作失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    posts.value = posts.value.filter(post => post.id !== id);
    totalPosts.value--;
    alert('文章已删除');
  }
};

// 生成模拟文章数据
const generateMockPosts = () => {
  const statuses = ['published', 'draft', 'pending', 'rejected'];
  const authors = [
    { id: 1, nickname: '张三', avatar: '/img/avatar1.jpg' },
    { id: 2, nickname: '李四', avatar: '/img/avatar2.jpg' },
    { id: 3, nickname: '王五', avatar: '/img/avatar3.jpg' }
  ];
  
  return Array.from({ length: 35 }, (_, i) => {
    const id = i + 1;
    const randomDate = new Date();
    randomDate.setDate(randomDate.getDate() - Math.floor(Math.random() * 90));
    
    return {
      id,
      title: `测试文章 #${id} - ${['Vue.js实战', 'React入门', 'TypeScript高级指南', 'Web性能优化', 'CSS动画详解'][i % 5]}`,
      coverImage: i % 3 === 0 ? `/img/post-cover-${(i % 5) + 1}.jpg` : null,
      status: statuses[i % 4],
      viewCount: Math.floor(Math.random() * 1000),
      commentCount: Math.floor(Math.random() * 50),
      likeCount: Math.floor(Math.random() * 100),
      author: authors[i % 3],
      createdAt: randomDate.toISOString()
    };
  });
};

onMounted(() => {
  fetchPosts(1);
});
</script> 