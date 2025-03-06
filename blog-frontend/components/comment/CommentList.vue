<template>
  <div class="comments-section mt-10">
    <h2 class="text-2xl font-bold text-gray-800 mb-4">
      <span class="flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
        </svg>
        评论 ({{ totalComments }})
      </span>
    </h2>

    <!-- 评论表单 -->
    <CommentForm 
      :post-id="postId" 
      :reply-to="currentReplyTo" 
      @comment-added="handleCommentAdded"
      @cancel-reply="currentReplyTo = null" 
    />

    <!-- 排序选项 -->
    <div class="flex justify-between items-center my-4">
      <div class="comment-sort">
        <span class="text-gray-500 mr-2">排序方式:</span>
        <button 
          @click="sortComments('latest')" 
          :class="[
            'mr-2 px-2 py-1 rounded', 
            sortBy === 'latest' ? 'bg-blue-100 text-blue-600' : 'text-gray-500 hover:bg-gray-100'
          ]"
        >
          最新
        </button>
        <button 
          @click="sortComments('popular')" 
          :class="[
            'px-2 py-1 rounded', 
            sortBy === 'popular' ? 'bg-blue-100 text-blue-600' : 'text-gray-500 hover:bg-gray-100'
          ]"
        >
          最热
        </button>
      </div>
      
      <button 
        v-if="hasMoreComments" 
        class="text-blue-500 hover:text-blue-700"
        @click="loadMoreComments"
        :disabled="isLoadingMore"
      >
        {{ isLoadingMore ? '加载中...' : '加载更多' }}
      </button>
    </div>

    <!-- 加载动画 -->
    <div v-if="isLoading" class="flex justify-center py-6">
      <div class="loader"></div>
    </div>

    <div v-else-if="comments.length === 0" class="py-10 text-center text-gray-500">
      暂无评论，快来发表第一条评论吧~
    </div>

    <div v-else class="comments-list mt-8 space-y-6">
      <div 
        v-for="comment in sortedComments" 
        :key="comment.id" 
        class="comment"
      >
        <div class="comment-item bg-white p-4 rounded-lg border border-gray-100 hover:border-gray-200 transition-colors">
          <!-- 评论头部 -->
          <div class="flex items-start">
            <img 
              :src="comment.author.avatar || '/img/default-avatar.png'" 
              alt="用户头像" 
              class="w-10 h-10 rounded-full mr-3"
            />
            <div class="flex-grow">
              <div class="flex items-center justify-between">
                <div>
                  <span class="font-medium text-gray-900">{{ comment.author.nickname }}</span>
                  <span class="text-xs text-gray-500 ml-2">{{ formatDate(comment.createdAt) }}</span>
                </div>
                <!-- 操作按钮 -->
                <div class="flex items-center space-x-2">
                  <!-- 点赞按钮 -->
                  <button 
                    @click="likeComment(comment)" 
                    class="flex items-center text-gray-400 hover:text-rose-500 text-sm"
                    :class="{ 'text-rose-500': isCommentLiked(comment.id) }"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor" :class="{ 'fill-current': isCommentLiked(comment.id) }">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                    </svg>
                    <span>{{ getCommentLikeCount(comment.id) }}</span>
                  </button>
                  <!-- 仅显示删除自己的评论 -->
                  <button 
                    v-if="isCommentAuthor(comment)" 
                    @click="deleteComment(comment)" 
                    class="text-gray-400 hover:text-rose-500 text-sm"
                  >
                    删除
                  </button>
                  <button 
                    @click="replyToComment(comment)" 
                    class="text-gray-400 hover:text-rose-500 text-sm flex items-center"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6" />
                    </svg>
                    回复
                  </button>
                </div>
              </div>
              <!-- 评论内容 -->
              <div class="mt-2 text-gray-800">
                <p>{{ comment.content }}</p>
              </div>
            </div>
          </div>

          <!-- 评论回复 -->
          <div v-if="comment.replies && comment.replies.length > 0" class="mt-4 pl-10 space-y-4">
            <div 
              v-for="reply in comment.replies" 
              :key="reply.id" 
              class="reply bg-gray-50 p-3 rounded-lg"
            >
              <div class="flex items-start">
                <img 
                  :src="reply.author.avatar || '/img/default-avatar.png'" 
                  alt="用户头像" 
                  class="w-8 h-8 rounded-full mr-2"
                />
                <div class="flex-grow">
                  <div class="flex items-center justify-between">
                    <div>
                      <span class="font-medium text-gray-900">{{ reply.author.nickname }}</span>
                      <span class="text-xs text-gray-500 ml-2">{{ formatDate(reply.createdAt) }}</span>
                    </div>
                    <!-- 操作按钮 -->
                    <div class="flex items-center space-x-2">
                      <!-- 点赞按钮 -->
                      <button 
                        @click="likeComment(reply)" 
                        class="flex items-center text-gray-400 hover:text-rose-500 text-xs"
                        :class="{ 'text-rose-500': isCommentLiked(reply.id) }"
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor" :class="{ 'fill-current': isCommentLiked(reply.id) }">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                        </svg>
                        <span>{{ getCommentLikeCount(reply.id) }}</span>
                      </button>
                      <button 
                        v-if="isCommentAuthor(reply)" 
                        @click="deleteComment(reply)" 
                        class="text-gray-400 hover:text-rose-500 text-xs"
                      >
                        删除
                      </button>
                      <button 
                        @click="replyToComment(reply)" 
                        class="text-gray-400 hover:text-rose-500 text-xs flex items-center"
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6" />
                        </svg>
                        回复
                      </button>
                    </div>
                  </div>
                  <!-- 回复内容 -->
                  <div class="mt-1 text-gray-800 text-sm">
                    <p>
                      <span v-if="reply.replyToUser" class="text-rose-500 font-medium">
                        @{{ reply.replyToUser.nickname }}
                      </span>
                      {{ reply.content }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import CommentForm from './CommentForm.vue';
import UserList from '../UserList.vue';
import { useInteractionStore } from '@/stores/interaction';
import { useUserStore } from '@/stores/user';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn';

dayjs.extend(relativeTime);
dayjs.locale('zh-cn');

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true
  }
});

