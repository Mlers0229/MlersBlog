<template>
  <div>
    <h1 class="text-2xl font-bold text-gray-800 mb-6">系统设置</h1>
    
    <!-- 设置选项卡 -->
    <div class="bg-white rounded-lg shadow-sm mb-6">
      <div class="border-b border-gray-200">
        <nav class="flex -mb-px">
          <button 
            v-for="tab in tabs" 
            :key="tab.id"
            class="py-4 px-6 font-medium text-sm border-b-2 focus:outline-none"
            :class="[
              activeTab === tab.id 
                ? 'border-rose-500 text-rose-600' 
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            ]"
            @click="activeTab = tab.id"
          >
            {{ tab.name }}
          </button>
        </nav>
      </div>
    </div>
    
    <!-- 基本信息设置 -->
    <div v-if="activeTab === 'basic'" class="bg-white rounded-lg shadow-sm p-6">
      <h2 class="text-lg font-medium text-gray-800 mb-4">网站基本信息</h2>
      
      <form @submit.prevent="saveBasicSettings">
        <div class="space-y-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              网站名称
            </label>
            <input 
              type="text" 
              v-model="settings.basic.siteName" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="输入网站名称"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              网站描述
            </label>
            <textarea 
              v-model="settings.basic.siteDescription" 
              rows="3"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="输入网站描述"
            ></textarea>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              网站Logo
            </label>
            <div class="flex items-center">
              <img 
                :src="settings.basic.logo || '/img/default-logo.png'" 
                alt="网站Logo" 
                class="h-12 mr-4"
              />
              <button 
                type="button"
                class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
              >
                上传Logo
              </button>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              页脚版权信息
            </label>
            <input 
              type="text" 
              v-model="settings.basic.footerText" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="© 2024 Blog System"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              备案信息
            </label>
            <input 
              type="text" 
              v-model="settings.basic.icp" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="京ICP备xxxxxx号"
            />
          </div>
        </div>
        
        <div class="mt-6">
          <button 
            type="submit"
            class="px-4 py-2 bg-rose-600 text-white rounded-md hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
            :disabled="saving"
          >
            {{ saving ? '保存中...' : '保存设置' }}
          </button>
        </div>
      </form>
    </div>
    
    <!-- 评论设置 -->
    <div v-if="activeTab === 'comment'" class="bg-white rounded-lg shadow-sm p-6">
      <h2 class="text-lg font-medium text-gray-800 mb-4">评论设置</h2>
      
      <form @submit.prevent="saveCommentSettings">
        <div class="space-y-6">
          <div class="flex items-center">
            <input 
              type="checkbox" 
              id="enableComment" 
              v-model="settings.comment.enableComment"
              class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
            />
            <label for="enableComment" class="ml-2 block text-sm text-gray-700">
              启用评论功能
            </label>
          </div>
          
          <div class="flex items-center">
            <input 
              type="checkbox" 
              id="commentNeedsApproval" 
              v-model="settings.comment.needsApproval"
              class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
            />
            <label for="commentNeedsApproval" class="ml-2 block text-sm text-gray-700">
              评论需要审核
            </label>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              评论可嵌套层级
            </label>
            <select 
              v-model="settings.comment.nestLevel" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
            >
              <option value="1">1级（无嵌套）</option>
              <option value="2">2级</option>
              <option value="3">3级</option>
              <option value="0">无限制</option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              评论默认排序
            </label>
            <select 
              v-model="settings.comment.defaultSort" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
            >
              <option value="latest">最新</option>
              <option value="oldest">最早</option>
              <option value="popular">最热</option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              敏感词过滤
            </label>
            <textarea 
              v-model="settings.comment.sensitiveWords" 
              rows="3"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="输入需要过滤的敏感词，用逗号分隔"
            ></textarea>
            <p class="mt-1 text-xs text-gray-500">敏感词之间用逗号分隔，包含敏感词的评论将被自动标记为需要审核</p>
          </div>
        </div>
        
        <div class="mt-6">
          <button 
            type="submit"
            class="px-4 py-2 bg-rose-600 text-white rounded-md hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
            :disabled="saving"
          >
            {{ saving ? '保存中...' : '保存设置' }}
          </button>
        </div>
      </form>
    </div>
    
    <!-- 邮件设置 -->
    <div v-if="activeTab === 'email'" class="bg-white rounded-lg shadow-sm p-6">
      <h2 class="text-lg font-medium text-gray-800 mb-4">邮件通知设置</h2>
      
      <form @submit.prevent="saveEmailSettings">
        <div class="space-y-6">
          <div class="flex items-center">
            <input 
              type="checkbox" 
              id="enableEmail" 
              v-model="settings.email.enableEmail"
              class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
            />
            <label for="enableEmail" class="ml-2 block text-sm text-gray-700">
              启用邮件通知
            </label>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              SMTP服务器
            </label>
            <input 
              type="text" 
              v-model="settings.email.smtpServer" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="smtp.example.com"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              SMTP端口
            </label>
            <input 
              type="number" 
              v-model="settings.email.smtpPort" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="587"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              发件人邮箱
            </label>
            <input 
              type="email" 
              v-model="settings.email.fromEmail" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="noreply@example.com"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              SMTP用户名
            </label>
            <input 
              type="text" 
              v-model="settings.email.smtpUsername" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="username"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              SMTP密码
            </label>
            <input 
              type="password" 
              v-model="settings.email.smtpPassword" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="password"
            />
          </div>
          
          <div>
            <h3 class="text-sm font-medium text-gray-700 mb-2">通知触发条件</h3>
            <div class="space-y-2">
              <div class="flex items-center">
                <input 
                  type="checkbox" 
                  id="notifyNewComment" 
                  v-model="settings.email.notifyNewComment"
                  class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
                />
                <label for="notifyNewComment" class="ml-2 block text-sm text-gray-700">
                  新评论
                </label>
              </div>
              <div class="flex items-center">
                <input 
                  type="checkbox" 
                  id="notifyNewUser" 
                  v-model="settings.email.notifyNewUser"
                  class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
                />
                <label for="notifyNewUser" class="ml-2 block text-sm text-gray-700">
                  新用户注册
                </label>
              </div>
              <div class="flex items-center">
                <input 
                  type="checkbox" 
                  id="notifyNewPost" 
                  v-model="settings.email.notifyNewPost"
                  class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
                />
                <label for="notifyNewPost" class="ml-2 block text-sm text-gray-700">
                  新文章发布
                </label>
              </div>
            </div>
          </div>
          
          <div>
            <button 
              type="button"
              class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 mr-2"
              @click="testEmailConnection"
              :disabled="saving || testing"
            >
              {{ testing ? '发送中...' : '测试邮件连接' }}
            </button>
          </div>
        </div>
        
        <div class="mt-6">
          <button 
            type="submit"
            class="px-4 py-2 bg-rose-600 text-white rounded-md hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
            :disabled="saving"
          >
            {{ saving ? '保存中...' : '保存设置' }}
          </button>
        </div>
      </form>
    </div>
    
    <!-- 安全设置 -->
    <div v-if="activeTab === 'security'" class="bg-white rounded-lg shadow-sm p-6">
      <h2 class="text-lg font-medium text-gray-800 mb-4">安全设置</h2>
      
      <form @submit.prevent="saveSecuritySettings">
        <div class="space-y-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              登录尝试失败限制
            </label>
            <div class="flex items-center">
              <input 
                type="number" 
                v-model="settings.security.maxLoginAttempts" 
                class="w-32 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
                min="1"
                max="10"
              />
              <span class="ml-2 text-sm text-gray-500">次</span>
            </div>
            <p class="mt-1 text-xs text-gray-500">超过限制后账户将被临时锁定</p>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              账户锁定时间
            </label>
            <div class="flex items-center">
              <input 
                type="number" 
                v-model="settings.security.lockoutDuration" 
                class="w-32 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
                min="5"
                max="1440"
              />
              <span class="ml-2 text-sm text-gray-500">分钟</span>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              登录要求验证码
            </label>
            <select 
              v-model="settings.security.captchaType" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
            >
              <option value="none">不需要验证码</option>
              <option value="simple">简单计算题</option>
              <option value="recaptcha">Google reCAPTCHA</option>
            </select>
          </div>
          
          <div v-if="settings.security.captchaType === 'recaptcha'">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              reCAPTCHA站点密钥
            </label>
            <input 
              type="text" 
              v-model="settings.security.recaptchaSiteKey" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="reCAPTCHA站点密钥"
            />
          </div>
          
          <div v-if="settings.security.captchaType === 'recaptcha'">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              reCAPTCHA密钥
            </label>
            <input 
              type="password" 
              v-model="settings.security.recaptchaSecretKey" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
              placeholder="reCAPTCHA密钥"
            />
          </div>
          
          <div class="flex items-center">
            <input 
              type="checkbox" 
              id="enablePasswordPolicy" 
              v-model="settings.security.enablePasswordPolicy"
              class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
            />
            <label for="enablePasswordPolicy" class="ml-2 block text-sm text-gray-700">
              启用密码策略
            </label>
          </div>
          
          <div v-if="settings.security.enablePasswordPolicy" class="pl-6 border-l-2 border-gray-200">
            <div class="flex items-center mb-2">
              <input 
                type="checkbox" 
                id="requireUppercase" 
                v-model="settings.security.requireUppercase"
                class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
              />
              <label for="requireUppercase" class="ml-2 block text-sm text-gray-700">
                需要大写字母
              </label>
            </div>
            <div class="flex items-center mb-2">
              <input 
                type="checkbox" 
                id="requireLowercase" 
                v-model="settings.security.requireLowercase"
                class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
              />
              <label for="requireLowercase" class="ml-2 block text-sm text-gray-700">
                需要小写字母
              </label>
            </div>
            <div class="flex items-center mb-2">
              <input 
                type="checkbox" 
                id="requireNumbers" 
                v-model="settings.security.requireNumbers"
                class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
              />
              <label for="requireNumbers" class="ml-2 block text-sm text-gray-700">
                需要数字
              </label>
            </div>
            <div class="flex items-center mb-2">
              <input 
                type="checkbox" 
                id="requireSpecialChars" 
                v-model="settings.security.requireSpecialChars"
                class="h-4 w-4 text-rose-600 focus:ring-rose-500 border-gray-300 rounded"
              />
              <label for="requireSpecialChars" class="ml-2 block text-sm text-gray-700">
                需要特殊字符
              </label>
            </div>
            <div class="mb-2">
              <label class="block text-sm text-gray-700 mb-1">
                最小密码长度
              </label>
              <input 
                type="number" 
                v-model="settings.security.minPasswordLength" 
                class="w-20 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
                min="6"
                max="30"
              />
            </div>
          </div>
        </div>
        
        <div class="mt-6">
          <button 
            type="submit"
            class="px-4 py-2 bg-rose-600 text-white rounded-md hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
            :disabled="saving"
          >
            {{ saving ? '保存中...' : '保存设置' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

// 选项卡管理
const tabs = [
  { id: 'basic', name: '基本信息' },
  { id: 'comment', name: '评论设置' },
  { id: 'email', name: '邮件通知' },
  { id: 'security', name: '安全设置' }
];
const activeTab = ref('basic');

// 设置数据
const settings = reactive({
  basic: {
    siteName: 'Blog System',
    siteDescription: '一个现代化的个人博客系统',
    logo: null,
    footerText: '© 2024 Blog System',
    icp: ''
  },
  comment: {
    enableComment: true,
    needsApproval: true,
    nestLevel: '3',
    defaultSort: 'latest',
    sensitiveWords: ''
  },
  email: {
    enableEmail: false,
    smtpServer: '',
    smtpPort: 587,
    fromEmail: '',
    smtpUsername: '',
    smtpPassword: '',
    notifyNewComment: true,
    notifyNewUser: true,
    notifyNewPost: false
  },
  security: {
    maxLoginAttempts: 5,
    lockoutDuration: 30,
    captchaType: 'none',
    recaptchaSiteKey: '',
    recaptchaSecretKey: '',
    enablePasswordPolicy: true,
    requireUppercase: true,
    requireLowercase: true,
    requireNumbers: true,
    requireSpecialChars: false,
    minPasswordLength: 8
  }
});

const saving = ref(false);
const testing = ref(false);

// 初始化加载设置
onMounted(async () => {
  try {
    // 尝试从API获取设置
    try {
      const response = await $fetch('/api/admin/settings', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        }
      });
      
      if (response.code === 200) {
        // 合并API返回的设置
        Object.keys(response.data).forEach(key => {
          if (settings[key]) {
            Object.assign(settings[key], response.data[key]);
          }
        });
      }
    } catch (error) {
      console.error('获取设置失败，使用默认值:', error);
      
      // 如果API失败，尝试从本地存储获取
      if (process.client && window.localStorage) {
        try {
          const storedSettings = localStorage.getItem('blog_admin_settings');
          if (storedSettings) {
            const parsedSettings = JSON.parse(storedSettings);
            Object.keys(parsedSettings).forEach(key => {
              if (settings[key]) {
                Object.assign(settings[key], parsedSettings[key]);
              }
            });
          }
        } catch (localError) {
          console.error('从本地存储获取设置失败:', localError);
        }
      }
    }
  } catch (error) {
    console.error('初始化设置失败:', error);
  }
});

