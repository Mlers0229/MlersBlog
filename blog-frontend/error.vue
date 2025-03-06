<template>
  <div class="min-h-screen flex flex-col justify-center items-center px-4 py-16 bg-gray-50">
    <div class="bg-white p-8 rounded-lg shadow-sm max-w-lg w-full text-center">
      <div class="mb-6">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-24 w-24 mx-auto text-rose-400"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
          />
        </svg>
      </div>

      <h1 class="text-2xl font-bold text-gray-800 mb-2">
        {{ error.statusCode === 404 ? '页面未找到' : '发生错误' }}
      </h1>
      
      <p class="text-gray-600 mb-6">
        {{ error.statusCode === 404 
          ? '很抱歉，您要查找的页面不存在或已被移除。' 
          : error.message || '发生了一个意外错误，请稍后再试。' 
        }}
      </p>

      <div class="flex flex-col sm:flex-row justify-center gap-4">
        <button
          @click="handleError"
          class="px-4 py-2 bg-rose-400 text-white rounded-full hover:bg-rose-500 transition-colors"
        >
          返回首页
        </button>
        
        <button
          @click="navigateTo('/contact')"
          class="px-4 py-2 bg-gray-100 text-gray-700 rounded-full hover:bg-gray-200 transition-colors"
        >
          联系我们
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  error: Object
});

const handleError = () => {
  clearError({ redirect: '/' });
};
</script> 