import api from './index'

/**
 * 上传图片
 * @param {FormData} formData - 包含图片文件的FormData对象
 * @returns {Promise<Object>} 包含图片URL的响应
 */
export const uploadImage = (formData) => {
  return api.post('/upload/image', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传文章封面图片
 * @param {FormData} formData - 包含封面图片文件的FormData对象
 * @returns {Promise<Object>} 包含图片URL的响应
 */
export const uploadCoverImage = (formData) => {
  return api.post('/upload/cover', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传文件附件
 * @param {FormData} formData - 包含文件的FormData对象
 * @returns {Promise<Object>} 包含文件URL和信息的响应
 */
export const uploadFile = (formData) => {
  return api.post('/upload/file', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 从URL导入图片
 * @param {Object} data - 包含图片URL的对象 {url: string}
 * @returns {Promise<Object>} 包含导入后图片URL的响应
 */
export const importImageFromUrl = (data) => {
  return api.post('/upload/import-image', data)
}

/**
 * 删除已上传的文件
 * @param {string} fileKey - 文件的唯一标识符
 * @returns {Promise<Object>} 响应结果
 */
export const deleteUploadedFile = (fileKey) => {
  return api.delete(`/upload/file/${fileKey}`)
}

// 导出所有方法
export const upload = {
  uploadImage,
  uploadCoverImage,
  uploadFile,
  importImageFromUrl,
  deleteUploadedFile
} 