import api from './index'

/**
 * 获取博客整体统计信息
 * @returns {Promise<Object>} 统计信息
 */
export const getBlogStats = () => {
  return api.get('/stats/blog')
}

/**
 * 获取用户统计信息
 * @param {number} userId - 用户ID，不提供则获取当前用户
 * @returns {Promise<Object>} 用户统计信息
 */
export const getUserStats = (userId) => {
  const url = userId ? `/stats/user/${userId}` : '/stats/user/me'
  return api.get(url)
}

/**
 * 获取文章统计信息
 * @param {number} postId - 文章ID
 * @returns {Promise<Object>} 文章统计信息
 */
export const getPostStats = (postId) => {
  return api.get(`/stats/post/${postId}`)
}

/**
 * 获取访问量趋势
 * @param {string} period - 时间段 (day, week, month, year)
 * @returns {Promise<Object>} 趋势数据
 */
export const getVisitTrends = (period = 'week') => {
  return api.get(`/stats/trends/visits?period=${period}`)
}

/**
 * 获取新增用户趋势
 * @param {string} period - 时间段 (day, week, month, year)
 * @returns {Promise<Object>} 趋势数据
 */
export const getUserTrends = (period = 'week') => {
  return api.get(`/stats/trends/users?period=${period}`)
}

/**
 * 获取热门文章
 * @param {number} limit - 数量限制
 * @returns {Promise<Array>} 热门文章列表
 */
export const getHotPosts = (limit = 10) => {
  return api.get(`/stats/hot-posts?limit=${limit}`)
}

/**
 * 获取热门搜索关键词
 * @param {number} limit - 数量限制
 * @returns {Promise<Array>} 热门关键词列表
 */
export const getHotSearchKeywords = (limit = 10) => {
  return api.get(`/stats/hot-keywords?limit=${limit}`)
}

/**
 * 记录文章浏览
 * @param {number} postId - 文章ID
 * @returns {Promise<Object>} 响应结果
 */
export const recordPostView = (postId) => {
  return api.post(`/stats/post/${postId}/view`)
}

// 导出所有方法
export const stats = {
  getBlogStats,
  getUserStats,
  getPostStats,
  getVisitTrends,
  getUserTrends,
  getHotPosts,
  getHotSearchKeywords,
  recordPostView
} 