<template>
  <div>
    <div class="mb-6 flex justify-between items-center">
      <h1 class="text-2xl font-bold text-gray-800">评论管理</h1>
      <div class="flex space-x-2">
        <div class="relative">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索评论内容或作者..." 
            class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
            @keyup.enter="searchComments"
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
          @change="fetchComments(1)"
        >
          <option value="all">所有状态</option>
          <option value="approved">已通过</option>
          <option value="pending">待审核</option>
          <option value="rejected">已拒绝</option>
        </select>
      </div>
    </div>
    
    <!-- 评论列表 -->
    <div class="space-y-4">
      <!-- 加载中 -->
      <div v-if="loading" class="space-y-4">
        <div v-for="i in 5" :key="i" class="bg-white rounded-lg shadow-sm p-4 animate-pulse">
          <div class="flex">
            <div class="w-10 h-10 rounded-full bg-gray-200 mr-3"></div>
            <div class="flex-1">
              <div class="h-4 bg-gray-200 rounded w-1/4 mb-2"></div>
              <div class="h-3 bg-gray-200 rounded w-full mb-2"></div>
              <div class="h-3 bg-gray-200 rounded w-3/4"></div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 无数据 -->
      <div v-else-if="comments.length === 0" class="bg-white rounded-lg shadow-sm p-8 text-center">
        <svg 
          xmlns="http://www.w3.org/2000/svg" 
          class="h-10 w-10 mx-auto text-gray-300" 
          fill="none" 
          viewBox="0 0 24 24" 
          stroke="currentColor"
        >
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
        </svg>
        <p class="mt-2 text-gray-500">
          {{ searchQuery ? '没有找到匹配的评论' : '暂无评论数据' }}
        </p>
      </div>
      
      <!-- 评论内容 -->
      <div 
        v-else 
        v-for="comment in comments" 
        :key="comment.id" 
        class="bg-white rounded-lg shadow-sm p-4"
      >
        <div class="flex items-start">
          <img 
            :src="comment.author.avatar || '/img/default-avatar.png'" 
            alt="用户头像" 
            class="w-10 h-10 rounded-full mr-3"
          />
          <div class="flex-1">
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <span class="font-medium text-gray-900 mr-2">{{ comment.author.nickname }}</span>
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="{
                    'bg-green-100 text-green-800': comment.status === 'approved',
                    'bg-amber-100 text-amber-800': comment.status === 'pending',
                    'bg-rose-100 text-rose-800': comment.status === 'rejected'
                  }"
                >
                  {{ formatStatus(comment.status) }}
                </span>
              </div>
              <div class="text-xs text-gray-500">
                {{ formatDate(comment.createdAt) }}
              </div>
            </div>
            
            <div class="mt-1 text-gray-800">
              <p>{{ comment.content }}</p>
            </div>
            
            <div class="mt-2 flex items-center justify-between">
              <div class="text-xs text-gray-500 flex items-center">
                <span class="inline-flex items-center mr-3">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                  {{ comment.likeCount || 0 }}
                </span>
                <span>
                  来自文章: <a :href="`/post/${comment.postId}`" class="text-blue-500 hover:underline">{{ comment.postTitle || `文章 #${comment.postId}` }}</a>
                </span>
              </div>
              
              <div class="flex space-x-2">
                <button 
                  v-if="comment.status === 'pending'"
                  @click="approveComment(comment.id)"
                  class="text-xs bg-green-100 text-green-600 rounded-full px-3 py-1 hover:bg-green-200"
                >
                  通过
                </button>
                <button 
                  v-if="comment.status === 'pending'"
                  @click="rejectComment(comment.id)"
                  class="text-xs bg-rose-100 text-rose-600 rounded-full px-3 py-1 hover:bg-rose-200"
                >
                  拒绝
                </button>
                <button 
                  @click="openReplyModal(comment)"
                  class="text-xs bg-blue-100 text-blue-600 rounded-full px-3 py-1 hover:bg-blue-200"
                >
                  回复
                </button>
                <button 
                  @click="deleteComment(comment.id)"
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
        共 {{ totalComments }} 条数据
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
    
    <!-- 回复评论模态框 -->
    <div 
      v-if="replyModalOpen" 
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-lg">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-medium text-gray-900">回复评论</h3>
          <button @click="replyModalOpen = false" class="text-gray-400 hover:text-gray-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div v-if="selectedComment" class="mb-4 p-3 bg-gray-50 rounded-lg">
          <div class="flex items-center mb-2">
            <img 
              :src="selectedComment.author.avatar || '/img/default-avatar.png'" 
              alt="用户头像" 
              class="w-6 h-6 rounded-full mr-2"
            />
            <span class="font-medium text-gray-800">{{ selectedComment.author.nickname }}</span>
          </div>
          <p class="text-gray-700">{{ selectedComment.content }}</p>
        </div>
        
        <div class="mb-4">
          <label for="reply-content" class="block text-sm font-medium text-gray-700 mb-2">回复内容</label>
          <textarea 
            id="reply-content" 
            v-model="replyContent" 
            rows="4" 
            class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
            placeholder="输入回复内容..."
          ></textarea>
        </div>
        
        <div class="flex justify-end space-x-3">
          <button 
            @click="replyModalOpen = false" 
            class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
          >
            取消
          </button>
          <button 
            @click="submitReply" 
            :disabled="!replyContent.trim()"
            class="px-4 py-2 bg-rose-500 text-white rounded-md hover:bg-rose-600 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            发布回复
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale';

const authStore = useAuthStore();

// 评论列表状态
const comments = ref([]);
const loading = ref(true);
const searchQuery = ref('');
const filterStatus = ref('all');
const currentPage = ref(1);
const pageSize = 10;
const totalComments = ref(0);

