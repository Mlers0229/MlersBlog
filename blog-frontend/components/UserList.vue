<template>
  <div class="user-list">
    <!-- 标题 -->
    <div class="flex items-center justify-between mb-4">
      <h3 class="text-lg font-semibold text-gray-800">
        {{ title }} ({{ users.length }})
      </h3>
      <button 
        @click="$emit('close')" 
        class="text-gray-400 hover:text-gray-600"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>
    
    <!-- 用户列表 -->
    <div class="overflow-y-auto max-h-60">
      <div v-if="users.length === 0" class="text-center py-4 text-gray-500">
        暂无{{ type === 'like' ? '点赞' : '收藏' }}用户
      </div>
      
      <div 
        v-for="user in users" 
        :key="user.id"
        class="flex items-center py-2 border-b border-gray-100 last:border-b-0"
      >
        <img 
          :src="user.avatar || '/img/default-avatar.png'" 
          alt="用户头像" 
          class="w-10 h-10 rounded-full mr-3"
        />
        <div>
          <p class="font-medium text-gray-800">{{ user.nickname }}</p>
          <p class="text-xs text-gray-500">ID: {{ user.id }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  users: {
    type: Array,
    default: () => []
  },
  title: {
    type: String,
    default: '用户列表'
  },
  type: {
    type: String,
    default: 'like', // 'like' 或 'favorite'
    validator: value => ['like', 'favorite'].includes(value)
  }
});

defineEmits(['close']);
</script>

<style scoped>
.user-list {
  width: 100%;
  max-width: 400px;
  background-color: white;
  border-radius: 0.5rem;
  padding: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}
</style> 