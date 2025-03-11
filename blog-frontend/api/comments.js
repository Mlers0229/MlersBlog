// blog-frontend/api/comments.js
import api from './index'

export const getCommentsByPost = (postId, page = 0, size = 10) => {
  return api.get(`/comments/post/${postId}?page=${page}&size=${size}`)
}

export const createComment = (commentData) => {
  return api.post('/comments', commentData)
}

export const createReply = (parentId, commentData) => {
  return api.post(`/comments/reply/${parentId}`, commentData)
}

export const updateComment = (id, data) => {
  return api.put(`/comments/${id}`, data)
}

export const deleteComment = (id) => {
  return api.delete(`/comments/${id}`)
}

// 导出所有方法
export const comments = {
  getCommentsByPost,
  createComment,
  createReply,
  updateComment,
  deleteComment
}