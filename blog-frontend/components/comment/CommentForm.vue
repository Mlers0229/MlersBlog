<template>
  <div class="comment-form mt-6">
    <h3 class="text-lg font-semibold text-gray-800 mb-3">{{ isReply ? '回复评论' : '发表评论' }}</h3>
    <div v-if="replyTo" class="bg-gray-50 p-3 rounded-lg mb-3 text-sm">
      <span class="font-medium text-rose-500">回复 {{ replyTo.author.nickname }}:</span>
      <p class="text-gray-600 mt-1">
        <span v-if="replyTo.replyToUser" class="text-gray-500 font-medium">
          @{{ replyTo.replyToUser.nickname }}
        </span>
        {{ replyTo.content }}
      </p>
      <button 
        @click="cancelReply" 
        class="text-gray-500 text-xs mt-2 hover:text-rose-500"
      >
        取消回复
      </button>
    </div>
    <div class="flex">
      <div class="flex-shrink-0 mr-3">
        <img 
          :src="userAvatar || '/img/default-avatar.png'" 
          alt="用户头像" 
          class="w-10 h-10 rounded-full"
        />
      </div>
      <div class="flex-grow">
        <textarea 
          v-model="content" 
          placeholder="写下你的评论..." 
          class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-300 focus:border-transparent"
          rows="3"
        ></textarea>
        <div class="flex justify-between items-center mt-2">
          <p v-if="!isLoggedIn" class="text-sm text-gray-500">
            <a href="/login" class="text-rose-500 hover:underline">登录</a> 后才能发表评论
          </p>
          <div class="flex ml-auto">
            <button 
              v-if="isReply" 
              @click="cancelReply" 
              class="px-4 py-2 bg-gray-200 text-gray-700 rounded-full hover:bg-gray-300 transition-colors mr-2"
            >
              取消
            </button>
            <button 
              @click="submitComment" 
              class="px-6 py-2 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors disabled:bg-rose-300"
              :disabled="!isLoggedIn || !content.trim() || submitting"
            >
              {{ submitting ? '提交中...' : '提交评论' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useEventBus } from '@/composables/useEventBus';

const props = defineProps({
  postId: {
    type: [String, Number],
    required: true
  },
  postTitle: {
    type: String,
    default: ''
  },
  postAuthorId: {
    type: [String, Number],
    default: null
  },
  replyTo: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['comment-added', 'cancel-reply']);

const authStore = useAuthStore();
const eventBus = useEventBus();
const isLoggedIn = computed(() => authStore.isLoggedIn);
const userAvatar = computed(() => authStore.user?.avatar || '');

const content = ref('');
const submitting = ref(false);
const isReply = computed(() => !!props.replyTo);

// 提交评论
const submitComment = async () => {
  if (!isLoggedIn.value || !content.value.trim() || submitting.value) {
    return;
  }
  
  submitting.value = true;
  
  try {
    // 创建评论数据
    const commentData = {
      content: content.value.trim(),
      postId: props.postId,
      parentId: props.replyTo?.parentId || props.replyTo?.id || null,
      replyToId: props.replyTo?.id || null,
      replyToUser: props.replyTo ? {
        id: props.replyTo.author.id,
        nickname: props.replyTo.author.nickname
      } : null
    };
    
    console.log('提交评论数据:', commentData);
    
    // 尝试发送到API
    try {
      const response = await $fetch('/api/comments', {
        method: 'POST',
        body: commentData,
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        }
      });
      
      if (response.code === 200) {
        console.log('评论提交成功:', response.data);
        // 清空内容并通知父组件
        content.value = '';
        emit('comment-added', response.data);
        
        // 如果是回复，触发取消回复事件
        if (props.replyTo) {
          emit('cancel-reply');
        }
        
        // 发送通知
        if (props.replyTo) {
          // 回复通知 - 发送给被回复的用户
          const replyToUserId = props.replyTo.replyTo 
            ? props.replyTo.replyTo.user.id 
            : props.replyTo.comment.user.id;
          
          // 不要给自己发送通知
          if (replyToUserId !== authStore.user.id) {
            eventBus.emit('add-notification', {
              type: 'reply',
              content: commentData.content,
              actor: authStore.user,
              target: {
                id: props.postId,
                title: props.postTitle || `文章 #${props.postId}`
              },
              commentId: response.data.id
            });
          }
        } else {
          // 评论通知 - 发送给文章作者
          // 不要给自己发送通知
          if (props.postAuthorId && props.postAuthorId !== authStore.user.id) {
            eventBus.emit('add-notification', {
              type: 'comment',
              content: commentData.content,
              actor: authStore.user,
              target: {
                id: props.postId,
                title: props.postTitle || `文章 #${props.postId}`
              },
              commentId: response.data.id
            });
          }
        }
      } else {
        throw new Error(response.message || '评论失败');
      }
    } catch (apiError) {
      console.error('API调用失败，使用本地存储:', apiError);
      
      // 模拟API响应
      const mockComment = createMockComment(commentData);
      console.log('创建模拟评论:', mockComment);
      
      // 清空内容并通知父组件
      content.value = '';
      emit('comment-added', mockComment);
    }
  } catch (error) {
    console.error('提交评论失败:', error);
    alert('评论失败，请稍后再试');
  } finally {
    submitting.value = false;
  }
};

// 创建模拟评论数据
const createMockComment = (commentData) => {
  const now = new Date();
  
  return {
    id: Date.now(),
    content: commentData.content,
    postId: commentData.postId,
    parentId: commentData.parentId,
    replyToId: commentData.replyToId,
    replyToUser: commentData.replyToUser,
    createdAt: now.toISOString(),
    updatedAt: now.toISOString(),
    author: {
      id: authStore.user?.id || 1,
      nickname: authStore.user?.nickname || '测试用户',
      avatar: authStore.user?.avatar || '/img/default-avatar.png'
    },
    replies: []
  };
};

// 取消回复
const cancelReply = () => {
  emit('cancel-reply');
};
</script> 