const authStore = useAuthStore();
const interactionStore = useInteractionStore();
const userStore = useUserStore();
const comments = ref([]);
const loading = ref(true);
const loadingMore = ref(false);
const hasMoreComments = ref(false);
const page = ref(1);
const pageSize = 10;
const currentReplyTo = ref(null);
const sortBy = ref('latest');

const totalComments = computed(() => {
  let count = comments.value.length;
  
  // 统计回复数量
  comments.value.forEach(comment => {
    if (comment.replies) {
      count += comment.replies.length;
    }
  });
  
  return count;
});

// 检查当前用户是否为评论作者
const isCommentAuthor = (comment) => {
  if (!authStore.isLoggedIn || !authStore.user) return false;
  return comment.author.id === authStore.user.id;
};

// 修改排序
const sortedComments = computed(() => {
  if (sortBy.value === 'latest') {
    // 按最新排序
    return [...comments.value].sort((a, b) => {
      return new Date(b.createdAt) - new Date(a.createdAt);
    });
  } else if (sortBy.value === 'popular') {
    // 按热度（点赞数）排序
    return [...comments.value].sort((a, b) => {
      const aLikes = interactionStore.getCommentLikeCount(a.id);
      const bLikes = interactionStore.getCommentLikeCount(b.id);
      if (bLikes === aLikes) {
        return new Date(b.createdAt) - new Date(a.createdAt);
      }
      return bLikes - aLikes;
    });
  }
  return comments.value;
});

