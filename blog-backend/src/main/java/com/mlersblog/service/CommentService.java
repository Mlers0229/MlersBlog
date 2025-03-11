package com.mlersblog.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mlersblog.model.Comment;

/**
 * 评论服务接口
 * 
 * 定义评论相关的业务逻辑方法
 */
public interface CommentService {
    
    /**
     * 根据ID获取评论
     * 
     * @param id 评论ID
     * @return 评论对象
     * @throws ResourceNotFoundException 如果评论不存在
     */
    Comment getCommentById(Long id);
    
    /**
     * 创建评论
     * 
     * @param comment 评论对象
     * @param postId 文章ID
     * @param userId 用户ID，可为null（游客评论）
     * @return 创建的评论
     * @throws ResourceNotFoundException 如果文章或用户不存在
     */
    Comment createComment(Comment comment, Long postId, Long userId);
    
    /**
     * 创建回复评论
     * 
     * @param comment 评论对象
     * @param postId 文章ID
     * @param parentId 父评论ID
     * @param userId 用户ID，可为null（游客评论）
     * @return 创建的回复评论
     * @throws ResourceNotFoundException 如果文章、父评论或用户不存在
     */
    Comment createReply(Comment comment, Long postId, Long parentId, Long userId);
    
    /**
     * 更新评论
     * 
     * @param comment 评论对象
     * @param currentUserId 当前用户ID
     * @return 更新后的评论
     * @throws ResourceNotFoundException 如果评论不存在
     * @throws UnauthorizedException 如果当前用户无权限更新评论
     */
    Comment updateComment(Comment comment, Long currentUserId);
    
    /**
     * 删除评论
     * 
     * @param id 评论ID
     * @param currentUserId 当前用户ID
     * @throws ResourceNotFoundException 如果评论不存在
     * @throws UnauthorizedException 如果当前用户无权限删除评论
     */
    void deleteComment(Long id, Long currentUserId);
    
    /**
     * 获取文章的所有评论
     * 
     * @param postId 文章ID
     * @return 评论列表
     */
    List<Comment> getCommentsByPostId(Long postId);
    
    /**
     * 分页获取文章的评论
     * 
     * @param postId 文章ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<Comment> getCommentsByPostId(Long postId, Pageable pageable);
    
    /**
     * 获取用户的所有评论
     * 
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Comment> getCommentsByUserId(Long userId);
    
    /**
     * 分页获取用户的评论
     * 
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<Comment> getCommentsByUserId(Long userId, Pageable pageable);
    
    /**
     * 获取评论的回复
     * 
     * @param commentId 评论ID
     * @return 回复列表
     */
    List<Comment> getRepliesByCommentId(Long commentId);
    
    /**
     * 分页获取评论的回复
     * 
     * @param commentId 评论ID
     * @param pageable 分页参数
     * @return 回复分页结果
     */
    Page<Comment> getRepliesByCommentId(Long commentId, Pageable pageable);
    
    /**
     * 获取所有评论
     * 
     * @param pageable 分页参数
     * @return 评论分页结果
     */
    Page<Comment> getAllComments(Pageable pageable);
    
    /**
     * 获取最新评论
     * 
     * @param limit 限制数量
     * @return 最新评论列表
     */
    List<Comment> getRecentComments(int limit);
    
    /**
     * 获取待审核评论
     * 
     * @param pageable 分页参数
     * @return 待审核评论分页结果
     */
    Page<Comment> getPendingComments(Pageable pageable);
    
    /**
     * 审核评论
     * 
     * @param id 评论ID
     * @param approved 是否通过
     * @return 审核后的评论
     */
    Comment approveComment(Long id, boolean approved);
} 