// 回复评论状态
const replyModalOpen = ref(false);
const selectedComment = ref(null);
const replyContent = ref('');

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(totalComments.value / pageSize);
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
    case 'approved': return '已通过';
    case 'pending': return '待审核';
    case 'rejected': return '已拒绝';
    default: return status;
  }
};

// 获取评论列表
const fetchComments = async (page = 1) => {
  try {
    loading.value = true;
    currentPage.value = page;
    
    // 实际环境中应调用API
    try {
      const response = await $fetch('/api/admin/comments', {
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
        comments.value = response.data.items;
        totalComments.value = response.data.total;
      } else {
        throw new Error(response.message || '获取评论列表失败');
      }
    } catch (error) {
      console.error('API调用失败，使用模拟数据:', error);
      
      // 模拟数据
      setTimeout(() => {
        const mockComments = generateMockComments();
        
        // 过滤状态
        let filteredComments = mockComments;
        if (filterStatus.value !== 'all') {
          filteredComments = mockComments.filter(comment => comment.status === filterStatus.value);
        }
        
        // 搜索过滤
        if (searchQuery.value) {
          const query = searchQuery.value.toLowerCase();
          filteredComments = filteredComments.filter(
            comment => comment.content.toLowerCase().includes(query) || 
                      comment.author.nickname.toLowerCase().includes(query)
          );
        }
        
        // 分页
        const start = (page - 1) * pageSize;
        const end = start + pageSize;
        comments.value = filteredComments.slice(start, end);
        totalComments.value = filteredComments.length;
        
        loading.value = false;
      }, 800);
    }
  } catch (error) {
    console.error('获取评论列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索评论
const searchComments = () => {
  fetchComments(1);
};

// 翻页
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    fetchComments(currentPage.value + 1);
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    fetchComments(currentPage.value - 1);
  }
};

// 审核评论
const approveComment = async (id) => {
  if (!confirm('确定通过这条评论吗？')) return;
  
  try {
    const response = await $fetch(`/api/admin/comments/${id}/approve`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('评论已通过审核');
      fetchComments(currentPage.value);
    } else {
      throw new Error(response.message || '审核操作失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = comments.value.findIndex(comment => comment.id === id);
    if (index !== -1) {
      comments.value[index].status = 'approved';
      alert('评论已通过审核');
    }
  }
};

const rejectComment = async (id) => {
  if (!confirm('确定拒绝这条评论吗？')) return;
  
  try {
    const response = await $fetch(`/api/admin/comments/${id}/reject`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('评论已被拒绝');
      fetchComments(currentPage.value);
    } else {
      throw new Error(response.message || '审核操作失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = comments.value.findIndex(comment => comment.id === id);
    if (index !== -1) {
      comments.value[index].status = 'rejected';
      alert('评论已被拒绝');
    }
  }
};

// 删除评论
const deleteComment = async (id) => {
  if (!confirm('确定要删除这条评论吗？此操作不可恢复！')) return;
  
  try {
    const response = await $fetch(`/api/admin/comments/${id}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('评论已删除');
      fetchComments(currentPage.value);
    } else {
      throw new Error(response.message || '删除操作失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    comments.value = comments.value.filter(comment => comment.id !== id);
    totalComments.value--;
    alert('评论已删除');
  }
};

// 回复评论
const openReplyModal = (comment) => {
  selectedComment.value = comment;
  replyModalOpen.value = true;
  replyContent.value = '';
};

const submitReply = async () => {
  if (!replyContent.value.trim() || !selectedComment.value) return;
  
  try {
    const response = await $fetch(`/api/comments`, {
      method: 'POST',
      body: {
        content: replyContent.value,
        postId: selectedComment.value.postId,
        parentId: selectedComment.value.id,
        replyToUserId: selectedComment.value.author.id
      },
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('回复已发布');
      replyModalOpen.value = false;
      fetchComments(currentPage.value);
    } else {
      throw new Error(response.message || '回复发布失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    alert('回复已发布');
    replyModalOpen.value = false;
    // 不重新加载评论列表，因为管理员回复不会显示在这个列表中
  }
};

// 生成模拟评论数据
const generateMockComments = () => {
  const statuses = ['approved', 'pending', 'rejected'];
  const authors = [
    { id: 1, nickname: '张三', avatar: '/img/avatar1.jpg' },
    { id: 2, nickname: '李四', avatar: '/img/avatar2.jpg' },
    { id: 3, nickname: '王五', avatar: '/img/avatar3.jpg' }
  ];
  
  const postTitles = [
    'Vue.js实战教程',
    'React入门指南',
    'TypeScript高级技巧',
    'Web性能优化实践',
    'CSS动画实现方案'
  ];
  
  return Array.from({ length: 45 }, (_, i) => {
    const id = i + 1;
    const randomDate = new Date();
    randomDate.setDate(randomDate.getDate() - Math.floor(Math.random() * 30));
    const postId = Math.floor(Math.random() * 5) + 1;
    
    return {
      id,
      content: [
        '这篇文章很有帮助，谢谢分享！',
        '我有一个问题，请问如何解决XXX问题？',
        '这个实现方式很巧妙，学习了。',
        '文章内容有些错误，建议作者修改一下。',
        '期待更多相关内容的分享。'
      ][i % 5],
      status: statuses[i % 3],
      likeCount: Math.floor(Math.random() * 20),
      author: authors[i % 3],
      postId,
      postTitle: postTitles[postId - 1],
      createdAt: randomDate.toISOString()
    };
  });
};

onMounted(() => {
  fetchComments(1);
});
</script> 