// 加载评论
const fetchComments = async () => {
  try {
    loading.value = true;
    
    // 尝试从API获取评论
    try {
      const response = await $fetch(`/api/comments`, {
        method: 'GET',
        params: {
          postId: props.postId,
          page: page.value,
          pageSize
        }
      });
      
      if (response.code === 200) {
        if (page.value === 1) {
          comments.value = response.data.items;
        } else {
          comments.value = [...comments.value, ...response.data.items];
        }
        hasMoreComments.value = response.data.hasMore;
      } else {
        throw new Error(response.message || '获取评论失败');
      }
    } catch (apiError) {
      console.error('API调用失败，使用本地存储:', apiError);
      
      // 从localStorage获取评论
      if (process.client && window.localStorage) {
        const mockComments = getMockComments();
        
        // 模拟分页
        const start = (page.value - 1) * pageSize;
        const end = start + pageSize;
        const pageComments = mockComments.slice(start, end);
        
        if (page.value === 1) {
          comments.value = pageComments;
        } else {
          comments.value = [...comments.value, ...pageComments];
        }
        
        hasMoreComments.value = end < mockComments.length;
      }
    }
  } catch (error) {
    console.error('获取评论列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 从localStorage获取模拟评论
const getMockComments = () => {
  try {
    // 从localStorage获取所有评论
    const storageComments = JSON.parse(localStorage.getItem('mockComments') || '[]');
    
    // 过滤当前文章的评论
    return storageComments.filter(comment => comment.postId == props.postId);
  } catch (error) {
    console.error('解析本地评论失败:', error);
    return [];
  }
};

// 保存评论到localStorage
const saveMockComments = (commentsList) => {
  if (process.client && window.localStorage) {
    try {
      localStorage.setItem('mockComments', JSON.stringify(commentsList));
    } catch (error) {
      console.error('保存评论到localStorage失败:', error);
    }
  }
};

// 加载更多评论
const loadMoreComments = async () => {
  if (loadingMore.value || !hasMoreComments.value) return;
  
  loadingMore.value = true;
  page.value += 1;
  
  try {
    await fetchComments();
  } finally {
    loadingMore.value = false;
  }
};

// 回复评论
const replyToComment = (comment) => {
  currentReplyTo.value = comment;
  
  // 滚动到评论表单
  setTimeout(() => {
    const formElement = document.querySelector('.comment-form');
    if (formElement) {
      formElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
  }, 100);
};

// 处理评论添加
const handleCommentAdded = (newComment) => {
  // 如果是回复评论
  if (currentReplyTo.value) {
    // 查找被回复评论所属的主评论
    let isHandled = false;
    
    // 遍历所有顶级评论
    for (const parentComment of comments.value) {
      // 情况1: 回复的是顶级评论
      if (parentComment.id === currentReplyTo.value.id) {
        if (!parentComment.replies) {
          parentComment.replies = [];
        }
        parentComment.replies.push(newComment);
        isHandled = true;
        break;
      }
      
      // 情况2: 回复的是回复评论，需要查找其所属的顶级评论
      if (parentComment.replies && parentComment.replies.length > 0) {
        const replyIndex = parentComment.replies.findIndex(r => r.id === currentReplyTo.value.id);
        if (replyIndex !== -1) {
          if (!parentComment.replies) {
            parentComment.replies = [];
          }
          
          // 添加回复信息，指明是对哪条评论的回复
          newComment.replyToUser = {
            id: currentReplyTo.value.author.id,
            nickname: currentReplyTo.value.author.nickname
          };
          
          parentComment.replies.push(newComment);
          isHandled = true;
          break;
        }
      }
    }
    
    // 如果没有找到合适的父评论，则添加为顶级评论
    if (!isHandled) {
      comments.value.unshift(newComment);
    }
    
    // 清除当前回复对象
    currentReplyTo.value = null;
  } else {
    // 添加到顶级评论
    comments.value.unshift(newComment);
  }
  
  // 保存到localStorage
  saveMockCommentsWithCurrentComments();
};

// 删除评论
const deleteComment = async (commentId) => {
  if (!confirm('确定要删除此评论吗？')) return;
  
  try {
    // 尝试调用API删除
    try {
      const response = await $fetch(`/api/comments/${commentId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        }
      });
      
      if (response.code === 200) {
        removeCommentFromList(commentId);
      } else {
        throw new Error(response.message || '删除评论失败');
      }
    } catch (apiError) {
      console.error('API调用失败，使用本地存储:', apiError);
      removeCommentFromList(commentId);
      saveMockCommentsWithCurrentComments();
    }
  } catch (error) {
    console.error('删除评论失败:', error);
    alert('删除失败，请稍后再试');
  }
};

// 从列表中移除评论
const removeCommentFromList = (commentId) => {
  // 检查是否为顶级评论
  const topLevelIndex = comments.value.findIndex(c => c.id === commentId);
  
  if (topLevelIndex !== -1) {
    // 移除顶级评论
    comments.value.splice(topLevelIndex, 1);
  } else {
    // 在每个顶级评论的回复中查找
    comments.value.forEach(comment => {
      if (comment.replies) {
        const replyIndex = comment.replies.findIndex(r => r.id === commentId);
        if (replyIndex !== -1) {
          comment.replies.splice(replyIndex, 1);
        }
      }
    });
  }
};

// 保存当前评论列表到localStorage
const saveMockCommentsWithCurrentComments = () => {
  if (process.client && window.localStorage) {
    // 获取所有其他文章的评论
    const allComments = JSON.parse(localStorage.getItem('mockComments') || '[]');
    const otherPostsComments = allComments.filter(comment => comment.postId != props.postId);
    
    // 将当前文章的评论扁平化
    let currentPostComments = [];
    
    // 添加顶级评论
    comments.value.forEach(comment => {
      // 保存主评论（移除replies数组，将在下面单独保存回复）
      const parentComment = { ...comment };
      delete parentComment.replies;
      currentPostComments.push(parentComment);
      
      // 添加回复评论
      if (comment.replies && comment.replies.length > 0) {
        comment.replies.forEach(reply => {
          // 确保回复评论有正确的parentId
          const replyComment = { ...reply, parentId: comment.id };
          currentPostComments.push(replyComment);
        });
      }
    });
    
    // 合并并保存
    const allUpdatedComments = [...otherPostsComments, ...currentPostComments];
    saveMockComments(allUpdatedComments);
  }
};

// 格式化日期
const formatDate = (dateString) => {
  try {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffSec = Math.floor(diffMs / 1000);
    const diffMin = Math.floor(diffSec / 60);
    const diffHour = Math.floor(diffMin / 60);
    const diffDay = Math.floor(diffHour / 24);
    
    if (diffSec < 60) {
      return '刚刚';
    } else if (diffMin < 60) {
      return `${diffMin}分钟前`;
    } else if (diffHour < 24) {
      return `${diffHour}小时前`;
    } else if (diffDay < 30) {
      return `${diffDay}天前`;
    } else {
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    }
  } catch (error) {
    return dateString;
  }
};

// 评论排序
const sortComments = (type) => {
  sortBy.value = type;
};

// 处理评论点赞
const likeComment = (comment) => {
  if (!authStore.isLoggedIn) {
    alert('请先登录再点赞');
    return;
  }
  
  interactionStore.toggleCommentLike(
    comment.id, 
    props.postId, 
    '', // 文章标题，可以在props中添加
    comment.author.id
  );
};

// 是否已点赞评论
const isCommentLiked = (commentId) => {
  return interactionStore.isCommentLiked(commentId);
};

// 获取评论点赞数
const getCommentLikeCount = (commentId) => {
  return interactionStore.getCommentLikeCount(commentId);
};

// 获取评论点赞用户列表
const getCommentLikeUsers = (commentId) => {
  const userIds = interactionStore.getCommentLikeUserIds(commentId);
  return userStore.getUsers(userIds);
};

// 初始化
onMounted(() => {
  fetchComments();
  interactionStore.initialize();
  userStore.initialize();
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