// 保存基本设置
const saveBasicSettings = async () => {
  await saveSettings('basic');
};

// 保存评论设置
const saveCommentSettings = async () => {
  await saveSettings('comment');
};

// 保存邮件设置
const saveEmailSettings = async () => {
  await saveSettings('email');
};

// 保存安全设置
const saveSecuritySettings = async () => {
  await saveSettings('security');
};

// 统一的保存设置方法
const saveSettings = async (type) => {
  saving.value = true;
  
  try {
    // 尝试调用API保存设置
    try {
      const response = await $fetch('/api/admin/settings', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        },
        body: {
          type,
          data: settings[type]
        }
      });
      
      if (response.code === 200) {
        alert(`${tabs.find(t => t.id === type).name}保存成功`);
      } else {
        throw new Error(response.message || '保存设置失败');
      }
    } catch (apiError) {
      console.error('API调用失败，保存到本地存储:', apiError);
      
      // 如果API失败，保存到本地存储
      if (process.client && window.localStorage) {
        try {
          const storedSettings = localStorage.getItem('blog_admin_settings');
          let allSettings = storedSettings ? JSON.parse(storedSettings) : {};
          
          allSettings[type] = settings[type];
          localStorage.setItem('blog_admin_settings', JSON.stringify(allSettings));
          
          alert(`${tabs.find(t => t.id === type).name}已保存到本地`);
        } catch (localError) {
          console.error('保存到本地存储失败:', localError);
          alert('保存设置失败');
        }
      }
    }
  } catch (error) {
    console.error('保存设置失败:', error);
    alert('保存设置失败');
  } finally {
    saving.value = false;
  }
};

// 测试邮件连接
const testEmailConnection = async () => {
  testing.value = true;
  
  try {
    // 尝试调用API测试邮件连接
    try {
      const response = await $fetch('/api/admin/settings/test-email', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        },
        body: settings.email
      });
      
      if (response.code === 200) {
        alert('测试邮件发送成功，请检查您的邮箱');
      } else {
        throw new Error(response.message || '测试邮件发送失败');
      }
    } catch (apiError) {
      console.error('API调用失败:', apiError);
      alert('测试邮件发送失败: ' + (apiError.message || '未知错误'));
    }
  } catch (error) {
    console.error('测试邮件连接失败:', error);
    alert('测试邮件发送失败');
  } finally {
    testing.value = false;
  }
};
</script> 