<template>
  <div class="notification-center">
    <!-- 通知图标和徽章 -->
    <div class="relative">
      <button 
        @click="toggleNotificationPanel" 
        class="text-gray-600 hover:text-gray-800 focus:outline-none"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
        </svg>
        <span 
          v-if="unreadCount > 0" 
          class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-4 w-4 flex items-center justify-center"
        >
          {{ unreadCount > 99 ? '99+' : unreadCount }}
        </span>
      </button>
    </div>
    
    <!-- 通知面板 -->
    <div 
      v-if="showPanel" 
      class="notification-panel"
    >
      <!-- 通知面板头部 -->
      <div class="flex justify-between items-center px-4 py-3 border-b border-gray-200">
        <h3 class="text-lg font-semibold">通知</h3>
        <div class="flex space-x-2">
          <button 
            v-if="unreadCount > 0"
            @click="markAllAsRead" 
            class="text-sm text-blue-500 hover:text-blue-700"
          >
            全部标为已读
          </button>
          <button 
            @click="showPanel = false" 
            class="text-gray-400 hover:text-gray-600"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>
      
      <!-- 通知列表 -->
      <div class="overflow-y-auto max-h-80">
        <template v-if="notifications.length > 0">
          <div 
            v-for="notification in notifications" 
            :key="notification.id"
            :class="[
              'notification-item p-4 border-b border-gray-100 hover:bg-gray-50',
              { 'bg-blue-50': !notification.read }
            ]"
            @click="openNotification(notification)"
          >
            <!-- 点赞通知 -->
            <div v-if="notification.type === 'like'" class="flex items-start">
              <div class="mr-3">
                <div class="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center text-red-500">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                </div>
              </div>
              <div class="flex-1">
                <div class="flex items-center">
                  <span class="font-medium text-gray-800">{{ notification.actor.nickname }}</span>
                  <span class="ml-1 text-gray-600">点赞了你的文章</span>
                </div>
                <div class="mt-1 text-gray-500 text-sm line-clamp-1">{{ notification.target.title }}</div>
                <div class="mt-2 text-gray-400 text-xs">{{ formatDate(notification.createTime) }}</div>
              </div>
              <div v-if="!notification.read" class="ml-2 w-2 h-2 rounded-full bg-blue-500"></div>
            </div>
            
            <!-- 收藏通知 -->
            <div v-else-if="notification.type === 'favorite'" class="flex items-start">
              <div class="mr-3">
                <div class="w-10 h-10 rounded-full bg-yellow-100 flex items-center justify-center text-yellow-500">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" />
                  </svg>
                </div>
              </div>
              <div class="flex-1">
                <div class="flex items-center">
                  <span class="font-medium text-gray-800">{{ notification.actor.nickname }}</span>
                  <span class="ml-1 text-gray-600">收藏了你的文章</span>
                </div>
                <div class="mt-1 text-gray-500 text-sm line-clamp-1">{{ notification.target.title }}</div>
                <div class="mt-2 text-gray-400 text-xs">{{ formatDate(notification.createTime) }}</div>
              </div>
              <div v-if="!notification.read" class="ml-2 w-2 h-2 rounded-full bg-blue-500"></div>
            </div>
            
            <!-- 评论通知 -->
            <div v-else-if="notification.type === 'comment'" class="flex items-start">
              <div class="mr-3">
                <div class="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center text-green-500">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                  </svg>
                </div>
              </div>
              <div class="flex-1">
                <div class="flex items-center">
                  <span class="font-medium text-gray-800">{{ notification.actor.nickname }}</span>
                  <span class="ml-1 text-gray-600">评论了你的文章</span>
                </div>
                <div class="mt-1 text-gray-500 text-sm line-clamp-2">{{ notification.content }}</div>
                <div class="mt-2 text-gray-400 text-xs">{{ formatDate(notification.createTime) }}</div>
              </div>
              <div v-if="!notification.read" class="ml-2 w-2 h-2 rounded-full bg-blue-500"></div>
            </div>
            
            <!-- 回复通知 -->
            <div v-else-if="notification.type === 'reply'" class="flex items-start">
              <div class="mr-3">
                <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-500">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6" />
                  </svg>
                </div>
              </div>
              <div class="flex-1">
                <div class="flex items-center">
                  <span class="font-medium text-gray-800">{{ notification.actor.nickname }}</span>
                  <span class="ml-1 text-gray-600">回复了你的评论</span>
                </div>
                <div class="mt-1 text-gray-500 text-sm line-clamp-2">{{ notification.content }}</div>
                <div class="mt-2 text-gray-400 text-xs">{{ formatDate(notification.createTime) }}</div>
              </div>
              <div v-if="!notification.read" class="ml-2 w-2 h-2 rounded-full bg-blue-500"></div>
            </div>
          </div>
        </template>
        
        <!-- 无通知提示 -->
        <div v-else class="p-6 text-center text-gray-500">
          暂无通知
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn';

dayjs.extend(relativeTime);
dayjs.locale('zh-cn');

const router = useRouter();
const showPanel = ref(false);
const notifications = ref([]);
const localStorageKey = 'blog_notifications';

// 未读通知数量
const unreadCount = computed(() => {
  return notifications.value.filter(notification => !notification.read).length;
});

// 切换通知面板
const toggleNotificationPanel = () => {
  showPanel.value = !showPanel.value;
};

// 标记全部为已读
const markAllAsRead = () => {
  notifications.value = notifications.value.map(notification => ({
    ...notification,
    read: true
  }));
  
  saveNotifications();
};

// 打开通知
const openNotification = (notification) => {
  // 标记为已读
  notification.read = true;
  saveNotifications();
  
  // 跳转到相应页面
  if (notification.type === 'comment' || notification.type === 'reply') {
    router.push(`/post/${notification.target.id}#comment-${notification.commentId}`);
  } else {
    router.push(`/post/${notification.target.id}`);
  }
  
  // 关闭通知面板
  showPanel.value = false;
};

// 添加通知
const addNotification = (notification) => {
  const newNotification = {
    id: Date.now().toString(),
    createTime: new Date().toISOString(),
    read: false,
    ...notification
  };
  
  notifications.value = [newNotification, ...notifications.value].slice(0, 50); // 最多保留50条通知
  saveNotifications();
};

// 格式化日期
const formatDate = (date) => {
  return dayjs(date).fromNow();
};

// 从本地存储加载通知
const loadNotifications = () => {
  try {
    const storedNotifications = localStorage.getItem(localStorageKey);
    if (storedNotifications) {
      notifications.value = JSON.parse(storedNotifications);
    }
  } catch (error) {
    console.error('加载通知失败:', error);
  }
};

// 保存通知到本地存储
const saveNotifications = () => {
  try {
    localStorage.setItem(localStorageKey, JSON.stringify(notifications.value));
  } catch (error) {
    console.error('保存通知失败:', error);
  }
};

// 点击外部关闭通知面板
const handleClickOutside = (event) => {
  const notificationCenter = document.querySelector('.notification-center');
  if (notificationCenter && !notificationCenter.contains(event.target)) {
    showPanel.value = false;
  }
};

// 导出添加通知方法，供其他组件使用
defineExpose({
  addNotification
});

// 监听点击事件
onMounted(() => {
  loadNotifications();
  document.addEventListener('click', handleClickOutside);
});

// 在组件销毁时移除事件监听
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
.notification-center {
  position: relative;
}

.notification-panel {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 0.5rem;
  width: 400px;
  max-width: calc(100vw - 2rem);
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  z-index: 50;
}

.notification-item {
  cursor: pointer;
  transition: background-color 0.2s;
}

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style> 