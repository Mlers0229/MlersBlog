package com.mlersblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mlersblog.model.Category;

/**
 * 分类存储库接口
 * 
 * 提供文章分类的数据库操作
 * 继承JpaRepository获取基本的CRUD操作
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 根据分类名称查找分类
     * 
     * @param name 分类名称
     * @return 包含分类的Optional对象
     */
    Optional<Category> findByName(String name);
    
    /**
     * 检查分类名称是否存在
     * 
     * @param name 分类名称
     * @return 如果存在则返回true，否则返回false
     */
    boolean existsByName(String name);
    
    /**
     * 查询所有分类，按名称排序
     * 
     * @return 分类列表
     */
    List<Category> findAllByOrderByName();
    
    /**
     * 查询所有有文章的分类（包括草稿和已发布）
     * 
     * @return 有文章的分类列表
     */
    @Query("SELECT DISTINCT c FROM Category c JOIN c.posts p ORDER BY c.name")
    List<Category> findCategoriesWithPosts();
    
    /**
     * 查询具有已发布文章的分类（用于前端显示）
     * 
     * @return 有已发布文章的分类列表
     */
    @Query("SELECT DISTINCT c FROM Category c JOIN c.posts p WHERE p.status = 'PUBLISHED' ORDER BY c.name")
    List<Category> findCategoriesWithPublishedPosts();
    
    /**
     * 查询每个分类下的文章数量
     * 
     * @return 包含分类ID、名称和文章数量的对象数组
     */
    @Query("SELECT c.id, c.name, COUNT(p) FROM Category c LEFT JOIN c.posts p WHERE p.status = 'PUBLISHED' GROUP BY c.id, c.name")
    List<Object[]> findCategoriesWithPostCount();
} 