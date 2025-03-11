<template>
  <div>
    <!-- 加载状态 -->
    <div v-if="loading" class="container mx-auto px-4 py-12">
      <div class="animate-pulse">
        <div class="h-6 bg-gray-200 rounded-md w-3/4 mb-4"></div>
        <div class="h-4 bg-gray-200 rounded-md w-1/2 mb-8"></div>
        <div class="h-64 bg-gray-200 rounded-md mb-4"></div>
        <div class="h-4 bg-gray-200 rounded-md w-full mb-2"></div>
        <div class="h-4 bg-gray-200 rounded-md w-full mb-2"></div>
        <div class="h-4 bg-gray-200 rounded-md w-3/4 mb-2"></div>
      </div>
    </div>
    
    <!-- 文章不存在提示 -->
    <div v-else-if="!post" class="container mx-auto px-4 py-12 text-center">
      <div class="text-4xl font-bold text-gray-700 mb-2">404</div>
      <div class="text-xl text-gray-500 mb-8">文章不存在或已被删除</div>
      <NuxtLink to="/" class="bg-rose-500 hover:bg-rose-600 text-white px-6 py-2 rounded-md font-medium">
        返回首页
      </NuxtLink>
    </div>
    
    <!-- 文章详情 -->
    <div v-else class="container mx-auto px-4 py-8">
      <div class="max-w-3xl mx-auto">
        <!-- 文章标题和信息 -->
        <div class="mb-8">
          <h1 class="text-3xl md:text-4xl font-bold text-gray-800 mb-4">{{ post.title }}</h1>
          
          <div class="flex flex-wrap items-center text-gray-600 text-sm">
            <!-- 作者信息 -->
            <div class="flex items-center mr-6 mb-2">
              <img 
                :src="post.author?.avatar || 'https://ui-avatars.com/api/?name=' + encodeURIComponent(post.author?.nickname || 'User')" 
                :alt="post.author?.nickname || '作者'" 
                class="w-8 h-8 rounded-full mr-2"
              >
              <span>{{ post.author?.nickname || '未知作者' }}</span>
            </div>
            
            <!-- 发布日期 -->
            <div class="mr-6 mb-2 flex items-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              <span>{{ formatDate(post.createdAt) }}</span>
            </div>
            
            <!-- 标签列表 -->
            <div class="flex flex-wrap mb-2">
              <NuxtLink 
                v-for="tag in post.tags" 
                :key="tag.id" 
                :to="`/tags/${tag.id}`"
                class="bg-gray-100 text-gray-700 px-2 py-1 rounded-md text-xs mr-2 mb-2 hover:bg-gray-200"
              >
                # {{ tag.name }}
              </NuxtLink>
            </div>
          </div>
        </div>
        
        <!-- 文章内容 - 使用渲染后的Markdown HTML -->
        <div class="prose prose-rose max-w-none" v-html="post.contentHtml"></div>
        
        <!-- 底部互动区 -->
        <div class="mt-8 border-t border-gray-200 pt-8">
          <div class="flex items-center justify-between flex-wrap">
            <div class="flex items-center space-x-4">
              <!-- 点赞按钮 -->
              <div class="relative">
                <button 
                  @click="toggleLike" 
                  class="flex items-center space-x-1 text-gray-600 hover:text-blue-500 px-3 py-2 rounded-md"
                  :class="{ 'text-blue-500 bg-blue-50': post.isLiked }"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" :class="{'fill-current': post.isLiked}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" />
                  </svg>
                  <span>{{ post.likes || 0 }} 赞</span>
                </button>
                
                <!-- 点赞用户按钮 -->
                <button 
                  v-if="post.likes > 0"
                  @click="showLikedUsers" 
                  class="text-gray-500 hover:text-blue-500 text-sm ml-2"
                >
                  查看
                </button>
              </div>
              
              <!-- 收藏按钮 -->
              <div class="relative">
                <button 
                  @click="toggleFavorite" 
                  class="flex items-center space-x-1 text-gray-600 hover:text-yellow-500 px-3 py-2 rounded-md"
                  :class="{ 'text-yellow-500 bg-yellow-50': post.isFavorited }"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" :class="{'fill-current': post.isFavorited}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" />
                  </svg>
                  <span>{{ post.favorites || 0 }} 收藏</span>
                </button>
                
                <!-- 收藏用户按钮 -->
                <button 
                  v-if="post.favorites > 0"
                  @click="showFavoritedUsers" 
                  class="text-gray-500 hover:text-yellow-500 text-sm ml-2"
                >
                  查看
                </button>
              </div>
              
              <!-- 分享按钮 -->
              <button 
                @click="sharePost" 
                class="flex items-center space-x-1 text-gray-600 hover:text-green-500 px-3 py-2 rounded-md"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z" />
                </svg>
                <span>分享</span>
              </button>
            </div>
            
            <div>
              <span class="text-gray-500 flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
                {{ post.viewCount || 0 }} 阅读
              </span>
            </div>
          </div>
        </div>

        <!-- 评论区 -->
        <div class="mt-8">
          <CommentList :post-id="post.id" :post-title="post.title" :post-author-id="post.author?.id" />
        </div>
      </div>
    </div>
    
    <!-- 用户列表弹窗 -->
    <div v-if="showUserModal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50" @click="showUserModal = false">
      <div @click.stop>
        <UserList 
          :users="userList" 
          :title="userListTitle" 
          :type="userListType" 
          @close="showUserModal = false" 
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useInteractionStore } from '@/stores/interaction';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale';
import MarkdownIt from 'markdown-it';
import CommentList from '@/components/comment/CommentList.vue';
import UserList from '@/components/UserList.vue';
import { useEventBus } from '@/composables/useEventBus';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const interactionStore = useInteractionStore();
const eventBus = useEventBus();

