package com.mlersblog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mlersblog.exception.ResourceNotFoundException;
import com.mlersblog.exception.UnauthorizedException;
import com.mlersblog.model.Comment;
import com.mlersblog.model.Comment.CommentStatus;
import com.mlersblog.model.Post;
import com.mlersblog.model.User;
import com.mlersblog.repository.post.PostRepository;
import com.mlersblog.repository.user.UserRepository;
import com.mlersblog.repository.comment.CommentRepository;

/**
 * 评论服务实现类
 * 
 * 实现CommentService接口中定义的所有方法，
 * 处理评论相关业务逻辑
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("评论不存在，ID: " + id));
    }

    @Override
    @Transactional
    public Comment createComment(Comment comment, Long postId, Long userId) {
        // 检查文章是否存在
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在，ID: " + postId));
        
        // 设置文章关联
        comment.setPost(post);
        
        // 如果有用户ID，则设置用户关联
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + userId));
            comment.setAuthor(user);
        }
        
        // 设置评论时间和初始状态
        comment.setCreatedAt(LocalDateTime.now());
        
        // 根据系统设置决定评论是否需要审核
        // 这里简单处理，可以在实际应用中根据配置调整
        boolean needsApproval = false; // 可以从系统配置中获取
        comment.setStatus(needsApproval ? CommentStatus.PENDING : CommentStatus.APPROVED);
        
        // 保存评论
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment createReply(Comment comment, Long postId, Long parentId, Long userId) {
        // 检查父评论是否存在
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("父评论不存在，ID: " + parentId));
        
        // 设置父评论关联
        comment.setParent(parentComment);
        
        // 设置回复目标用户为父评论的作者
        comment.setReplyToUser(parentComment.getAuthor());
        
        // 创建评论，复用已有方法
        return createComment(comment, postId, userId);
    }

    @Override
    @Transactional
    public Comment updateComment(Comment comment, Long currentUserId) {
        // 检查评论是否存在
        Comment existingComment = getCommentById(comment.getId());
        
        // 检查权限：只有评论作者或管理员可以更新评论
        if (existingComment.getAuthor() != null && 
            !existingComment.getAuthor().getId().equals(currentUserId) &&
            !isUserAdmin(currentUserId)) {
            throw new UnauthorizedException("您没有权限更新此评论");
        }
        
        // 更新评论内容
        existingComment.setContent(comment.getContent());
        
        // 保存更新后的评论
        return commentRepository.save(existingComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id, Long currentUserId) {
        // 检查评论是否存在
        Comment comment = getCommentById(id);
        
        // 检查权限：只有评论作者、文章作者或管理员可以删除评论
        boolean isCommentAuthor = comment.getAuthor() != null && comment.getAuthor().getId().equals(currentUserId);
        boolean isPostAuthor = comment.getPost().getAuthor().getId().equals(currentUserId);
        
        if (!isCommentAuthor && !isPostAuthor && !isUserAdmin(currentUserId)) {
            throw new UnauthorizedException("您没有权限删除此评论");
        }
        
        // 删除评论
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtDesc(postId);
    }

    @Override
    public Page<Comment> getCommentsByPostId(Long postId, Pageable pageable) {
        return commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtDesc(postId, pageable);
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByAuthorIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Page<Comment> getCommentsByUserId(Long userId, Pageable pageable) {
        return commentRepository.findByAuthorIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public List<Comment> getRepliesByCommentId(Long commentId) {
        return commentRepository.findByParentIdOrderByCreatedAtAsc(commentId);
    }

    @Override
    public Page<Comment> getRepliesByCommentId(Long commentId, Pageable pageable) {
        return commentRepository.findByParentIdOrderByCreatedAtAsc(commentId, pageable);
    }

    @Override
    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public List<Comment> getRecentComments(int limit) {
        return commentRepository.findTopByOrderByCreatedAtDesc(limit);
    }

    @Override
    public Page<Comment> getPendingComments(Pageable pageable) {
        return commentRepository.findByStatus(CommentStatus.PENDING, pageable);
    }

    @Override
    @Transactional
    public Comment approveComment(Long id, boolean approved) {
        // 检查评论是否存在
        Comment comment = getCommentById(id);
        
        // 设置评论状态
        comment.setStatus(approved ? CommentStatus.APPROVED : CommentStatus.REJECTED);
        
        // 保存更新后的评论
        return commentRepository.save(comment);
    }
    
    /**
     * 检查用户是否为管理员
     * 
     * @param userId 用户ID
     * @return 如果是管理员则返回true，否则返回false
     */
    private boolean isUserAdmin(Long userId) {
        // 此方法应该根据实际的用户角色系统实现
        // 示例实现，需要根据实际情况调整
        if (userId == null) {
            return false;
        }
        
        return userRepository.findById(userId)
                .map(user -> "ADMIN".equals(user.getRole()))
                .orElse(false);
    }
} 