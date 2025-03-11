package com.mlersblog.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mlersblog.model.Category;

/**
 * 分类服务接口
 * 
 * 定义分类相关的业务逻辑方法
 */
public interface CategoryService {
    
    /**
     * 根据ID获取分类
     * 
     * @param id 分类ID
     * @return 分类对象
     * @throws ResourceNotFoundException 如果分类不存在
     */
    Category getCategoryById(Long id);
    
    /**
     * 根据名称获取分类
     * 
     * @param name 分类名称
     * @return 分类对象
     * @throws ResourceNotFoundException 如果分类不存在
     */
    Category getCategoryByName(String name);
    
    /**
     * 创建新分类
     * 
     * @param category 分类对象
     * @return 创建的分类
     * @throws CategoryAlreadyExistsException 如果分类名称已存在
     */
    Category createCategory(Category category);
    
    /**
     * 更新分类
     * 
     * @param category 分类对象
     * @return 更新后的分类
     */
    Category updateCategory(Category category);
    
    /**
     * 删除分类
     * 
     * @param id 分类ID
     */
    void deleteCategory(Long id);
    
    /**
     * 获取所有分类
     * 
     * @return 分类列表
     */
    List<Category> getAllCategories();
    
    /**
     * 分页获取所有分类
     * 
     * @param pageable 分页参数
     * @return 分类分页结果
     */
    Page<Category> getAllCategories(Pageable pageable);
    
    /**
     * 获取有发布文章的分类
     * 
     * @return 有文章的分类列表
     */
    List<Category> getCategoriesWithPosts();
    
    /**
     * 获取每个分类的文章数量
     * 
     * @return 分类ID-名称-文章数量的映射
     */
    List<Map<String, Object>> getCategoriesWithPostCount();
    
    /**
     * 检查分类名称是否已存在
     * 
     * @param name 分类名称
     * @return 如果存在则返回true，否则返回false
     */
    boolean existsByName(String name);
} 