<template>
  <div>
    <div class="mb-6 flex justify-between items-center">
      <h1 class="text-2xl font-bold text-gray-800">用户管理</h1>
      <div class="flex space-x-2">
        <div class="relative">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索用户名或邮箱..." 
            class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
            @keyup.enter="searchUsers"
          />
          <svg 
            xmlns="http://www.w3.org/2000/svg" 
            class="h-5 w-5 absolute left-3 top-2.5 text-gray-400" 
            fill="none" 
            viewBox="0 0 24 24" 
            stroke="currentColor"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </div>
        <select 
          v-model="filterRole" 
          class="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
          @change="fetchUsers(1)"
        >
          <option value="all">所有角色</option>
          <option value="admin">管理员</option>
          <option value="user">普通用户</option>
        </select>
        <select 
          v-model="filterStatus" 
          class="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-rose-500 focus:border-transparent"
          @change="fetchUsers(1)"
        >
          <option value="all">所有状态</option>
          <option value="active">已激活</option>
          <option value="inactive">未激活</option>
          <option value="banned">已封禁</option>
        </select>
      </div>
    </div>
    
    <!-- 表格 -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden">
      <div class="min-w-full divide-y divide-gray-200">
        <!-- 表头 -->
        <div class="bg-gray-50">
          <div class="grid grid-cols-12 divide-x divide-gray-200">
            <div class="px-6 py-3 col-span-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              用户信息
            </div>
            <div class="px-6 py-3 col-span-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              邮箱
            </div>
            <div class="px-6 py-3 col-span-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              注册日期
            </div>
            <div class="px-6 py-3 col-span-1 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              角色
            </div>
            <div class="px-6 py-3 col-span-1 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              状态
            </div>
            <div class="px-6 py-3 col-span-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              操作
            </div>
          </div>
        </div>
        
        <!-- 加载中 -->
        <div v-if="loading" class="divide-y divide-gray-200">
          <div v-for="i in 5" :key="i" class="animate-pulse bg-white">
            <div class="grid grid-cols-12 divide-x divide-gray-200">
              <div class="px-6 py-4 col-span-3">
                <div class="flex items-center">
                  <div class="w-10 h-10 rounded-full bg-gray-200 mr-3"></div>
                  <div>
                    <div class="h-4 bg-gray-200 rounded w-24 mb-2"></div>
                    <div class="h-3 bg-gray-200 rounded w-16"></div>
                  </div>
                </div>
              </div>
              <div class="px-6 py-4 col-span-2">
                <div class="h-4 bg-gray-200 rounded w-32"></div>
              </div>
              <div class="px-6 py-4 col-span-2">
                <div class="h-4 bg-gray-200 rounded w-24"></div>
              </div>
              <div class="px-6 py-4 col-span-1">
                <div class="h-4 bg-gray-200 rounded w-16"></div>
              </div>
              <div class="px-6 py-4 col-span-1">
                <div class="h-4 bg-gray-200 rounded w-16"></div>
              </div>
              <div class="px-6 py-4 col-span-3">
                <div class="h-8 bg-gray-200 rounded w-32"></div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 无数据 -->
        <div v-else-if="users.length === 0" class="bg-white">
          <div class="px-6 py-10 text-center">
            <svg 
              xmlns="http://www.w3.org/2000/svg" 
              class="h-10 w-10 mx-auto text-gray-300" 
              fill="none" 
              viewBox="0 0 24 24" 
              stroke="currentColor"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            <p class="mt-2 text-gray-500">
              {{ searchQuery ? '没有找到匹配的用户' : '暂无用户数据' }}
            </p>
          </div>
        </div>
        
        <!-- 表格内容 -->
        <div v-else class="bg-white divide-y divide-gray-200">
          <div 
            v-for="user in users" 
            :key="user.id" 
            class="hover:bg-gray-50"
          >
            <div class="grid grid-cols-12 divide-x divide-gray-200">
              <div class="px-6 py-4 col-span-3">
                <div class="flex items-center">
                  <img 
                    :src="user.avatar || '/img/default-avatar.png'" 
                    alt="用户头像" 
                    class="w-10 h-10 rounded-full mr-3"
                  />
                  <div>
                    <div class="font-medium text-gray-900">{{ user.nickname }}</div>
                    <div class="text-sm text-gray-500">@{{ user.username }}</div>
                  </div>
                </div>
              </div>
              <div class="px-6 py-4 col-span-2 text-gray-900">
                {{ user.email }}
              </div>
              <div class="px-6 py-4 col-span-2 text-sm text-gray-500">
                {{ formatDate(user.createdAt) }}
              </div>
              <div class="px-6 py-4 col-span-1">
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="{
                    'bg-blue-100 text-blue-800': user.role === 'admin',
                    'bg-gray-100 text-gray-800': user.role === 'user'
                  }"
                >
                  {{ formatRole(user.role) }}
                </span>
              </div>
              <div class="px-6 py-4 col-span-1">
                <span 
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="{
                    'bg-green-100 text-green-800': user.status === 'active',
                    'bg-gray-100 text-gray-800': user.status === 'inactive',
                    'bg-rose-100 text-rose-800': user.status === 'banned'
                  }"
                >
                  {{ formatStatus(user.status) }}
                </span>
              </div>
              <div class="px-6 py-4 col-span-3 flex space-x-2">
                <button 
                  v-if="user.status !== 'active' && user.status !== 'banned'"
                  @click="activateUser(user.id)"
                  class="text-xs bg-green-100 text-green-600 rounded-full px-3 py-1 hover:bg-green-200"
                >
                  激活
                </button>
                <button 
                  v-if="user.status !== 'banned'"
                  @click="banUser(user.id)"
                  class="text-xs bg-rose-100 text-rose-600 rounded-full px-3 py-1 hover:bg-rose-200"
                >
                  封禁
                </button>
                <button 
                  v-if="user.status === 'banned'"
                  @click="unbanUser(user.id)"
                  class="text-xs bg-green-100 text-green-600 rounded-full px-3 py-1 hover:bg-green-200"
                >
                  解封
                </button>
                <button 
                  v-if="user.role !== 'admin'"
                  @click="setAdminRole(user.id)"
                  class="text-xs bg-blue-100 text-blue-600 rounded-full px-3 py-1 hover:bg-blue-200"
                >
                  设为管理员
                </button>
                <button 
                  v-if="user.role === 'admin' && authStore.user && authStore.user.id !== user.id"
                  @click="removeAdminRole(user.id)"
                  class="text-xs bg-amber-100 text-amber-600 rounded-full px-3 py-1 hover:bg-amber-200"
                >
                  移除管理员
                </button>
                <button 
                  @click="openUserDetailsModal(user)"
                  class="text-xs bg-gray-100 text-gray-600 rounded-full px-3 py-1 hover:bg-gray-200"
                >
                  查看详情
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <div class="mt-6 flex justify-between items-center">
      <div class="text-sm text-gray-500">
        共 {{ totalUsers }} 条数据
      </div>
      <div class="flex space-x-2">
        <button 
          @click="prevPage" 
          :disabled="currentPage === 1"
          class="px-3 py-1 rounded bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          上一页
        </button>
        <button 
          @click="nextPage" 
          :disabled="currentPage >= totalPages"
          class="px-3 py-1 rounded bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          下一页
        </button>
      </div>
    </div>
    
    <!-- 用户详情模态框 -->
    <div 
      v-if="userDetailsModalOpen" 
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-2xl">
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-xl font-medium text-gray-900">用户详情</h3>
          <button @click="userDetailsModalOpen = false" class="text-gray-400 hover:text-gray-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div v-if="selectedUser" class="space-y-6">
          <div class="flex flex-col sm:flex-row">
            <div class="mb-4 sm:mb-0 sm:mr-6">
              <img 
                :src="selectedUser.avatar || '/img/default-avatar.png'" 
                alt="用户头像" 
                class="w-24 h-24 object-cover rounded-lg"
              />
            </div>
            <div class="flex-1 grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <h4 class="text-xs text-gray-500 uppercase tracking-wider mb-1">昵称</h4>
                <p class="text-gray-900">{{ selectedUser.nickname }}</p>
              </div>
              <div>
                <h4 class="text-xs text-gray-500 uppercase tracking-wider mb-1">用户名</h4>
                <p class="text-gray-900">{{ selectedUser.username }}</p>
              </div>
              <div>
                <h4 class="text-xs text-gray-500 uppercase tracking-wider mb-1">邮箱</h4>
                <p class="text-gray-900">{{ selectedUser.email }}</p>
              </div>
              <div>
                <h4 class="text-xs text-gray-500 uppercase tracking-wider mb-1">注册日期</h4>
                <p class="text-gray-900">{{ formatDate(selectedUser.createdAt) }}</p>
              </div>
              <div>
                <h4 class="text-xs text-gray-500 uppercase tracking-wider mb-1">角色</h4>
                <p class="text-gray-900">{{ formatRole(selectedUser.role) }}</p>
              </div>
              <div>
                <h4 class="text-xs text-gray-500 uppercase tracking-wider mb-1">状态</h4>
                <p class="text-gray-900">{{ formatStatus(selectedUser.status) }}</p>
              </div>
            </div>
          </div>
          
          <div>
            <h4 class="text-xs text-gray-500 uppercase tracking-wider mb-1">个人简介</h4>
            <p class="text-gray-900">{{ selectedUser.bio || '暂无个人简介' }}</p>
          </div>
          
          <!-- 用户统计 -->
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div class="bg-gray-50 p-4 rounded-lg">
              <div class="flex items-center justify-between">
                <h4 class="text-sm font-medium text-gray-500">文章数</h4>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
              <p class="text-2xl font-bold text-gray-800 mt-2">{{ selectedUser.stats?.posts || 0 }}</p>
            </div>
            
            <div class="bg-gray-50 p-4 rounded-lg">
              <div class="flex items-center justify-between">
                <h4 class="text-sm font-medium text-gray-500">评论数</h4>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                </svg>
              </div>
              <p class="text-2xl font-bold text-gray-800 mt-2">{{ selectedUser.stats?.comments || 0 }}</p>
            </div>
            
            <div class="bg-gray-50 p-4 rounded-lg">
              <div class="flex items-center justify-between">
                <h4 class="text-sm font-medium text-gray-500">收获点赞</h4>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
              </div>
              <p class="text-2xl font-bold text-gray-800 mt-2">{{ selectedUser.stats?.likes || 0 }}</p>
            </div>
          </div>
          
          <!-- 操作按钮 -->
          <div class="mt-6 flex justify-end space-x-2">
            <button 
              @click="userDetailsModalOpen = false" 
              class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
            >
              关闭
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { format } from 'date-fns';
import { zhCN } from 'date-fns/locale';

