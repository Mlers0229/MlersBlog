// blog-frontend/api/tags.js
import api from './index'

export const getTags = () => {
  return api.get('/tags')
}

export const getTagById = (id) => {
  return api.get(`/tags/${id}`)
}

export const createTag = (data) => {
  return api.post('/tags', data)
}

export const updateTag = (id, data) => {
  return api.put(`/tags/${id}`, data)
}

export const deleteTag = (id) => {
  return api.delete(`/tags/${id}`)
}

export const getPopularTags = (limit = 10) => {
  return api.get(`/tags/popular?limit=${limit}`)
}

// 导出所有方法
export const tags = {
  getTags,
  getTagById,
  createTag,
  updateTag,
  deleteTag,
  getPopularTags
}