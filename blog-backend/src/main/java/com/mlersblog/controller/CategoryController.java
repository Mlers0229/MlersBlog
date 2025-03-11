package com.mlersblog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mlersblog.dto.CategoryDTO;
import com.mlersblog.dto.request.CategoryRequest;
import com.mlersblog.model.Category;
import com.mlersblog.service.CategoryService;

import jakarta.validation.Valid;

/**
 * 分类控制器
 * 
 * 处理分类相关的HTTP请求
 */
@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    /**
     * 获取所有分类
     * 
     * @return 分类列表
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }
    
    /**
     * 分页获取所有分类
     * 
     * @param page 页码
     * @param size 每页大小
     * @param sort 排序字段
     * @param direction 排序方向
     * @return 分类分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<Page<CategoryDTO>> getAllCategoriesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Category> categoriesPage = categoryService.getAllCategories(pageable);
        
        Page<CategoryDTO> categoryDTOPage = categoriesPage.map(this::convertToDTO);
        return ResponseEntity.ok(categoryDTOPage);
    }
    
    /**
     * 根据ID获取分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(convertToDTO(category));
    }
    
    /**
     * 创建分类（需要管理员权限）
     * 
     * @param categoryRequest 分类请求数据
     * @return 创建的分类
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        
        Category createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(convertToDTO(createdCategory), HttpStatus.CREATED);
    }
    
    /**
     * 更新分类（需要管理员权限）
     * 
     * @param id 分类ID
     * @param categoryRequest 分类请求数据
     * @return 更新后的分类
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id, 
            @Valid @RequestBody CategoryRequest categoryRequest) {
        
        Category category = categoryService.getCategoryById(id);
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        
        Category updatedCategory = categoryService.updateCategory(category);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }
    
    /**
     * 删除分类（需要管理员权限）
     * 
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 获取有文章的分类
     * 
     * @return 分类列表
     */
    @GetMapping("/with-posts")
    public ResponseEntity<List<CategoryDTO>> getCategoriesWithPosts() {
        List<Category> categories = categoryService.getCategoriesWithPosts();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }
    
    /**
     * 获取每个分类的文章数量
     * 
     * @return 分类及其文章数量
     */
    @GetMapping("/post-count")
    public ResponseEntity<List<Map<String, Object>>> getCategoriesWithPostCount() {
        List<Map<String, Object>> categoriesWithCount = categoryService.getCategoriesWithPostCount();
        return ResponseEntity.ok(categoriesWithCount);
    }
    
    /**
     * 检查分类名称是否存在
     * 
     * @param name 分类名称
     * @return 检查结果
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkCategoryName(@RequestParam String name) {
        boolean exists = categoryService.existsByName(name);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 转换Category对象为CategoryDTO
     * 
     * @param category 分类对象
     * @return 分类DTO
     */
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }
} 