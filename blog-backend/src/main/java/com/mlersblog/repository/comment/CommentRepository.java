package com.mlersblog.repository.comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mlersblog.model.Comment;
import com.mlersblog.model.Comment.CommentStatus;

/**
 * 评论存储库接口
 * 
 * 提供评论相关的数据库操作
 * 继承JpaRepository获取基本的CRUD操作
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * 根据文章ID查询评论
     * 
     * @param postId 文章ID
     * @return 评论列表
     */
    List<Comment> findByPostId(Long postId);
    
    /**
     * 根据作者ID查询评论
     * 
     * @param authorId 作者ID
     * @return 评论列表
     */
    List<Comment> findByAuthorId(Long authorId);
    
    /**
     * 根据用户ID查询评论，按创建时间降序排序
     *
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 分页查询用户的评论，按创建时间降序排序
     *
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<Comment> findByAuthorIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * 根据文章ID查询顶级评论（没有父评论的评论），按创建时间降序排序
     *
     * @param postId 文章ID
     * @return 评论列表
     */
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtDesc(Long postId);
    
    /**
     * 分页查询文章的顶级评论（没有父评论的评论），按创建时间降序排序
     *
     * @param postId 文章ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<Comment> findByPostIdAndParentIsNullOrderByCreatedAtDesc(Long postId, Pageable pageable);
    
    /**
     * 根据父评论ID查询回复，按创建时间升序排序
     *
     * @param parentId 父评论ID
     * @return 回复评论列表
     */
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
    
    /**
     * 分页查询父评论的回复，按创建时间升序排序
     *
     * @param parentId 父评论ID
     * @param pageable 分页参数
     * @return 回复评论分页结果
     */
    Page<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId, Pageable pageable);
    
    /**
     * 查询最新的n条评论
     *
     * @param limit 限制数量
     * @return 最新评论列表
     */
    @Query(value = "SELECT c FROM Comment c WHERE c.status = 'APPROVED' ORDER BY c.createdAt DESC LIMIT :limit")
    List<Comment> findTopByOrderByCreatedAtDesc(@Param("limit") int limit);
    
    /**
     * 根据文章ID和状态查询评论
     * 
     * @param postId 文章ID
     * @param status 评论状态
     * @return 评论列表
     */
    List<Comment> findByPostIdAndStatus(Long postId, CommentStatus status);
    
    /**
     * 根据父评论ID查询回复
     * 
     * @param parentId 父评论ID
     * @return 回复评论列表
     */
    List<Comment> findByParentId(Long parentId);
    
    /**
     * 查询顶级评论（没有父评论的评论）
     * 
     * @param postId 文章ID
     * @param pageable 分页参数
     * @return 顶级评论分页结果
     */
    Page<Comment> findByPostIdAndParentIdIsNullAndStatus(Long postId, CommentStatus status, Pageable pageable);
    
    /**
     * 根据文章ID分页查询评论
     * 
     * @param postId 文章ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<Comment> findByPostId(Long postId, Pageable pageable);
    
    /**
     * 根据作者ID分页查询评论
     * 
     * @param authorId 作者ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);
    
    /**
     * 根据评论状态分页查询
     * 
     * @param status 评论状态
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<Comment> findByStatus(CommentStatus status, Pageable pageable);
    
    /**
     * 查询最近评论
     * 
     * @param pageable 分页参数
     * @return 评论列表
     */
    @Query("SELECT c FROM Comment c WHERE c.status = 'APPROVED' ORDER BY c.createdAt DESC")
    List<Comment> findRecentComments(Pageable pageable);
    
    /**
     * 计算文章评论数
     * 
     * @param postId 文章ID
     * @return 评论数
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId AND c.status = 'APPROVED'")
    long countByPostId(@Param("postId") Long postId);
    
    /**
     * 更新评论点赞数
     * 
     * @param commentId 评论ID
     * @param value 更新值（增加或减少）
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + :value WHERE c.id = :commentId")
    int updateLikeCount(@Param("commentId") Long commentId, @Param("value") int value);
    
    /**
     * 根据关键字搜索评论
     * 
     * @param keyword 关键字
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword% ORDER BY c.createdAt DESC")
    Page<Comment> search(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 删除文章的所有评论
     * 
     * @param postId 文章ID
     * @return 受影响的行数
     */
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.id = :postId")
    int deleteByPostId(@Param("postId") Long postId);
    
    /**
     * 查询用户收到的评论和回复
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    @Query("SELECT c FROM Comment c WHERE (c.post.author.id = :userId AND c.author.id != :userId) OR c.replyToUser.id = :userId ORDER BY c.createdAt DESC")
    Page<Comment> findCommentsByUserReceived(@Param("userId") Long userId, Pageable pageable);
} 