const authStore = useAuthStore();

// 用户列表状态
const users = ref([]);
const loading = ref(true);
const searchQuery = ref('');
const filterRole = ref('all');
const filterStatus = ref('all');
const currentPage = ref(1);
const pageSize = 10;
const totalUsers = ref(0);

// 用户详情模态框状态
const userDetailsModalOpen = ref(false);
const selectedUser = ref(null);

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(totalUsers.value / pageSize);
});

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  return format(date, 'yyyy-MM-dd HH:mm', { locale: zhCN });
};

// 格式化角色
const formatRole = (role) => {
  switch (role) {
    case 'admin': return '管理员';
    case 'user': return '普通用户';
    default: return role;
  }
};

// 格式化状态
const formatStatus = (status) => {
  switch (status) {
    case 'active': return '已激活';
    case 'inactive': return '未激活';
    case 'banned': return '已封禁';
    default: return status;
  }
};

// 获取用户列表
const fetchUsers = async (page = 1) => {
  try {
    loading.value = true;
    currentPage.value = page;
    
    // 实际环境中应调用API
    try {
      const response = await $fetch('/api/admin/users', {
        method: 'GET',
        params: {
          page: page,
          pageSize: pageSize,
          role: filterRole.value === 'all' ? '' : filterRole.value,
          status: filterStatus.value === 'all' ? '' : filterStatus.value,
          search: searchQuery.value
        },
        headers: {
          Authorization: `Bearer ${authStore.getToken()}`
        }
      });
      
      if (response.code === 200) {
        users.value = response.data.items;
        totalUsers.value = response.data.total;
      } else {
        throw new Error(response.message || '获取用户列表失败');
      }
    } catch (error) {
      console.error('API调用失败，使用模拟数据:', error);
      
      // 模拟数据
      setTimeout(() => {
        const mockUsers = generateMockUsers();
        
        // 过滤角色
        let filteredUsers = mockUsers;
        if (filterRole.value !== 'all') {
          filteredUsers = mockUsers.filter(user => user.role === filterRole.value);
        }
        
        // 过滤状态
        if (filterStatus.value !== 'all') {
          filteredUsers = filteredUsers.filter(user => user.status === filterStatus.value);
        }
        
        // 搜索过滤
        if (searchQuery.value) {
          const query = searchQuery.value.toLowerCase();
          filteredUsers = filteredUsers.filter(
            user => user.username.toLowerCase().includes(query) || 
                   user.email.toLowerCase().includes(query) ||
                   user.nickname.toLowerCase().includes(query)
          );
        }
        
        // 分页
        const start = (page - 1) * pageSize;
        const end = start + pageSize;
        users.value = filteredUsers.slice(start, end);
        totalUsers.value = filteredUsers.length;
        
        loading.value = false;
      }, 800);
    }
  } catch (error) {
    console.error('获取用户列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索用户
const searchUsers = () => {
  fetchUsers(1);
};

// 翻页
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    fetchUsers(currentPage.value + 1);
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    fetchUsers(currentPage.value - 1);
  }
};

// 激活用户
const activateUser = async (id) => {
  if (!confirm('确定要激活这个用户吗？')) return;
  
  try {
    const response = await $fetch(`/api/admin/users/${id}/activate`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('用户已激活');
      fetchUsers(currentPage.value);
    } else {
      throw new Error(response.message || '激活用户失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = users.value.findIndex(user => user.id === id);
    if (index !== -1) {
      users.value[index].status = 'active';
      alert('用户已激活');
    }
  }
};

// 封禁用户
const banUser = async (id) => {
  if (!confirm('确定要封禁这个用户吗？封禁后用户将无法登录和发布内容。')) return;
  
  try {
    const response = await $fetch(`/api/admin/users/${id}/ban`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('用户已封禁');
      fetchUsers(currentPage.value);
    } else {
      throw new Error(response.message || '封禁用户失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = users.value.findIndex(user => user.id === id);
    if (index !== -1) {
      users.value[index].status = 'banned';
      alert('用户已封禁');
    }
  }
};

// 解封用户
const unbanUser = async (id) => {
  if (!confirm('确定要解除封禁这个用户吗？')) return;
  
  try {
    const response = await $fetch(`/api/admin/users/${id}/unban`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('用户已解封');
      fetchUsers(currentPage.value);
    } else {
      throw new Error(response.message || '解封用户失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = users.value.findIndex(user => user.id === id);
    if (index !== -1) {
      users.value[index].status = 'active';
      alert('用户已解封');
    }
  }
};

// 设置管理员角色
const setAdminRole = async (id) => {
  if (!confirm('确定要将这个用户设置为管理员吗？管理员拥有所有权限，请谨慎操作。')) return;
  
  try {
    const response = await $fetch(`/api/admin/users/${id}/role`, {
      method: 'PUT',
      body: {
        role: 'admin'
      },
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('用户已设置为管理员');
      fetchUsers(currentPage.value);
    } else {
      throw new Error(response.message || '设置角色失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = users.value.findIndex(user => user.id === id);
    if (index !== -1) {
      users.value[index].role = 'admin';
      alert('用户已设置为管理员');
    }
  }
};

// 移除管理员角色
const removeAdminRole = async (id) => {
  if (!confirm('确定要移除这个用户的管理员角色吗？')) return;
  
  try {
    const response = await $fetch(`/api/admin/users/${id}/role`, {
      method: 'PUT',
      body: {
        role: 'user'
      },
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      alert('已移除管理员角色');
      fetchUsers(currentPage.value);
    } else {
      throw new Error(response.message || '设置角色失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作
    const index = users.value.findIndex(user => user.id === id);
    if (index !== -1) {
      users.value[index].role = 'user';
      alert('已移除管理员角色');
    }
  }
};

// 打开用户详情模态框
const openUserDetailsModal = async (user) => {
  try {
    // 实际环境中应调用API获取用户详情
    const response = await $fetch(`/api/admin/users/${user.id}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${authStore.getToken()}`
      }
    });
    
    if (response.code === 200) {
      selectedUser.value = response.data;
      userDetailsModalOpen.value = true;
    } else {
      throw new Error(response.message || '获取用户详情失败');
    }
  } catch (error) {
    console.error('API调用失败:', error);
    
    // 模拟操作 - 填充用户统计数据
    selectedUser.value = {
      ...user,
      bio: user.bio || `这是${user.nickname}的个人简介。作为一名${user.role === 'admin' ? '管理员' : '用户'}，${user.nickname}${user.role === 'admin' ? '负责管理博客内容和用户。' : '活跃在博客的各个版块。'}`,
      stats: {
        posts: Math.floor(Math.random() * 20),
        comments: Math.floor(Math.random() * 50),
        likes: Math.floor(Math.random() * 100)
      }
    };
    userDetailsModalOpen.value = true;
  }
};

// 生成模拟用户数据
const generateMockUsers = () => {
  const roles = ['admin', 'user'];
  const statuses = ['active', 'inactive', 'banned'];
  
  return Array.from({ length: 38 }, (_, i) => {
    const id = i + 1;
    const username = ['admin', 'johndoe', 'janedoe', 'mike123', 'sarah_smith', 'developer', 'designer', 'blogger'][i % 8];
    const nickname = ['管理员', '约翰', '简', '麦克', '莎拉', '开发者', '设计师', '博主'][i % 8];
    const indexSuffix = i > 7 ? `_${Math.floor(i / 8)}` : '';
    
    const randomDate = new Date();
    randomDate.setDate(randomDate.getDate() - Math.floor(Math.random() * 365));
    
    return {
      id,
      username: username + indexSuffix,
      nickname: nickname + (i > 7 ? `${Math.floor(i / 8)}` : ''),
      email: `${username}${indexSuffix}@example.com`,
      avatar: i % 4 === 0 ? `/img/avatar${(i % 4) + 1}.jpg` : null,
      role: i === 0 ? 'admin' : (i % 10 === 0 ? 'admin' : 'user'),
      status: statuses[i % 3],
      createdAt: randomDate.toISOString()
    };
  });
};

onMounted(() => {
  fetchUsers(1);
});
</script> 