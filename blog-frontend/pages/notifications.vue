<template>
  <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
      </svg>
      通知中心
    </h1>
    
    <div class="flex mb-6 border-b border-gray-100">
      <button 
        v-for="(tab, index) in tabs" 
        :key="index"
        :class="[
          'px-4 py-2 font-medium border-b-2 -mb-px', 
          activeTab === index 
            ? 'text-rose-500 border-rose-500' 
            : 'text-gray-500 border-transparent hover:text-gray-700'
        ]"
        @click="activeTab = index"
      >
        {{ tab.name }}
        <span v-if="tab.unread > 0" class="ml-1 inline-flex items-center justify-center w-5 h-5 text-xs font-medium rounded-full bg-rose-100 text-rose-600">
          {{ tab.unread }}
        </span>
      </button>
    </div>
    
    <div v-if="loading" class="space-y-4">
      <div v-for="i in 3" :key="i" class="animate-pulse">
        <div class="flex items-start">
          <div class="w-10 h-10 rounded-full bg-gray-200 mr-3"></div>
          <div class="flex-1">
            <div class="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
            <div class="h-3 bg-gray-200 rounded w-1/2"></div>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else-if="filteredNotifications.length === 0" class="text-center py-16">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
      </svg>
      <h3 class="mt-4 text-xl font-medium text-gray-800">暂无通知</h3>
      <p class="mt-2 text-gray-600">当有新的通知时，会显示在这里</p>
    </div>
    
    <div v-else class="space-y-4">
      <div 
        v-for="notification in filteredNotifications" 
        :key="notification.id"
        :class="[
          'p-4 border rounded-lg hover:bg-gray-50 transition-colors', 
          notification.isRead ? 'border-gray-100' : 'border-rose-100 bg-rose-50 hover:bg-rose-50'
        ]"
      >
        <div class="flex items-start">
          <!-- 评论通知 -->
          <div v-if="notification.type === 'comment'" class="w-10 h-10 rounded-full overflow-hidden mr-3 flex-shrink-0">
            <img :src="notification.sender.avatar || '/images/default-avatar.png'" alt="用户头像" class="w-full h-full object-cover" />
          </div>
          
          <!-- 点赞通知 -->
          <div v-else-if="notification.type === 'like'" class="w-10 h-10 rounded-full overflow-hidden mr-3 flex-shrink-0">
            <img :src="notification.sender.avatar || '/images/default-avatar.png'" alt="用户头像" class="w-full h-full object-cover" />
          </div>
          
          <!-- 系统通知 -->
          <div v-else class="w-10 h-10 rounded-full bg-blue-500 flex items-center justify-center text-white mr-3 flex-shrink-0">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          
          <div class="flex-1">
            <!-- 评论通知 -->
            <p v-if="notification.type === 'comment'" class="text-gray-700">
              <span class="font-medium text-gray-900">{{ notification.sender.nickname }}</span> 
              评论了你的文章 
              <NuxtLink :to="`/post/${notification.postId}`" class="text-rose-500 hover:underline">
                {{ notification.postTitle }}
              </NuxtLink>
            </p>
            
            <!-- 点赞通知 -->
            <p v-else-if="notification.type === 'like'" class="text-gray-700">
              <span class="font-medium text-gray-900">{{ notification.sender.nickname }}</span> 
              点赞了你的文章 
              <NuxtLink :to="`/post/${notification.postId}`" class="text-rose-500 hover:underline">
                {{ notification.postTitle }}
              </NuxtLink>
            </p>
            
            <!-- 系统通知 -->
            <p v-else class="text-gray-700">
              <span class="font-medium text-gray-900">系统通知</span> 
              {{ notification.content }}
            </p>
            
            <!-- 评论内容 -->
            <p v-if="notification.type === 'comment'" class="mt-1 text-gray-600 border-l-4 border-gray-200 pl-3 py-1">
              {{ notification.content }}
            </p>
            
            <div class="mt-2 flex items-center justify-between">
              <span class="text-xs text-gray-500">{{ formatDate(notification.createdAt) }}</span>
              
              <div class="flex space-x-2">
                <button 
                  v-if="!notification.isRead" 
                  class="text-xs text-gray-500 hover:text-gray-700"
                  @click="markAsRead(notification.id)"
                >
                  标为已读
                </button>
                <button 
                  class="text-xs text-gray-500 hover:text-gray-700"
                  @click="deleteNotification(notification.id)"
                >
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 加载更多 -->
    <div v-if="notifications.length > 0 && hasMoreNotifications" class="mt-6 text-center">
      <button 
        class="px-4 py-2 bg-white border border-gray-300 rounded-lg text-gray-600 hover:bg-gray-50 transition-colors"
        @click="loadMoreNotifications"
        :disabled="loadingMore"
      >
        {{ loadingMore ? '加载中...' : '加载更多' }}
      </button>
    </div>
    
    <!-- 全部标为已读 -->
    <div v-if="unreadCount > 0" class="mt-6 text-center">
      <button 
        class="px-4 py-2 bg-white border border-gray-300 rounded-lg text-gray-600 hover:bg-gray-50 transition-colors"
        @click="markAllAsRead"
      >
        全部标为已读
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { format, parseISO } from 'date-fns';
import { zhCN } from 'date-fns/locale';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

// 标签页
const activeTab = ref(0);
const tabs = [
  { name: '全部通知', type: 'all', unread: 0 },
  { name: '评论', type: 'comment', unread: 0 },
  { name: '点赞', type: 'like', unread: 0 },
  { name: '系统', type: 'system', unread: 0 }
];

// 状态
const loading = ref(false);
const loadingMore = ref(false);
const hasMoreNotifications = ref(true);
const page = ref(1);
const pageSize = 10;

