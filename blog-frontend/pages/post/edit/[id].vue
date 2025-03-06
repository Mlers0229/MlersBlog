<template>
  <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
      </svg>
      编辑文章
    </h1>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="space-y-6 animate-pulse">
      <div class="h-10 bg-gray-200 rounded w-3/4 mb-6"></div>
      <div class="h-24 bg-gray-200 rounded mb-6"></div>
      <div class="flex flex-col md:flex-row gap-6">
        <div class="w-full md:w-1/2">
          <div class="h-10 bg-gray-200 rounded w-full"></div>
        </div>
        <div class="w-full md:w-1/2">
          <div class="h-10 bg-gray-200 rounded w-full"></div>
        </div>
      </div>
      <div class="h-40 bg-gray-200 rounded mb-6"></div>
      <div class="h-64 bg-gray-200 rounded mb-6"></div>
    </div>
    
    <!-- 编辑表单 -->
    <div v-else-if="post" class="space-y-6">
      <div>
        <label class="block text-gray-700 font-medium mb-2">文章标题</label>
        <input 
          v-model="post.title"
          type="text" 
          placeholder="输入文章标题"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-300 focus:border-transparent"
        />
      </div>
      
      <div>
        <label class="block text-gray-700 font-medium mb-2">文章摘要</label>
        <textarea 
          v-model="post.summary"
          placeholder="输入文章摘要"
          rows="3"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-300 focus:border-transparent"
        ></textarea>
      </div>
      
      <div class="flex flex-col md:flex-row gap-6">
        <div class="w-full md:w-1/2">
          <label class="block text-gray-700 font-medium mb-2">分类</label>
          <select 
            v-model="post.categoryId"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-300 focus:border-transparent"
          >
            <option value="">选择分类</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        
        <div class="w-full md:w-1/2">
          <label class="block text-gray-700 font-medium mb-2">标签</label>
          <input 
            v-model="tagsInput"
            type="text" 
            placeholder="输入标签，用逗号分隔"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-300 focus:border-transparent"
          />
        </div>
      </div>
      
      <div>
        <label class="block text-gray-700 font-medium mb-2">封面图</label>
        <div 
          class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center cursor-pointer hover:border-rose-300 transition-colors"
          @click="triggerFileInput"
        >
          <div v-if="!coverImagePreview">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mx-auto text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            <p class="mt-2 text-gray-500">点击或拖拽上传图片</p>
          </div>
          <div v-else class="relative">
            <img :src="coverImagePreview" alt="封面预览" class="max-h-48 mx-auto rounded-lg" />
            <button 
              class="absolute top-2 right-2 bg-white rounded-full p-1 shadow-md hover:bg-gray-100"
              @click.stop="removeCoverImage"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>
        <input 
          ref="fileInput"
          type="file" 
          accept="image/*"
          class="hidden"
          @change="handleFileChange"
        />
      </div>
      
      <div>
        <label class="block text-gray-700 font-medium mb-2">文章内容</label>
        <!-- Editor.md Markdown编辑器 -->
        <div id="editormd-container" class="border border-gray-300 rounded-lg overflow-hidden"></div>
      </div>
      
      <div class="flex justify-end gap-4">
        <button 
          class="px-6 py-2 bg-gray-200 text-gray-700 rounded-full hover:bg-gray-300 transition-colors"
          @click="saveDraft"
          :disabled="saving"
        >
          {{ saving ? '保存中...' : '保存草稿' }}
        </button>
        <button 
          class="px-6 py-2 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors disabled:bg-rose-300"
          @click="updatePost"
          :disabled="updating || !post.title || !post.content"
        >
          {{ updating ? '更新中...' : '更新文章' }}
        </button>
      </div>
    </div>
    
    <!-- 文章不存在 -->
    <div v-else class="text-center py-16">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
      </svg>
      <h3 class="mt-4 text-xl font-medium text-gray-800">文章不存在</h3>
      <p class="mt-2 text-gray-600">找不到要编辑的文章，或者您没有权限编辑此文章</p>
      <div class="mt-6">
        <NuxtLink to="/" class="px-6 py-2 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition-colors inline-block">
          返回首页
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRoute, useRouter } from 'vue-router';

const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();
const fileInput = ref(null);

