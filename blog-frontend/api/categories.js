// blog-frontend/api/categories.js
import api from './index'

export const getCategories = () => {
  return api.get('/categories')
}

export const getCategoryById = (id) => {
  return api.get(`/categories/${id}`)
}

export const createCategory = (data) => {
  return api.post('/categories', data)
}

export const updateCategory = (id, data) => {
  return api.put(`/categories/${id}`, data)
}

export const deleteCategory = (id) => {
  return api.delete(`/categories/${id}`)
}

export const getCategoriesWithPostCount = () => {
  return api.get('/categories/post-count')
}

// 导出所有方法
export const categories = {
  getCategories,
  getCategoryById,
  createCategory,
  updateCategory,
  deleteCategory,
  getCategoriesWithPostCount
}