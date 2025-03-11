// blog-frontend/api/post.js
import api from './index'

// 获取文章列表 - 添加/api前缀
export const getPosts = (page = 0, size = 10) => {
  return api.get(`/api/posts?page=${page}&size=${size}`)
}

export const getPostById = (id) => {
  return api.get(`/api/posts/${id}`)
}

export const getPostsByCategory = (categoryId, page = 0, size = 10) => {
  return api.get(`/api/posts/category/${categoryId}?page=${page}&size=${size}`)
}

export const getPostsByTag = (tagId, page = 0, size = 10) => {
  return api.get(`/api/posts/tag/${tagId}?page=${page}&size=${size}`)
}

export const searchPosts = (keyword, page = 0, size = 10) => {
  return api.get(`/api/posts/search?keyword=${keyword}&page=${page}&size=${size}`)
}

export const createPost = (postData) => {
  return api.post('/api/posts', postData)
}

export const updatePost = (id, postData) => {
  return api.put(`/api/posts/${id}`, postData)
}

export const deletePost = (id) => {
  return api.delete(`/api/posts/${id}`)
}

export const publishPost = (id) => {
  return api.put(`/api/posts/${id}/publish`)
}

export const saveAsDraft = (id) => {
  return api.put(`/api/posts/${id}/draft`)
}

// 导出所有方法
export const posts = {
  getPosts,
  getPostById,
  getPostsByCategory,
  getPostsByTag,
  searchPosts,
  createPost,
  updatePost,
  deletePost,
  publishPost,
  saveAsDraft
}