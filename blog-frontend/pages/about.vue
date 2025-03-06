<template>
  <div class="space-y-8">
    <!-- 关于博客 -->
    <div class="bg-white rounded-lg shadow-sm p-8">
      <h1 class="text-3xl font-bold text-gray-800 mb-6 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 mr-3 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        关于博客
      </h1>
      
      <p class="text-gray-600 mb-6 text-lg leading-relaxed">
        欢迎来到我的博客！这是一个使用Spring Boot和Nuxt.js构建的现代化个人博客系统，致力于分享编程、设计和生活中的点滴思考。
      </p>
      
      <h2 class="text-xl font-bold text-gray-800 mb-3">博客特点</h2>
      <ul class="list-disc pl-6 mb-6 text-gray-600 space-y-2">
        <li>基于Spring Boot和Nuxt.js的现代化架构</li>
        <li>响应式设计，完美适配各种设备</li>
        <li>丰富的文章管理功能</li>
        <li>用户互动系统（评论、点赞）</li>
        <li>高性能和搜索引擎优化</li>
      </ul>
      
      <h2 class="text-xl font-bold text-gray-800 mb-3">技术栈</h2>
      <div class="grid grid-cols-2 sm:grid-cols-3 gap-4 mb-6">
        <div class="bg-gray-50 p-3 rounded-lg">
          <div class="font-medium text-gray-800">前端</div>
          <div class="text-sm text-gray-600">Nuxt.js + Vue 3</div>
        </div>
        <div class="bg-gray-50 p-3 rounded-lg">
          <div class="font-medium text-gray-800">后端</div>
          <div class="text-sm text-gray-600">Spring Boot</div>
        </div>
        <div class="bg-gray-50 p-3 rounded-lg">
          <div class="font-medium text-gray-800">数据库</div>
          <div class="text-sm text-gray-600">MySQL</div>
        </div>
        <div class="bg-gray-50 p-3 rounded-lg">
          <div class="font-medium text-gray-800">样式</div>
          <div class="text-sm text-gray-600">Tailwind CSS</div>
        </div>
        <div class="bg-gray-50 p-3 rounded-lg">
          <div class="font-medium text-gray-800">状态管理</div>
          <div class="text-sm text-gray-600">Pinia</div>
        </div>
        <div class="bg-gray-50 p-3 rounded-lg">
          <div class="font-medium text-gray-800">认证</div>
          <div class="text-sm text-gray-600">JWT</div>
        </div>
      </div>
    </div>
    
    <!-- 关于作者 -->
    <div class="bg-white rounded-lg shadow-sm p-8">
      <h2 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 mr-3 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
        </svg>
        关于作者
      </h2>
      
      <div class="md:flex items-start mb-8">
        <div class="md:w-1/4 flex justify-center mb-6 md:mb-0">
          <img src="https://randomuser.me/api/portraits/men/1.jpg" alt="作者头像" class="w-40 h-40 rounded-full object-cover border-4 border-rose-100">
        </div>
        
        <div class="md:w-3/4 md:pl-8">
          <p class="text-gray-600 mb-4 leading-relaxed">
            你好，我是一名热爱技术的全栈开发者，对Web开发、用户体验设计和各种新技术充满热情。我有多年的开发经验，精通Spring Boot、Vue.js、React等技术栈。
          </p>
          
          <p class="text-gray-600 mb-4 leading-relaxed">
            除了编程，我也喜欢分享技术心得，帮助他人解决问题。这个博客是我记录学习笔记、分享经验和与志同道合者交流的平台。
          </p>
          
          <p class="text-gray-600 leading-relaxed">
            如果你有任何问题或想法，欢迎通过社交媒体或电子邮件与我联系！
          </p>
        </div>
      </div>
      
      <!-- 技能 -->
      <h3 class="text-xl font-bold text-gray-800 mb-4">技能专长</h3>
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
        <div class="space-y-2">
          <div class="flex justify-between items-center">
            <span class="text-gray-700">Java/Spring Boot</span>
            <span class="text-sm text-gray-500">90%</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-2.5">
            <div class="bg-rose-500 h-2.5 rounded-full" style="width: 90%"></div>
          </div>
        </div>
        
        <div class="space-y-2">
          <div class="flex justify-between items-center">
            <span class="text-gray-700">JavaScript/Vue</span>
            <span class="text-sm text-gray-500">85%</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-2.5">
            <div class="bg-rose-500 h-2.5 rounded-full" style="width: 85%"></div>
          </div>
        </div>
        
        <div class="space-y-2">
          <div class="flex justify-between items-center">
            <span class="text-gray-700">数据库设计</span>
            <span class="text-sm text-gray-500">80%</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-2.5">
            <div class="bg-rose-500 h-2.5 rounded-full" style="width: 80%"></div>
          </div>
        </div>
        
        <div class="space-y-2">
          <div class="flex justify-between items-center">
            <span class="text-gray-700">UI/UX设计</span>
            <span class="text-sm text-gray-500">75%</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-2.5">
            <div class="bg-rose-500 h-2.5 rounded-full" style="width: 75%"></div>
          </div>
        </div>
      </div>
      
      <!-- 联系方式 -->
      <h3 class="text-xl font-bold text-gray-800 mb-4">联系我</h3>
      <div class="flex flex-wrap gap-4">
        <a href="#" class="flex items-center px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded-full text-gray-700">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
          </svg>
          Email
        </a>
        <a href="#" class="flex items-center px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded-full text-gray-700">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
            <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" />
          </svg>
          GitHub
        </a>
        <a href="#" class="flex items-center px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded-full text-gray-700">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
            <path d="M23.953 4.57a10 10 0 01-2.825.775 4.958 4.958 0 002.163-2.723c-.951.555-2.005.959-3.127 1.184a4.92 4.92 0 00-8.384 4.482C7.69 8.095 4.067 6.13 1.64 3.162a4.822 4.822 0 00-.666 2.475c0 1.71.87 3.213 2.188 4.096a4.904 4.904 0 01-2.228-.616v.06a4.923 4.923 0 003.946 4.827 4.996 4.996 0 01-2.212.085 4.936 4.936 0 004.604 3.417 9.867 9.867 0 01-6.102 2.105c-.39 0-.779-.023-1.17-.067a13.995 13.995 0 007.557 2.209c9.053 0 13.998-7.496 13.998-13.985 0-.21 0-.42-.015-.63A9.935 9.935 0 0024 4.59z" />
          </svg>
          Twitter
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
// "关于"页面组件逻辑
</script> 