// 通知数据
const notifications = ref([]);

// 计算属性：筛选后的通知
const filteredNotifications = computed(() => {
  const type = tabs[activeTab.value].type;
  return type === 'all' 
    ? notifications.value 
    : notifications.value.filter(n => n.type === type);
});

// 计算属性：未读通知数量
const unreadCount = computed(() => {
  return notifications.value.filter(n => !n.isRead).length;
});

// 获取通知
const fetchNotifications = async () => {
  if (!authStore.isLoggedIn) {
    if (process.client) {
      alert('请先登录');
      router.push('/login');
    }
    return;
  }
  
  loading.value = true;
  
  try {
    const response = await $fetch('/api/notifications', {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      },
      params: {
        page: page.value,
        pageSize
      }
    });
    
    if (response.code === 200) {
      notifications.value = response.data.items;
      hasMoreNotifications.value = response.data.totalPages > page.value;
      updateTabUnreadCount();
    }
  } catch (error) {
    console.error('获取通知失败:', error);
    
    // 使用模拟数据
    setTimeout(() => {
      notifications.value = [
        {
          id: 1,
          type: 'comment',
          isRead: false,
          sender: {
            id: 101,
            nickname: '评论用户',
            avatar: ''
          },
          postId: 1,
          postTitle: '如何使用Nuxt 3构建高性能博客',
          content: '这篇文章写得非常好，学到了很多知识！',
          createdAt: new Date(Date.now() - 2*3600000).toISOString() // 2小时前
        },
        {
          id: 2,
          type: 'like',
          isRead: true,
          sender: {
            id: 102,
            nickname: '点赞用户',
            avatar: ''
          },
          postId: 2,
          postTitle: 'Tailwind CSS与传统CSS框架的比较',
          createdAt: new Date(Date.now() - 25*3600000).toISOString() // 1天前
        },
        {
          id: 3,
          type: 'system',
          isRead: false,
          content: '欢迎使用博客系统，点击查看新手指南！',
          createdAt: new Date(Date.now() - 3*86400000).toISOString() // 3天前
        }
      ];
      
      hasMoreNotifications.value = false;
      updateTabUnreadCount();
      loading.value = false;
    }, 1000);
  } finally {
    loading.value = false;
  }
};

// 加载更多通知
const loadMoreNotifications = async () => {
  if (loadingMore.value || !hasMoreNotifications.value) return;
  
  loadingMore.value = true;
  page.value += 1;
  
  try {
    const response = await $fetch('/api/notifications', {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      },
      params: {
        page: page.value,
        pageSize
      }
    });
    
    if (response.code === 200) {
      notifications.value = [...notifications.value, ...response.data.items];
      hasMoreNotifications.value = response.data.totalPages > page.value;
    }
  } catch (error) {
    console.error('加载更多通知失败:', error);
  } finally {
    loadingMore.value = false;
  }
};

// 标记为已读
const markAsRead = async (notificationId) => {
  try {
    await $fetch(`/api/notifications/${notificationId}/read`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    // 更新本地通知状态
    const notification = notifications.value.find(n => n.id === notificationId);
    if (notification) {
      notification.isRead = true;
      updateTabUnreadCount();
    }
  } catch (error) {
    console.error('标记通知为已读失败:', error);
    
    // 本地模拟
    const notification = notifications.value.find(n => n.id === notificationId);
    if (notification) {
      notification.isRead = true;
      updateTabUnreadCount();
    }
  }
};

// 删除通知
const deleteNotification = async (notificationId) => {
  if (!process.client) return;
  
  if (confirm('确定要删除这条通知吗？')) {
    try {
      await $fetch(`/api/notifications/${notificationId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        }
      });
      
      // 从列表中移除
      notifications.value = notifications.value.filter(n => n.id !== notificationId);
      updateTabUnreadCount();
    } catch (error) {
      console.error('删除通知失败:', error);
      
      // 本地模拟
      notifications.value = notifications.value.filter(n => n.id !== notificationId);
      updateTabUnreadCount();
    }
  }
};

// 全部标为已读
const markAllAsRead = async () => {
  try {
    await $fetch('/api/notifications/read-all', {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    // 更新所有通知为已读
    notifications.value.forEach(notification => {
      notification.isRead = true;
    });
    
    updateTabUnreadCount();
  } catch (error) {
    console.error('标记所有通知为已读失败:', error);
    
    // 本地模拟
    notifications.value.forEach(notification => {
      notification.isRead = true;
    });
    
    updateTabUnreadCount();
  }
};

// 更新标签页未读计数
const updateTabUnreadCount = () => {
  // 全部通知
  tabs[0].unread = notifications.value.filter(n => !n.isRead).length;
  
  // 评论
  tabs[1].unread = notifications.value.filter(n => !n.isRead && n.type === 'comment').length;
  
  // 点赞
  tabs[2].unread = notifications.value.filter(n => !n.isRead && n.type === 'like').length;
  
  // 系统
  tabs[3].unread = notifications.value.filter(n => !n.isRead && n.type === 'system').length;
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  const now = new Date();
  const diffInHours = Math.floor((now - date) / (1000 * 60 * 60));
  
  if (diffInHours < 1) {
    return '刚刚';
  } else if (diffInHours < 24) {
    return `${diffInHours}小时前`;
  } else if (diffInHours < 48) {
    return '昨天';
  } else if (diffInHours < 168) { // 7天内
    return `${Math.floor(diffInHours / 24)}天前`;
  } else {
    return format(date, 'yyyy年MM月dd日', { locale: zhCN });
  }
};

onMounted(() => {
  if (process.client) {
    // 检查用户是否登录
    if (!authStore.isLoggedIn) {
      alert('请先登录');
      router.push('/login');
      return;
    }
    
    fetchNotifications();
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