const loading = ref(true);
const post = ref(null);

// Markdown渲染器 - 不使用有问题的PrismJS插件
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true,
  highlight: function (str, lang) {
    // 简单的语法高亮包装器，不依赖Prism
    if (lang && lang.length > 0) {
      return `<pre class="language-${lang}"><code class="language-${lang}">${md.utils.escapeHtml(str)}</code></pre>`;
    }
    return `<pre><code>${md.utils.escapeHtml(str)}</code></pre>`;
  }
});

// 专门用于渲染Markdown的函数，确保代码语法高亮和链接处理的正确性
const renderMarkdown = async () => {
  if (!post.value || !post.value.content) return;
  
  // 如果已经有渲染好的HTML内容且非空，直接使用
  if (post.value.contentHtml && post.value.contentHtml.trim().length > 0) {
    console.log('使用已有的HTML内容');
    return;
  }
  
  try {
    console.log('渲染Markdown内容');
    // 渲染Markdown内容为HTML
    post.value.contentHtml = md.render(post.value.content || '');
    
    // 等待DOM更新
    await nextTick();
    
    // 处理代码块和链接
    if (process.client) {
      // 添加复制按钮到代码块
      const codeBlocks = document.querySelectorAll('.prose pre');
      codeBlocks.forEach((pre) => {
        // 如果已经有复制按钮，跳过
        if (pre.querySelector('.copy-code-btn')) return;
        
        // 创建复制按钮
        const copyBtn = document.createElement('button');
        copyBtn.className = 'absolute top-2 right-2 bg-white/80 rounded-md px-2 py-1 text-xs text-gray-700 hover:bg-white copy-code-btn';
        copyBtn.innerText = '复制';
        copyBtn.addEventListener('click', () => {
          const code = pre.querySelector('code')?.textContent || '';
          navigator.clipboard.writeText(code)
            .then(() => {
              copyBtn.innerText = '已复制！';
              setTimeout(() => {
                copyBtn.innerText = '复制';
              }, 2000);
            })
            .catch(err => {
              console.error('复制失败:', err);
            });
        });
        
        // 确保pre元素有相对定位，以便放置复制按钮
        if (getComputedStyle(pre).position !== 'relative') {
          pre.style.position = 'relative';
        }
        pre.appendChild(copyBtn);
      });
      
      // 处理文章中的链接，添加target="_blank"
      const links = document.querySelectorAll('.prose a');
      links.forEach(link => {
        // 排除锚点链接
        if (link.getAttribute('href')?.startsWith('#')) return;
        
        if (!link.getAttribute('target')) {
          link.setAttribute('target', '_blank');
          link.setAttribute('rel', 'noopener noreferrer');
        }
      });
    }
  } catch (error) {
    console.error('渲染Markdown失败:', error);
    // 如果渲染失败，至少显示原始内容
    post.value.contentHtml = `<pre>${post.value.content || ''}</pre>`;
  }
};

