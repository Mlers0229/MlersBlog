import api from './index'

/**
 * 获取当前用户的通知列表
 * @param {number} page - 页码，从0开始
 * @param {number} size - 每页数量
 * @returns {Promise<Object>} 分页的通知列表
 */
export const getNotifications = (page = 0, size = 20) => {
  return api.get(`/notifications?page=${page}&size=${size}`)
}

/**
 * 获取未读通知数量
 * @returns {Promise<Object>} 包含未读数量的响应
 */
export const getUnreadCount = () => {
  return api.get('/notifications/unread-count')
}

/**
 * 标记通知为已读
 * @param {number} notificationId - 通知ID
 * @returns {Promise<Object>} 响应结果
 */
export const markAsRead = (notificationId) => {
  return api.put(`/notifications/${notificationId}/read`)
}

/**
 * 标记所有通知为已读
 * @returns {Promise<Object>} 响应结果
 */
export const markAllAsRead = () => {
  return api.put('/notifications/read-all')
}

/**
 * 删除通知
 * @param {number} notificationId - 通知ID
 * @returns {Promise<Object>} 响应结果
 */
export const deleteNotification = (notificationId) => {
  return api.delete(`/notifications/${notificationId}`)
}

/**
 * 删除所有通知
 * @returns {Promise<Object>} 响应结果
 */
export const deleteAllNotifications = () => {
  return api.delete('/notifications/all')
}

/**
 * 获取通知设置
 * @returns {Promise<Object>} 通知设置
 */
export const getNotificationSettings = () => {
  return api.get('/notifications/settings')
}

/**
 * 更新通知设置
 * @param {Object} settings - 通知设置对象
 * @returns {Promise<Object>} 更新后的设置
 */
export const updateNotificationSettings = (settings) => {
  return api.put('/notifications/settings', settings)
}

// 导出所有方法
export const notification = {
  getNotifications,
  getUnreadCount,
  markAsRead,
  markAllAsRead,
  deleteNotification,
  deleteAllNotifications,
  getNotificationSettings,
  updateNotificationSettings
} 