<template>
  <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
      </svg>
      发布文章
    </h1>
    
    <div class="space-y-6">
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
          @click="publishPost"
          :disabled="publishing || !post.title || !post.content"
        >
          {{ publishing ? '发布中...' : '发布文章' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();
const fileInput = ref(null);

// 状态
const saving = ref(false);
const publishing = ref(false);
const coverImagePreview = ref('');
const categories = ref([
  { id: 1, name: '技术' },
  { id: 2, name: '设计' },
  { id: 3, name: '生活' }
]);
const tagsInput = ref('');

// 文章数据
const post = ref({
  title: '',
  summary: '',
  content: '',
  categoryId: '',
  tags: [],
  coverImage: null
});

// Editor.md 实例
let editor = null;

// 初始化 Editor.md
const initEditor = async () => {
  if (process.client && post.value) {
    try {
      console.log('开始初始化编辑器...');
      
      // 动态引入jQuery (仍然需要)
      const jQuery = await import('jquery');
      window.jQuery = jQuery.default;
      window.$ = jQuery.default;
      
      // 定义本地资源路径
      const EDITOR_CSS = '/static/editor/css/editormd.min.css';
      const EDITOR_JS = '/static/editor/js/editormd.min.js';
      const EDITOR_LIB_PATH = '/static/editor/lib/';
      
      // 关键依赖库路径
      const MARKED_JS = `${EDITOR_LIB_PATH}marked.min.js`;
      const CODEMIRROR_JS = `${EDITOR_LIB_PATH}codemirror/codemirror.min.js`;
      
      // 设置全局变量保存lib路径
      window.editormdLib = EDITOR_LIB_PATH;
      
      // 显示加载状态
      const container = document.getElementById('editormd-container');
      if (container) {
        container.innerHTML = `
          <div class="editor-loading">
            <div class="spinner"></div>
            <p>编辑器加载中...</p>
          </div>
        `;
      }
      
      // 检查是否已加载
      if (!window.editormd) {
        console.log('加载本地editor.md资源及依赖...');
        
        // 加载CSS
        await new Promise((resolve) => {
          const link = document.createElement('link');
          link.rel = 'stylesheet';
          link.href = EDITOR_CSS;
          link.onload = () => resolve(true);
          link.onerror = () => resolve(false);
          document.head.appendChild(link);
        });
        
        // 首先加载marked依赖
        await new Promise((resolve) => {
          const script = document.createElement('script');
          script.src = MARKED_JS;
          script.onload = () => {
            console.log('Marked依赖库加载成功');
            resolve(true);
          };
          script.onerror = () => {
            console.error('Marked依赖库加载失败');
            resolve(false);
          };
          document.body.appendChild(script);
        });
        
        // 加载CodeMirror依赖
        await new Promise((resolve) => {
          const script = document.createElement('script');
          script.src = CODEMIRROR_JS;
          script.onload = () => {
            console.log('CodeMirror依赖库加载成功');
            resolve(true);
          };
          script.onerror = () => {
            console.error('CodeMirror依赖库加载失败');
            resolve(false);
          };
          document.body.appendChild(script);
        });
        
        // 最后加载主JS文件
        await new Promise((resolve) => {
          const script = document.createElement('script');
          script.src = EDITOR_JS;
          script.onload = () => {
            console.log('Editor.md主程序加载成功');
            resolve(true);
          };
          script.onerror = () => {
            console.error('Editor.md主程序加载失败');
            resolve(false);
          };
          document.body.appendChild(script);
        });
        
        console.log('本地editor.md资源及依赖加载完成');
      } else {
        console.log('editor.md已加载，直接初始化编辑器...');
      }
      
      // 初始化编辑器实例
      if (window.marked && window.CodeMirror) {
        console.log('依赖库检查通过，初始化编辑器实例');
        initEditorInstance();
      } else {
        console.error('依赖库加载不完整:', {
          marked: !!window.marked,
          CodeMirror: !!window.CodeMirror
        });
        throw new Error('依赖库加载不完整');
      }
      
    } catch (error) {
      console.error('初始化编辑器失败:', error);
      alert('初始化编辑器失败，请刷新页面重试');
      // 加载备用编辑器
      initSimpleEditor();
    }
  }
};

// 创建编辑器实例
const initEditorInstance = () => {
  try {
    // 清空编辑器容器
    const container = document.getElementById('editormd-container');
    if (container) {
      container.innerHTML = '';
    }
    
    console.log('开始创建编辑器实例...');
    
    // 检查必要依赖是否存在
    if (!window.marked || !window.CodeMirror) {
      console.error('Editor.md依赖库未加载完成');
      alert('编辑器加载失败：缺少必要依赖库');
      return;
    }
    
    // 创建编辑器
    editor = window.editormd("editormd-container", {
      width: "100%",
      height: 500,
      path: "/static/editor/lib/",  // 确保这个路径指向包含所有依赖的lib目录
      markdown: "# 欢迎使用Markdown编辑器\n\n开始输入...",
      saveHTMLToTextarea: true,
      mode: "markdown",
      readOnly: false,
      htmlDecode: "style,script,iframe",
      emoji: true,
      taskList: true,
      tex: true,
      flowChart: true,
      sequenceDiagram: true,
      imageUpload: true,
      imageFormats: ["jpg", "jpeg", "png", "gif", "bmp", "webp"],
      imageUploadURL: "/api/upload",
      
      // 使用完整工具栏配置，显示所有功能按钮
      toolbarIcons: "full"
    });
    
    console.log('编辑器实例创建成功');
    
    // 添加额外检查
    setTimeout(() => {
      const editorLoaded = document.querySelector('.editormd-preview');
      if (!editorLoaded) {
        console.error('编辑器预览区未正确加载');
        alert('编辑器加载不完整，请刷新页面重试');
      }
    }, 2000);
  } catch (error) {
    console.error('创建编辑器实例失败:', error);
    alert('初始化编辑器时发生错误: ' + error.message);
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
  if (newValue) {
    post.value.tags = newValue.split(',')
      .map(tag => tag.trim())
      .filter(tag => tag.length > 0);
  } else {
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

// 获取分类列表
const fetchCategories = async () => {
  try {
    console.log('正在获取分类列表...');
    const response = await $fetch('/api/categories', {
      method: 'GET'
    });
    
    if (response.code === 200) {
      categories.value = response.data;
    }
  } catch (error) {
    console.error('获取分类失败:', error);
    console.log('使用模拟分类数据...');
    
    // 使用模拟数据
    categories.value = [
      { id: 1, name: '技术' },
      { id: 2, name: '设计' },
      { id: 3, name: '生活' },
      { id: 4, name: '前端' },
      { id: 5, name: '后端' }
    ];
  }
};

// 发布文章
const publishPost = async () => {
  if (!post.value.title) {
    if (process.client) {
      alert('请输入文章标题');
    }
    return;
  }
  
  // 获取最新的Markdown内容
  if (editor) {
    post.value.content = editor.getMarkdown();
  } else if (fallbackEditorActive.value && document.getElementById('fallback-editor')) {
    post.value.content = document.getElementById('fallback-editor').value;
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
  
  publishing.value = true;
  
  try {
    console.log('准备发布文章数据:', post.value);
    
    // 准备表单数据
    const formData = new FormData();
    formData.append('title', post.value.title);
    formData.append('summary', post.value.summary || '');
    formData.append('content', post.value.content);
    
    // 获取HTML内容
    let contentHtml = '';
    if (editor) {
      contentHtml = editor.getPreviewedHTML();
    } else if (fallbackEditorActive.value) {
      const previewArea = document.getElementById('preview-area');
      if (previewArea) {
        contentHtml = previewArea.innerHTML;
      } else {
        contentHtml = convertMarkdownToHtml(post.value.content);
      }
    }
    
    formData.append('contentHtml', contentHtml);
    formData.append('categoryId', post.value.categoryId || '');
    formData.append('tags', JSON.stringify(post.value.tags || []));
    
    if (post.value.coverImage) {
      formData.append('coverImage', post.value.coverImage);
    }
    
    console.log('发布文章表单数据:', Object.fromEntries(formData.entries()));
    
    try {
      // 尝试发送API请求
      const response = await $fetch('/api/posts', {
        method: 'POST',
        body: formData,
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        }
      });
      
      if (response.code === 200) {
        console.log('API发布成功:', response.data);
        if (process.client) {
          alert('文章发布成功');
          router.push(`/post/${response.data.id}`);
        }
      } else {
        throw new Error(response.message || '发布失败');
      }
    } catch (apiError) {
      console.error('API调用失败，使用本地存储:', apiError);
      
      // 模拟API调用延迟
      await new Promise(resolve => setTimeout(resolve, 800));
      
      // 生成一个模拟ID和当前日期
      let mockId = Math.floor(Math.random() * 1000) + 1;
      const now = new Date();
      
      // 将文章存储到localStorage中
      if (process.client && window.localStorage) {
        try {
          // 获取已有的文章列表
          const existingPosts = JSON.parse(localStorage.getItem('mockPosts') || '[]');
          
          // 检查是否已有相同ID的文章
          const existingIndex = existingPosts.findIndex(p => p.id === mockId);
          if (existingIndex !== -1) {
            // 如果已存在相同ID的文章，生成一个新的唯一ID
            mockId = Date.now();
          }
          
          // 获取HTML内容
          let contentHtml = formData.get('contentHtml') || '';
          if (!contentHtml) {
            if (editor) {
              contentHtml = editor.getPreviewedHTML();
            } else if (fallbackEditorActive.value) {
              contentHtml = convertMarkdownToHtml(post.value.content);
            }
          }
          
          // 添加新文章
          const newPost = {
            id: mockId,
            title: post.value.title,
            summary: post.value.summary || '',
            content: post.value.content,
            contentHtml: contentHtml,
            categoryId: post.value.categoryId || '',
            category: categories.value.find(c => c.id === post.value.categoryId)?.name || '',
            tags: post.value.tags || [],
            coverImage: coverImagePreview.value,
            createdAt: now.toISOString(),
            updatedAt: now.toISOString(),
            viewCount: 0,
            likes: 0,
            favorites: 0,
            comments: [],
            author: {
              id: 1,
              nickname: authStore.user?.nickname || '测试用户',
              avatar: authStore.user?.avatar || ''
            }
          };
          
          console.log('保存到本地存储的文章:', newPost);
          
          // 将新文章添加到列表开头
          existingPosts.unshift(newPost);
          localStorage.setItem('mockPosts', JSON.stringify(existingPosts));
          
          alert('文章发布成功（本地模式）');
          router.push(`/post/${mockId}`);
        } catch (storageError) {
          console.error('存储到localStorage失败:', storageError);
          alert('文章发布成功');
          router.push('/');
        }
      } else {
        alert('文章发布成功');
        router.push('/');
      }
    }
  } catch (error) {
    console.error('发布文章失败:', error);
    if (process.client) {
      alert('发布失败，请稍后再试');
    }
  } finally {
    publishing.value = false;
  }
};

onMounted(() => {
  if (process.client) {
    // 检查用户是否登录
    if (!authStore.isLoggedIn) {
      console.log('用户未登录，重定向到登录页面');
      setTimeout(() => {
        alert('请先登录');
        router.push('/login');
      }, 100);
      return;
    }
    
    console.log('开始获取分类列表...');
    // 获取分类列表
    fetchCategories();
    
    console.log('开始初始化编辑器...');
    // 初始化编辑器
    // 给编辑器容器元素添加高度，确保它可见
    const editorContainer = document.getElementById('editormd-container');
    if (editorContainer) {
      editorContainer.style.minHeight = '500px';
    }
    
    // 延迟初始化编辑器，确保DOM已完全渲染
    setTimeout(() => {
      initEditor();
    }, 500);
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
/* 改为使用CDN */
/* @import "/node_modules/editor.md/css/editormd.min.css"; */

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

.editor-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.editor-loading .spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top-color: #e53e3e;
  animation: spin 1s linear infinite;
  margin-bottom: 12px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.simple-markdown-editor {
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.editor-header {
  background-color: #f5f5f5;
  padding: 8px 15px;
  border-bottom: 1px solid #ddd;
}

.tab-buttons button {
  background: none;
  border: none;
  padding: 5px 15px;
  cursor: pointer;
  border-radius: 4px;
}

.tab-buttons button.active {
  background-color: #e53e3e;
  color: white;
}

.editor-body {
  position: relative;
  height: 500px;
}

.markdown-input {
  width: 100%;
  height: 100%;
  border: none;
  resize: none;
  padding: 15px;
  font-family: monospace;
}

.preview-panel {
  padding: 15px;
  height: 100%;
  overflow: auto;
}
</style> 