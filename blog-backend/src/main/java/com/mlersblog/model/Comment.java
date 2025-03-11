package com.mlersblog.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 评论实体类
 * 
 * 对应数据库中的comment表，存储用户对文章的评论信息
 * 支持评论嵌套，即评论的回复功能
 * 与User是多对一关系（多个评论对应一个作者）
 * 与Post是多对一关系（多个评论对应一篇文章）
 * 与自身是多对一关系（多个回复对应一个父评论）
 */
@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 评论ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 评论内容

    // 多对一关系：多个评论对应一篇文章
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post; // 所属文章

    /**
     * 设置文章（内部方法，供Post类使用）
     * 
     * @param post 文章对象
     */
    public void setPostInternal(Post post) {
        this.post = post;
    }

    // 多对一关系：多个评论对应一个作者
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private User author; // 评论作者

    // 多对一关系：多个回复对应一个父评论
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private Comment parent; // 父评论，用于实现回复功能

    // 多对一关系：回复的目标用户
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_user_id")
    @ToString.Exclude
    private User replyToUser; // 回复目标用户

    // 一对多关系：一个评论可以有多个回复
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Comment> replies = new ArrayList<>(); // 回复列表

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status = CommentStatus.APPROVED; // 评论状态，默认为已批准

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0; // 点赞数

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 创建时间
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间

    /**
     * 评论状态枚举
     */
    public enum CommentStatus {
        APPROVED, // 已批准
        PENDING,  // 待审核
        REJECTED  // 已拒绝
    }

    /**
     * 添加回复
     * 
     * @param reply 回复评论
     */
    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParent(this);
    }

    /**
     * 移除回复
     * 
     * @param reply 回复评论
     */
    public void removeReply(Comment reply) {
        replies.remove(reply);
        reply.setParent(null);
    }

    /**
     * 增加点赞数
     */
    public void incrementLikeCount() {
        this.likeCount++;
    }

    /**
     * 减少点赞数
     */
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    /**
     * 判断是否为回复评论
     * 
     * @return 如果是回复评论则返回true，否则返回false
     */
    public boolean isReply() {
        return parent != null;
    }

    /**
     * 审核通过
     */
    public void approve() {
        this.status = CommentStatus.APPROVED;
    }

    /**
     * 拒绝评论
     */
    public void reject() {
        this.status = CommentStatus.REJECTED;
    }
} 