// 获取文章详情
const fetchPost = async () => {
  loading.value = true;
  
  try {
    // 从API获取文章详情
    const { posts: postApi } = await import('~/api/post');
    const response = await postApi.getPostById(route.params.id);
    
    post.value = response;
    await renderMarkdown();
    
    // 在文章数据加载完成后，设置点赞和收藏状态
    if (post.value) {
      // 如果用户登录了，获取点赞和收藏状态
      if (authStore.isLoggedIn) {
        const userId = authStore.user?.id || 1;
        post.value.isLiked = interactionStore.isPostLiked(post.value.id, userId);
        post.value.isFavorited = interactionStore.isPostFavorited(post.value.id, userId);
      }
      
      // 获取点赞和收藏数量
      post.value.likes = interactionStore.getPostLikeCount(post.value.id);
      post.value.favorites = interactionStore.getPostFavoriteCount(post.value.id);
    }
  } catch (error) {
    console.error('获取文章详情失败:', error);
    showError({ statusCode: 404, message: '文章不存在或已被删除' });
  } finally {
    loading.value = false;
  }
};

// 点赞功能
const toggleLike = async () => {
  if (!authStore.isLoggedIn) {
    router.push('/login?redirect=' + route.fullPath);
    return;
  }
  
  try {
    if (post.value) {
      const userId = authStore.user?.id || 1; // 默认ID
      const userInfo = authStore.user || {
        id: userId,
        nickname: '当前用户',
        avatar: null
      };
      
      // 使用交互存储切换点赞状态
      const result = interactionStore.togglePostLike(post.value.id, userId, userInfo);
      
      // 更新UI状态
      post.value.isLiked = result.isLiked;
      post.value.likes = result.count;
      
      // 如果是点赞（而不是取消点赞），且当前用户不是文章作者，发送通知
      if (result.isLiked && post.value.author && post.value.author.id !== userId) {
        eventBus.emit('add-notification', {
          type: 'like',
          actor: authStore.user,
          target: {
            id: post.value.id,
            title: post.value.title
          }
        });
      }
      
      // 尝试调用API更新点赞状态
      try {
        const response = await $fetch(`/api/posts/${post.value.id}/like`, {
          method: post.value.isLiked ? 'POST' : 'DELETE',
          headers: {
            Authorization: `Bearer ${authStore.getToken()}`
          }
        });
        
        if (response.code !== 200) {
          console.log('API请求失败，但本地存储已更新');
        }
      } catch (apiError) {
        console.error('点赞API调用失败，使用本地存储:', apiError);
      }
    }
  } catch (error) {
    console.error('点赞操作失败:', error);
  }
};

// 收藏功能
const toggleFavorite = async () => {
  if (!authStore.isLoggedIn) {
    router.push('/login?redirect=' + route.fullPath);
    return;
  }
  
  try {
    if (post.value) {
      const userId = authStore.user?.id || 1; // 默认ID
      const userInfo = authStore.user || {
        id: userId,
        nickname: '当前用户',
        avatar: null
      };
      
      // 使用交互存储切换收藏状态
      const result = interactionStore.togglePostFavorite(post.value.id, userId, userInfo);
      
      // 更新UI状态
      post.value.isFavorited = result.isFavorited;
      post.value.favorites = result.count;
      
      // 如果是收藏（而不是取消收藏），且当前用户不是文章作者，发送通知
      if (result.isFavorited && post.value.author && post.value.author.id !== userId) {
        eventBus.emit('add-notification', {
          type: 'favorite',
          actor: authStore.user,
          target: {
            id: post.value.id,
            title: post.value.title
          }
        });
      }
      
      // 尝试调用API更新收藏状态
      try {
        const response = await $fetch(`/api/posts/${post.value.id}/favorite`, {
          method: post.value.isFavorited ? 'POST' : 'DELETE',
          headers: {
            Authorization: `Bearer ${authStore.getToken()}`
          }
        });
        
        if (response.code !== 200) {
          console.log('API请求失败，但本地存储已更新');
        }
      } catch (apiError) {
        console.error('收藏API调用失败，使用本地存储:', apiError);
      }
    }
  } catch (error) {
    console.error('收藏操作失败:', error);
  }
};

