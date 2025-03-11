import api from './index'

/**
 * 点赞文章
 * @param {number} postId - 文章ID
 * @returns {Promise<Object>} 响应结果
 */
export const likePost = (postId) => {
  return api.post(`/posts/${postId}/like`)
}

/**
 * 取消点赞文章
 * @param {number} postId - 文章ID
 * @returns {Promise<Object>} 响应结果
 */
export const unlikePost = (postId) => {
  return api.delete(`/posts/${postId}/like`)
}

/**
 * 收藏文章
 * @param {number} postId - 文章ID
 * @returns {Promise<Object>} 响应结果
 */
export const favoritePost = (postId) => {
  return api.post(`/posts/${postId}/favorite`)
}

/**
 * 取消收藏文章
 * @param {number} postId - 文章ID
 * @returns {Promise<Object>} 响应结果
 */
export const unfavoritePost = (postId) => {
  return api.delete(`/posts/${postId}/favorite`)
}

/**
 * 获取文章点赞用户列表
 * @param {number} postId - 文章ID
 * @param {number} page - 页码，从0开始
 * @param {number} size - 每页数量
 * @returns {Promise<Object>} 分页的用户列表
 */
export const getPostLikedUsers = (postId, page = 0, size = 20) => {
  return api.get(`/posts/${postId}/likes?page=${page}&size=${size}`)
}

/**
 * 获取文章收藏用户列表
 * @param {number} postId - 文章ID
 * @param {number} page - 页码，从0开始
 * @param {number} size - 每页数量
 * @returns {Promise<Object>} 分页的用户列表
 */
export const getPostFavoritedUsers = (postId, page = 0, size = 20) => {
  return api.get(`/posts/${postId}/favorites?page=${page}&size=${size}`)
}

/**
 * 获取用户点赞的文章列表
 * @param {number} userId - 用户ID
 * @param {number} page - 页码，从0开始
 * @param {number} size - 每页数量
 * @returns {Promise<Object>} 分页的文章列表
 */
export const getUserLikedPosts = (userId, page = 0, size = 10) => {
  return api.get(`/users/${userId}/likes?page=${page}&size=${size}`)
}

/**
 * 获取用户收藏的文章列表
 * @param {number} userId - 用户ID
 * @param {number} page - 页码，从0开始
 * @param {number} size - 每页数量
 * @returns {Promise<Object>} 分页的文章列表
 */
export const getUserFavoritedPosts = (userId, page = 0, size = 10) => {
  return api.get(`/users/${userId}/favorites?page=${page}&size=${size}`)
}

// 导出所有方法
export const interaction = {
  likePost,
  unlikePost,
  favoritePost,
  unfavoritePost,
  getPostLikedUsers,
  getPostFavoritedUsers,
  getUserLikedPosts,
  getUserFavoritedPosts
} 