// 状态
const loading = ref(true);
const saving = ref(false);
const updating = ref(false);
const coverImagePreview = ref('');
const categories = ref([
  { id: 1, name: '技术' },
  { id: 2, name: '设计' },
  { id: 3, name: '生活' }
]);
const tagsInput = ref('');
const postId = ref(null);

// 文章数据
const post = ref(null);

// Editor.md 实例
let editor = null;

// 获取文章详情
const fetchPost = async () => {
  loading.value = true;
  postId.value = route.params.id;

  try {
    console.log(`开始获取文章 ID: ${postId.value}`);
    
    // 尝试从本地存储获取文章数据
    if (process.client && window.localStorage) {
      try {
        const mockPosts = JSON.parse(localStorage.getItem('mockPosts') || '[]');
        const mockPost = mockPosts.find(p => p.id.toString() === postId.value.toString());
        
        if (mockPost) {
          console.log('从localStorage获取到文章:', mockPost);
          post.value = { ...mockPost };
          
          // 设置标签输入
          tagsInput.value = (post.value.tags || []).map(tag => typeof tag === 'string' ? tag : tag.name).join(',');
          
          // 设置封面图预览
          if (post.value.coverImage) {
            coverImagePreview.value = post.value.coverImage;
          }
          
          // 初始化编辑器
          await initEditor();
          loading.value = false;
          return;
        }
      } catch (error) {
        console.error('从localStorage获取文章失败:', error);
      }
    }
    
    // 如果本地存储中没有找到，则尝试通过API获取
    try {
      const response = await $fetch(`/api/posts/${postId.value}`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        }
      });
      
      if (response.code === 200 && response.data) {
        console.log('从API获取文章成功:', response.data);
        post.value = response.data;
        
        // 设置标签输入
        tagsInput.value = (post.value.tags || []).map(tag => typeof tag === 'string' ? tag : tag.name).join(',');
        
        // 设置封面图预览
        if (post.value.coverImage) {
          coverImagePreview.value = post.value.coverImage;
        }
        
        // 初始化编辑器
        await initEditor();
        loading.value = false;
        return;
      }
    } catch (error) {
      console.error('从API获取文章失败:', error);
    }
    
    // 如果API也失败，则模拟一些数据
    console.log('使用模拟文章数据');
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // 模拟文章数据 - 这里模拟的是Markdown格式的内容
    post.value = {
      id: postId.value,
      title: '如何使用Nuxt 3构建高性能博客系统',
      summary: 'Nuxt 3是Vue 3的服务器端渲染框架，本文将详细介绍如何使用Nuxt 3构建一个功能完善的博客系统。',
      content: '## 什么是Nuxt 3?\n\nNuxt 3是一个基于Vue 3的服务器端渲染框架...\n\n## 为什么选择Nuxt 3?\n\nNuxt 3提供了很多优势：服务器端渲染，文件系统路由，自动导入等...',
      categoryId: 1,
      tags: ['Vue.js', 'Nuxt', 'JavaScript'],
      coverImage: 'https://picsum.photos/800/400',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      author: {
        id: 1,
        name: '张三',
        avatar: 'https://randomuser.me/api/portraits/men/1.jpg'
      }
    };
    
    // 设置封面图预览
    if (post.value.coverImage) {
      coverImagePreview.value = post.value.coverImage;
    }
    
    // 设置标签输入
    tagsInput.value = post.value.tags.join(',');
    
    // 初始化编辑器
    await initEditor();
  } catch (error) {
    console.error('获取文章失败:', error);
    post.value = null;
  } finally {
    loading.value = false;
  }
};

// 初始化 Editor.md
const initEditor = async () => {
  if (process.client && post.value) {
    try {
      console.log('开始初始化编辑器，文章内容：', post.value.content);
      
      // 动态引入jQuery
      const jQuery = await import('jquery');
      window.jQuery = jQuery.default;
      window.$ = jQuery.default;
      
      // 使用更可靠的CDN加载editor.md
      if (!window.editormd) {
        console.log('加载editor.md资源');
        // 添加editor.md的CSS
        const editormdCss = document.createElement('link');
        editormdCss.rel = 'stylesheet';
        editormdCss.href = 'https://cdnjs.cloudflare.com/ajax/libs/editor.md/1.5.0/css/editormd.min.css';
        document.head.appendChild(editormdCss);
        
        // 加载editor.md的主JS文件
        const editormdScript = document.createElement('script');
        editormdScript.src = 'https://cdnjs.cloudflare.com/ajax/libs/editor.md/1.5.0/editormd.min.js';
        editormdScript.onload = () => {
          console.log('editor.md加载完成，初始化编辑器实例');
          initEditorInstance();
        };
        document.body.appendChild(editormdScript);
      } else {
        console.log('editor.md已加载，直接初始化编辑器实例');
        initEditorInstance();
      }
    } catch (error) {
      console.error('初始化编辑器失败:', error);
    }
  }
};

