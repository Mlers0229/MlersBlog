<template>
  <div class="min-h-screen flex flex-col bg-gray-50">
    <!-- 顶部导航栏 -->
    <NavBar ref="navbar" />
    
    <!-- 主内容区 -->
    <div class="container mx-auto px-4 py-8 flex-grow">
      <div class="flex flex-col md:flex-row gap-6">
        <!-- 主内容 -->
        <main class="w-full md:w-2/3">
          <slot />
        </main>
        
        <!-- 侧边栏 -->
        <aside class="w-full md:w-1/3">
          <SideBar />
        </aside>
      </div>
    </div>
    
    <!-- 页脚 -->
    <FooterBar />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useEventBus } from '@/composables/useEventBus';

// 引用导航栏
const navbar = ref(null);
const eventBus = useEventBus();

// 监听通知事件
onMounted(() => {
  const unsubscribe = eventBus.on('add-notification', (notification) => {
    // 如果导航栏中有通知中心组件，调用其添加通知方法
    if (navbar.value && navbar.value.$el.querySelector('.notification-center')) {
      const notificationCenterRef = navbar.value.$refs.notificationCenter;
      if (notificationCenterRef) {
        notificationCenterRef.addNotification(notification);
      }
    }
  });
  
  // 在组件卸载时取消订阅
  onUnmounted(() => {
    unsubscribe();
  });
});
</script> 