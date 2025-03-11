package com.mlersblog.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mlersblog.model.Tag;

/**
 * 标签存储库接口
 * 
 * 提供文章标签的数据库操作
 * 继承JpaRepository获取基本的CRUD操作
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    /**
     * 根据标签名称查找标签
     * 
     * @param name 标签名称
     * @return 包含标签的Optional对象
     */
    Optional<Tag> findByName(String name);
    
    /**
     * 检查标签名称是否存在
     * 
     * @param name 标签名称
     * @return 如果存在则返回true，否则返回false
     */
    boolean existsByName(String name);
    
    /**
     * 查询所有标签，按名称排序
     * 
     * @return 标签列表
     */
    List<Tag> findAllByOrderByName();
    
    /**
     * 根据名称列表查找标签
     * 
     * @param names 标签名称列表
     * @return 标签列表
     */
    List<Tag> findByNameIn(List<String> names);
    
    /**
     * 分页查询标签
     * 
     * @param pageable 分页参数
     * @return 标签分页结果
     */
    Page<Tag> findAll(Pageable pageable);
    
    /**
     * 查询热门标签（使用频率最高的标签）
     * 
     * @param limit 限制数量
     * @return 热门标签列表
     */
    @Query(value = "SELECT t FROM Tag t JOIN t.posts p WHERE p.status = 'PUBLISHED' GROUP BY t ORDER BY COUNT(p) DESC")
    List<Tag> findPopularTags(int limit);
    
    /**
     * 查询每个标签的文章数量
     * 
     * @return 包含标签ID、名称和文章数量的对象数组
     */
    @Query("SELECT t.id, t.name, COUNT(p) FROM Tag t LEFT JOIN t.posts p WHERE p.status = 'PUBLISHED' GROUP BY t.id, t.name")
    List<Object[]> findTagsWithPostCount();
    
    /**
     * 根据关键字搜索标签
     * 
     * @param keyword 关键字
     * @return 标签列表
     */
    @Query("SELECT t FROM Tag t WHERE t.name LIKE %:keyword%")
    List<Tag> search(@Param("keyword") String keyword);
    
    /**
     * 查询某篇文章的所有标签
     * 
     * @param postId 文章ID
     * @return 标签集合
     */
    @Query("SELECT t FROM Tag t JOIN t.posts p WHERE p.id = :postId")
    Set<Tag> findByPostId(@Param("postId") Long postId);
} 