// 创建编辑器实例
const initEditorInstance = () => {
  console.log('initEditorInstance被调用');
  try {
    // 确保DOM元素已准备好
    const container = document.getElementById('editormd-container');
    if (!container) {
      console.error('找不到编辑器容器元素');
      return;
    }
    
    // 确保有markdown内容加载
    const markdownContent = post.value.content || '';
    console.log('加载到编辑器的内容:', markdownContent);
    
    // 创建编辑器
    editor = window.editormd("editormd-container", {
      width: "100%",
      height: 500,
      path: "https://cdnjs.cloudflare.com/ajax/libs/editor.md/1.5.0/lib/",  // 使用更可靠的CDN路径
      watch: true, // 开启实时预览
      placeholder: "请输入文章内容...",
      theme: "default",
      editorTheme: "default",
      previewTheme: "default",
      saveHTMLToTextarea: true,
      markdown: markdownContent,
      toolbarIcons: function() {
        return [
          "undo", "redo", "|", 
          "bold", "italic", "quote", "|", 
          "h1", "h2", "h3", "h4", "h5", "h6", "|", 
          "list-ul", "list-ol", "hr", "|",
          "link", "image", "code", "code-block", "table", "|",
          "watch", "preview", "fullscreen", "|",
          "help"
        ];
      },
      imageUpload: true,
      imageFormats: ["jpg", "jpeg", "gif", "png", "webp"],
      imageUploadURL: "/api/upload/image", // 图片上传接口
      onload: function() {
        console.log('editor.md加载完成，设置内容');
        
        // 确保内容被加载
        if (markdownContent) {
          this.setMarkdown(markdownContent);
        }
        
        // 监听编辑器内容变化
        this.on("change", function() {
          if (post.value) {
            post.value.content = this.getMarkdown();
          }
        });
      }
    });
    
    console.log('编辑器实例创建成功');
  } catch (error) {
    console.error('创建编辑器实例失败:', error);
  }
};

// 触发文件上传
const triggerFileInput = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};

// 处理文件选择
const handleFileChange = (e) => {
  const file = e.target.files[0];
  if (!file) return;
  
  // 在实际环境中，应该将文件上传到服务器
  // 这里仅做本地预览
  const reader = new FileReader();
  reader.onload = (event) => {
    coverImagePreview.value = event.target.result;
    post.value.coverImage = file;
  };
  reader.readAsDataURL(file);
};

// 移除封面图片
const removeCoverImage = () => {
  coverImagePreview.value = '';
  post.value.coverImage = null;
  if (fileInput.value) {
    fileInput.value.value = '';
  }
};

// 从标签输入中解析标签
watch(tagsInput, (newValue) => {
  if (newValue && post.value) {
    post.value.tags = newValue.split(',')
      .map(tag => tag.trim())
      .filter(tag => tag.length > 0);
  } else if (post.value) {
    post.value.tags = [];
  }
});

// 保存草稿
const saveDraft = async () => {
  if (!authStore.isLoggedIn) {
    if (process.client) {
      alert('请先登录');
      router.push('/login');
    }
    return;
  }
  
  saving.value = true;
  
  try {
    // 获取最新的Markdown内容
    if (editor) {
      post.value.content = editor.getMarkdown();
    }
    
    // 实际环境中，应该将文章数据发送到服务器
    console.log('保存草稿:', post.value);
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    if (process.client) {
      alert('草稿保存成功');
    }
  } catch (error) {
    console.error('保存草稿失败:', error);
    if (process.client) {
      alert('保存失败，请稍后再试');
    }
  } finally {
    saving.value = false;
  }
};

