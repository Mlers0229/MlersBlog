<template>
  <div class="space-y-6">
    <!-- 关于作者卡片 -->
    <div class="bg-white rounded-lg shadow-sm p-6">
      <div class="flex flex-col items-center">
        <img src="https://randomuser.me/api/portraits/men/1.jpg" alt="作者头像" class="h-24 w-24 rounded-full object-cover border-4 border-rose-100 mb-4">
        <h3 class="text-xl font-bold text-gray-800 mb-2">博主</h3>
        <p class="text-gray-600 text-center mb-4">全栈开发者、设计爱好者、写作者</p>
        <div class="flex space-x-4">
          <a href="#" target="_blank" class="text-gray-500 hover:text-rose-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" />
            </svg>
          </a>
          <a href="#" target="_blank" class="text-gray-500 hover:text-rose-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
              <path d="M23.953 4.57a10 10 0 01-2.825.775 4.958 4.958 0 002.163-2.723c-.951.555-2.005.959-3.127 1.184a4.92 4.92 0 00-8.384 4.482C7.69 8.095 4.067 6.13 1.64 3.162a4.822 4.822 0 00-.666 2.475c0 1.71.87 3.213 2.188 4.096a4.904 4.904 0 01-2.228-.616v.06a4.923 4.923 0 003.946 4.827 4.996 4.996 0 01-2.212.085 4.936 4.936 0 004.604 3.417 9.867 9.867 0 01-6.102 2.105c-.39 0-.779-.023-1.17-.067a13.995 13.995 0 007.557 2.209c9.053 0 13.998-7.496 13.998-13.985 0-.21 0-.42-.015-.63A9.935 9.935 0 0024 4.59z" />
            </svg>
          </a>
          <a href="#" target="_blank" class="text-gray-500 hover:text-rose-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z" />
            </svg>
          </a>
        </div>
      </div>
    </div>
    
    <!-- 博客统计信息 -->
    <div class="bg-white rounded-lg shadow-sm p-6">
      <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
        </svg>
        统计信息
      </h3>
      <div class="grid grid-cols-2 gap-4">
        <div class="text-center">
          <div class="text-2xl font-bold text-rose-500">128</div>
          <div class="text-sm text-gray-600">文章数</div>
        </div>
        <div class="text-center">
          <div class="text-2xl font-bold text-rose-500">56</div>
          <div class="text-sm text-gray-600">分类数</div>
        </div>
        <div class="text-center">
          <div class="text-2xl font-bold text-rose-500">328</div>
          <div class="text-sm text-gray-600">标签数</div>
        </div>
        <div class="text-center">
          <div class="text-2xl font-bold text-rose-500">10.2k</div>
          <div class="text-sm text-gray-600">总访问量</div>
        </div>
      </div>
    </div>
    
    <!-- 热门文章 -->
    <div class="bg-white rounded-lg shadow-sm p-6">
      <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
        </svg>
        热门文章
      </h3>
      <div class="space-y-4">
        <div v-for="i in 5" :key="i" class="group">
          <NuxtLink to="/post/1" class="flex items-center space-x-3">
            <span class="text-gray-500 text-sm">{{ i }}</span>
            <span class="text-gray-700 group-hover:text-rose-500 line-clamp-1">如何使用Nuxt 3构建高性能博客系统</span>
          </NuxtLink>
        </div>
      </div>
    </div>
    
    <!-- 标签云 -->
    <div class="bg-white rounded-lg shadow-sm p-6">
      <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
        </svg>
        标签云
      </h3>
      <div class="flex flex-wrap gap-2">
        <NuxtLink to="/tags/nuxt" class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-rose-100 hover:text-rose-600">Nuxt.js</NuxtLink>
        <NuxtLink to="/tags/vue" class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-rose-100 hover:text-rose-600">Vue.js</NuxtLink>
        <NuxtLink to="/tags/javascript" class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-rose-100 hover:text-rose-600">JavaScript</NuxtLink>
        <NuxtLink to="/tags/css" class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-rose-100 hover:text-rose-600">CSS</NuxtLink>
        <NuxtLink to="/tags/spring" class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-rose-100 hover:text-rose-600">Spring Boot</NuxtLink>
        <NuxtLink to="/tags/java" class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-rose-100 hover:text-rose-600">Java</NuxtLink>
        <NuxtLink to="/tags/database" class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-rose-100 hover:text-rose-600">数据库</NuxtLink>
        <NuxtLink to="/tags/tailwind" class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm hover:bg-rose-100 hover:text-rose-600">Tailwind</NuxtLink>
      </div>
    </div>
    
    <!-- 最新评论 -->
    <div class="bg-white rounded-lg shadow-sm p-6">
      <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
        </svg>
        最新评论
      </h3>
      <div class="space-y-4">
        <div v-for="i in 3" :key="i" class="border-b border-gray-100 pb-4 last:border-b-0 last:pb-0">
          <div class="flex items-center mb-2">
            <img src="https://randomuser.me/api/portraits/men/12.jpg" alt="头像" class="h-6 w-6 rounded-full mr-2">
            <span class="text-sm font-medium text-gray-800">用户名</span>
            <span class="text-xs text-gray-500 ml-2">2小时前</span>
          </div>
          <p class="text-sm text-gray-600 line-clamp-2">这篇文章写得非常好，对我学习Nuxt.js很有帮助...</p>
          <NuxtLink to="/post/1" class="text-xs text-rose-500 hover:text-rose-600 mt-1 inline-block">来自: 如何使用Nuxt 3构建高性能博客</NuxtLink>
        </div>
      </div>
    </div>
    
    <!-- 友情链接 -->
    <div class="bg-white rounded-lg shadow-sm p-6">
      <h3 class="text-lg font-bold text-gray-800 mb-4 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
        </svg>
        友情链接
      </h3>
      <div class="flex flex-wrap gap-3">
        <a href="#" target="_blank" class="text-sm text-gray-600 hover:text-rose-500">Vue.js官网</a>
        <a href="#" target="_blank" class="text-sm text-gray-600 hover:text-rose-500">Nuxt.js官网</a>
        <a href="#" target="_blank" class="text-sm text-gray-600 hover:text-rose-500">Tailwind CSS</a>
        <a href="#" target="_blank" class="text-sm text-gray-600 hover:text-rose-500">Spring Boot</a>
      </div>
    </div>
  </div>
</template>

<script setup>
// 侧边栏组件逻辑
</script> 