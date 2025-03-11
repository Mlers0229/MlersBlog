package com.mlersblog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mlersblog.exception.CategoryAlreadyExistsException;
import com.mlersblog.exception.ResourceNotFoundException;
import com.mlersblog.model.Category;
import com.mlersblog.repository.CategoryRepository;
import com.mlersblog.repository.post.PostRepository;

/**
 * 分类服务实现类
 * 
 * 实现CategoryService接口中定义的所有方法，
 * 处理分类相关业务逻辑
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private PostRepository postRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + id));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在，名称: " + name));
    }

    @Override
    public Category createCategory(Category category) {
        // 检查分类名称是否已存在
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistsException("分类名称已存在: " + category.getName());
        }
        
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        // 检查分类是否存在
        Category existingCategory = getCategoryById(category.getId());
        
        // 检查更新的名称是否与其他分类冲突
        Optional<Category> categoryWithSameName = categoryRepository.findByName(category.getName());
        if (categoryWithSameName.isPresent() && !categoryWithSameName.get().getId().equals(category.getId())) {
            throw new CategoryAlreadyExistsException("分类名称已存在: " + category.getName());
        }
        
        // 更新分类信息
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        // 检查分类是否存在
        getCategoryById(id);
        
        // 删除分类
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> getCategoriesWithPosts() {
        return categoryRepository.findCategoriesWithPosts();
    }

    @Override
    public List<Map<String, Object>> getCategoriesWithPostCount() {
        List<Object[]> results = categoryRepository.findCategoriesWithPostCount();
        List<Map<String, Object>> categoryStats = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", result[0]);
            categoryMap.put("name", result[1]);
            categoryMap.put("postCount", result[2]);
            categoryStats.add(categoryMap);
        }
        
        return categoryStats;
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
} 