// 更新文章
const updatePost = async () => {
  if (!post.value.title) {
    if (process.client) {
      alert('请输入文章标题');
    }
    return;
  }
  
  // 获取最新的Markdown内容
  if (editor) {
    post.value.content = editor.getMarkdown();
  }
  
  if (!post.value.content) {
    if (process.client) {
      alert('请输入文章内容');
    }
    return;
  }
  
  if (!authStore.isLoggedIn) {
    if (process.client) {
      alert('请先登录');
      router.push('/login');
    }
    return;
  }
  
  updating.value = true;
  
  try {
    console.log('开始更新文章:', post.value);
    
    // 准备表单数据
    const formData = new FormData();
    formData.append('id', post.value.id);
    formData.append('title', post.value.title);
    formData.append('summary', post.value.summary || '');
    formData.append('content', post.value.content);
    formData.append('contentHtml', editor ? editor.getPreviewedHTML() : '');
    formData.append('categoryId', post.value.categoryId || '');
    formData.append('tags', JSON.stringify(post.value.tags || []));
    
    if (post.value.coverImage && typeof post.value.coverImage !== 'string') {
      formData.append('coverImage', post.value.coverImage);
    }
    
    console.log('更新文章表单数据:', Object.fromEntries(formData.entries()));
    
    // 尝试调用API更新文章
    try {
      const response = await fetch(`/api/posts/${post.value.id}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        },
        body: formData
      });
      
      const result = await response.json();
      
      if (result.code === 200) {
        console.log('API更新文章成功:', result);
        
        if (process.client) {
          alert('文章更新成功');
          router.push(`/post/${post.value.id}`);
        }
        return;
      } else {
        console.error('API更新文章失败:', result.message);
        throw new Error(result.message || '更新失败');
      }
    } catch (apiError) {
      console.error('API调用出错:', apiError);
      
      // 如果API调用失败，则尝试更新本地存储
      if (process.client && window.localStorage) {
        console.log('尝试更新本地存储中的文章');
        
        try {
          // 从本地存储获取文章列表
          const mockPosts = JSON.parse(localStorage.getItem('mockPosts') || '[]');
          
          // 查找并更新文章
          const postIndex = mockPosts.findIndex(p => p.id.toString() === post.value.id.toString());
          
          if (postIndex !== -1) {
            // 更新文章属性
            const updatedPost = {
              ...mockPosts[postIndex],
              title: post.value.title,
              summary: post.value.summary || '',
              content: post.value.content,
              contentHtml: editor ? editor.getPreviewedHTML() : '',
              categoryId: post.value.categoryId || '',
              tags: post.value.tags || [],
              updatedAt: new Date().toISOString()
            };
            
            // 如果有新的封面图，更新它
            if (post.value.coverImage) {
              updatedPost.coverImage = typeof post.value.coverImage === 'string' 
                ? post.value.coverImage 
                : URL.createObjectURL(post.value.coverImage);
            }
            
            // 替换数组中的旧文章
            mockPosts[postIndex] = updatedPost;
            
            // 保存回本地存储
            localStorage.setItem('mockPosts', JSON.stringify(mockPosts));
            
            console.log('成功更新本地存储中的文章:', updatedPost);
            
            if (process.client) {
              alert('文章更新成功（本地保存）');
              router.push(`/post/${post.value.id}`);
            }
            return;
          } else {
            throw new Error('在本地存储中找不到要更新的文章');
          }
        } catch (storageError) {
          console.error('更新本地存储中的文章失败:', storageError);
          throw storageError;
        }
      } else {
        throw apiError;
      }
    }
  } catch (error) {
    console.error('更新文章最终失败:', error);
    if (process.client) {
      alert(`更新失败: ${error.message || '请稍后再试'}`);
    }
  } finally {
    updating.value = false;
  }
};

// 获取分类列表
const fetchCategories = async () => {
  try {
    const response = await $fetch('/api/categories', {
      method: 'GET'
    });
    
    if (response.code === 200) {
      categories.value = response.data;
    }
  } catch (error) {
    console.error('获取分类失败:', error);
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
    
    // 获取文章ID
    if (!route.params.id) {
      alert('缺少文章ID');
      router.push('/');
      return;
    }
    
    // 获取分类列表
    fetchCategories();
    
    // 获取文章详情
    fetchPost();
  }
});

onBeforeUnmount(() => {
  // 清理编辑器实例
  if (editor) {
    editor = null;
  }
});
</script>

<style>
/* 自定义编辑器样式 */
.editormd-fullscreen {
  z-index: 1030;
}

.editormd-preview-container img {
  max-width: 100%;
}

.CodeMirror {
  font-family: "Source Code Pro", Consolas, "Liberation Mono", Menlo, Courier, monospace;
  font-size: 15px;
}

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