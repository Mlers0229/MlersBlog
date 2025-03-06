import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useUserStore = defineStore('user', () => {
  // 用户列表状态
  const users = ref({});
  
  // 初始化函数，加载用户数据
  const initialize = () => {
    if (process.client) {
      try {
        const storedUsers = localStorage.getItem('blog_users');
        if (storedUsers) {
          users.value = JSON.parse(storedUsers);
        }
      } catch (error) {
        console.error('加载用户数据失败:', error);
      }
    }
  };
  
  // 保存用户数据到本地存储
  const saveUsers = () => {
    if (process.client) {
      try {
        localStorage.setItem('blog_users', JSON.stringify(users.value));
      } catch (error) {
        console.error('保存用户数据失败:', error);
      }
    }
  };
  
  // 获取用户信息
  const getUser = (userId) => {
    return users.value[userId] || null;
  };
  
  // 获取多个用户信息
  const getUsers = (userIds) => {
    if (!userIds || !userIds.length) return [];
    
    return userIds.map(id => getUser(id)).filter(Boolean);
  };
  
  // 添加或更新用户信息
  const addUser = (user) => {
    if (!user || !user.id) return;
    
    users.value = {
      ...users.value,
      [user.id]: {
        ...users.value[user.id],
        ...user
      }
    };
    
    saveUsers();
  };
  
  // 批量添加用户信息
  const addUsers = (usersList) => {
    if (!usersList || !usersList.length) return;
    
    const newUsers = { ...users.value };
    
    usersList.forEach(user => {
      if (user && user.id) {
        newUsers[user.id] = {
          ...newUsers[user.id],
          ...user
        };
      }
    });
    
    users.value = newUsers;
    saveUsers();
  };
  
  return {
    users,
    initialize,
    getUser,
    getUsers,
    addUser,
    addUsers
  };
}); 