// 分享功能
const sharePost = () => {
  if (navigator.share) {
    navigator.share({
      title: post.value?.title,
      text: post.value?.summary,
      url: window.location.href
    }).catch(error => {
      console.error('分享失败:', error);
    });
  } else {
    // 复制链接到剪贴板
    navigator.clipboard.writeText(window.location.href)
      .then(() => alert('链接已复制到剪贴板'))
      .catch(() => alert('复制链接失败，请手动复制浏览器地址'));
  }
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  const now = new Date();
  const diff = now - date;
  
  // 1分钟内
  if (diff < 60 * 1000) {
    return '刚刚';
  }
  
  // 1小时内
  if (diff < 60 * 60 * 1000) {
    return `${Math.floor(diff / (60 * 1000))}分钟前`;
  }
  
  // 24小时内
  if (diff < 24 * 60 * 60 * 1000) {
    return `${Math.floor(diff / (60 * 60 * 1000))}小时前`;
  }
  
  // 30天内
  if (diff < 30 * 24 * 60 * 60 * 1000) {
    return `${Math.floor(diff / (24 * 60 * 60 * 1000))}天前`;
  }
  
  // 年月日格式
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  
  // 如果是当年，只显示月日
  if (year === now.getFullYear()) {
    return `${month}月${day}日`;
  }
  
  return `${year}年${month}月${day}日`;
};

// 判断当前用户是否为文章作者
const isAuthor = computed(() => {
  if (!authStore.isLoggedIn || !post.value || !post.value.author) {
    return false;
  }
  
  // 在实际应用中，应该比较用户ID和作者ID
  // 为了演示，我们假设用户已登录则为作者
  return authStore.isLoggedIn;
});

// 用户列表弹窗
const showUserModal = ref(false);
const userList = ref([]);
const userListTitle = ref('');
const userListType = ref('like');

// 显示点赞用户列表
const showLikedUsers = () => {
  if (!post.value) return;
  
  userList.value = interactionStore.getPostLikeUsers(post.value.id);
  userListTitle.value = '点赞用户';
  userListType.value = 'like';
  showUserModal.value = true;
};

// 显示收藏用户列表
const showFavoritedUsers = () => {
  if (!post.value) return;
  
  userList.value = interactionStore.getPostFavoriteUsers(post.value.id);
  userListTitle.value = '收藏用户';
  userListType.value = 'favorite';
  showUserModal.value = true;
};

onMounted(() => {
  fetchPost();
});
</script>

<style scoped>
/* 可以添加文章详情页的特定样式 */
:deep(.prose) {
  max-width: none;
}

:deep(.prose img) {
  margin: 1.5em auto;
  border-radius: 0.5rem;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  max-width: 100%;
  height: auto;
}

:deep(.prose pre) {
  background-color: #f8f9fa;
  border-radius: 0.5rem;
  padding: 1rem;
  overflow-x: auto;
  position: relative; /* 确保复制按钮定位正确 */
  margin: 1.5em 0;
}

:deep(.prose code) {
  color: #e53e3e;
  background-color: #f8f9fa;
  padding: 0.2em 0.4em;
  border-radius: 0.25rem;
  font-family: monospace;
}

/* 添加简单的代码高亮样式 */
:deep(.language-javascript),
:deep(.language-js) {
  color: #333;
}

:deep(.language-javascript .copy-code-btn),
:deep(.language-js .copy-code-btn) {
  color: #666;
}

:deep(.language-html) {
  color: #e34c26;
}

:deep(.language-css) {
  color: #563d7c;
}

:deep(.language-vue) {
  color: #42b883;
}

:deep(.prose blockquote) {
  border-left-color: #e53e3e;
  color: #4a5568;
  font-style: italic;
}

:deep(.prose a) {
  color: #e53e3e;
  text-decoration: none;
  border-bottom: 1px dotted #e53e3e;
}

:deep(.prose a:hover) {
  border-bottom: 1px solid #e53e3e;
}

:deep(.prose h2) {
  color: #2d3748;
  margin-top: 2em;
  margin-bottom: 0.8em;
  font-weight: 700;
}

:deep(.prose h3) {
  color: #4a5568;
  margin-top: 1.6em;
  margin-bottom: 0.6em;
  font-weight: 600;
}

/* 添加复制按钮样式 */
:deep(.copy-code-btn) {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
  border-radius: 0.25rem;
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
  cursor: pointer;
  color: #666;
  transition: all 0.2s;
}

:deep(.copy-code-btn:hover) {
  background-color: rgba(255, 255, 255, 1);
  color: #e53e3e;
}
</style>