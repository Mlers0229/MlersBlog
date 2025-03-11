package com.mlersblog.repository.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mlersblog.model.Post;
import com.mlersblog.model.Post.PostStatus;

/**
 * 文章存储库接口
 * 
 * 提供文章相关的数据库操作
 * 继承JpaRepository获取基本的CRUD操作
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * 根据作者ID查找文章
     * 
     * @param authorId 作者ID
     * @return 文章列表
     */
    List<Post> findByAuthorId(Long authorId);
    
    /**
     * 根据状态查找文章
     * 
     * @param status 文章状态
     * @return 文章列表
     */
    List<Post> findByStatus(PostStatus status);
    
    /**
     * 根据作者ID和状态查找文章
     * 
     * @param authorId 作者ID
     * @param status 文章状态
     * @return 文章列表
     */
    List<Post> findByAuthorIdAndStatus(Long authorId, PostStatus status);
    
    /**
     * 根据作者ID和状态分页查询文章
     * 
     * @param authorId 作者ID
     * @param status 文章状态
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> findByAuthorIdAndStatus(Long authorId, PostStatus status, Pageable pageable);
    
    /**
     * 根据分类ID查找已发布的文章
     * 
     * @param categoryId 分类ID
     * @return 文章列表
     */
    List<Post> findByCategoryIdAndStatus(Long categoryId, PostStatus status);
    
    /**
     * 根据分类ID分页查询已发布的文章
     * 
     * @param categoryId 分类ID
     * @param status 文章状态
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> findByCategoryIdAndStatus(Long categoryId, PostStatus status, Pageable pageable);
    
    /**
     * 查询最近发布的文章
     * 
     * @param pageable 分页参数
     * @return 文章列表
     */
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' ORDER BY p.publishedAt DESC")
    List<Post> findRecentPosts(Pageable pageable);
    
    /**
     * 分页查询所有文章，按创建时间倒序排序
     * 
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * 分页查询已发布的文章，按发布时间倒序排序
     * 
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> findByStatusOrderByPublishedAtDesc(PostStatus status, Pageable pageable);
    
    /**
     * 根据作者ID分页查询文章
     * 
     * @param authorId 作者ID
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    Page<Post> findByAuthorId(Long authorId, Pageable pageable);
    
    /**
     * 根据标签ID查询已发布的文章
     * 
     * @param tagId 标签ID
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId AND p.status = 'PUBLISHED' ORDER BY p.publishedAt DESC")
    Page<Post> findByTagId(@Param("tagId") Long tagId, Pageable pageable);
    
    /**
     * 全文搜索（标题、摘要和内容）
     * 
     * @param keyword 关键字
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND (p.title LIKE %:keyword% OR p.summary LIKE %:keyword% OR p.content LIKE %:keyword%) ORDER BY p.publishedAt DESC")
    Page<Post> search(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 查询指定时间范围内的文章（用于归档）
     * 
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param pageable 分页参数
     * @return 文章分页结果
     */
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND p.publishedAt BETWEEN :startDate AND :endDate ORDER BY p.publishedAt DESC")
    Page<Post> findByPublishedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    /**
     * 查询热门文章（浏览量最高）
     * 
     * @param pageable 分页参数
     * @return 文章列表
     */
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' ORDER BY p.viewCount DESC")
    List<Post> findPopularPosts(Pageable pageable);
    
    /**
     * 查询推荐文章（点赞和评论综合最高）
     * 
     * @param pageable 分页参数
     * @return 文章列表
     */
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' ORDER BY (p.likeCount + p.commentCount) DESC")
    List<Post> findRecommendedPosts(Pageable pageable);
    
    /**
     * 更新文章浏览量
     * 
     * @param postId 文章ID
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    int incrementViewCount(@Param("postId") Long postId);
    
    /**
     * 更新文章点赞数
     * 
     * @param postId 文章ID
     * @param value 更新值（增加或减少）
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + :value WHERE p.id = :postId")
    int updateLikeCount(@Param("postId") Long postId, @Param("value") int value);
    
    /**
     * 更新文章收藏数
     * 
     * @param postId 文章ID
     * @param value 更新值（增加或减少）
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE Post p SET p.favoriteCount = p.favoriteCount + :value WHERE p.id = :postId")
    int updateFavoriteCount(@Param("postId") Long postId, @Param("value") int value);
    
    /**
     * 更新文章评论数
     * 
     * @param postId 文章ID
     * @param value 更新值（增加或减少）
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + :value WHERE p.id = :postId")
    int updateCommentCount(@Param("postId") Long postId, @Param("value") int value);
    
    /**
     * 查询指定作者的文章统计信息
     * 
     * @param authorId 作者ID
     * @return 包含已发布文章数、总浏览量、总点赞数的对象数组
     */
    @Query("SELECT COUNT(p), SUM(p.viewCount), SUM(p.likeCount) FROM Post p WHERE p.author.id = :authorId AND p.status = 'PUBLISHED'")
    Object[] getAuthorStats(@Param("authorId") Long authorId);
} 