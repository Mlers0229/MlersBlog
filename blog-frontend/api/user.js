import api from './index'

/**
 * 获取用户个人信息
 * @returns {Promise<Object>} 用户详细信息
 */
export const getCurrentUser = () => {
  return api.get('/users/me')
}

/**
 * 根据ID获取用户信息
 * @param {number} userId - 用户ID
 * @returns {Promise<Object>} 用户公开信息
 */
export const getUserById = (userId) => {
  return api.get(`/users/${userId}`)
}

/**
 * 更新用户个人信息
 * @param {Object} userData - 用户信息对象
 * @returns {Promise<Object>} 更新后的用户信息
 */
export const updateUserProfile = (userData) => {
  return api.put(`/users/${userData.id}`, userData)
}

/**
 * 更新用户密码
 * @param {Object} passwordData - 包含旧密码和新密码的对象
 * @returns {Promise<Object>} 响应结果
 */
export const updatePassword = (passwordData) => {
  return api.put('/users/password', passwordData)
}

/**
 * 上传用户头像
 * @param {FormData} formData - 包含头像文件的FormData对象
 * @returns {Promise<Object>} 包含头像URL的响应
 */
export const uploadAvatar = (formData) => {
  return api.post('/users/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取用户发布的文章列表
 * @param {number} userId - 用户ID
 * @param {number} page - 页码，从0开始
 * @param {number} size - 每页数量
 * @returns {Promise<Object>} 分页的文章列表
 */
export const getUserPosts = (userId, page = 0, size = 10) => {
  return api.get(`/users/${userId}/posts?page=${page}&size=${size}`)
}

/**
 * 获取用户的草稿列表
 * @param {number} page - 页码，从0开始
 * @param {number} size - 每页数量
 * @returns {Promise<Object>} 分页的草稿列表
 */
export const getUserDrafts = (page = 0, size = 10) => {
  return api.get(`/users/me/drafts?page=${page}&size=${size}`)
}

/**
 * 获取用户的评论列表
 * @param {number} userId - 用户ID
 * @param {number} page - 页码，从0开始
 * @param {number} size - 每页数量
 * @returns {Promise<Object>} 分页的评论列表
 */
export const getUserComments = (userId, page = 0, size = 20) => {
  return api.get(`/users/${userId}/comments?page=${page}&size=${size}`)
}

/**
 * 注销账号
 * @returns {Promise<Object>} 响应结果
 */
export const deleteAccount = () => {
  return api.delete('/users/me')
}

// 导出所有方法
export const user = {
  getCurrentUser,
  getUserById,
  updateUserProfile,
  updatePassword,
  uploadAvatar,
  getUserPosts,
  getUserDrafts,
  getUserComments,
  deleteAccount
} 