package com.mlersblog.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
 * 文章实体类
 * 
 * 对应数据库中的post表，存储博客文章信息
 * 包括标题、内容、摘要、封面图、浏览量等
 * 与User是多对一关系（多篇文章对应一个作者）
 * 与Category是多对一关系（多篇文章对应一个分类）
 * 与Tag是多对多关系（一篇文章可以有多个标签，一个标签可以被多篇文章使用）
 */
@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 文章ID

    @Column(nullable = false)
    private String title; // 文章标题

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content; // 文章内容，使用Markdown格式

    @Column(length = 500)
    private String summary; // 文章摘要

    @Column(name = "cover_image")
    private String coverImage; // 封面图URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.DRAFT; // 文章状态，默认为草稿

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0; // 浏览量

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0; // 点赞数

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount = 0; // 评论数

    @Column(name = "favorite_count", nullable = false)
    private Integer favoriteCount = 0; // 收藏数

    // 多对一关系：多篇文章对应一个作者
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude // 避免ToString循环引用
    private User author; // 文章作者

    // 多对一关系：多篇文章对应一个分类
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category; // 文章分类

    // 多对多关系：一篇文章可以有多个标签
    // JoinTable定义了关联表post_tag的结构
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags = new HashSet<>();

    // 一对多关系：一篇文章可以有多条评论
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Comment> comments = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 创建时间

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新时间

    @Column(name = "published_at")
    private LocalDateTime publishedAt; // 发布时间

    /**
     * 文章状态枚举
     */
    public enum PostStatus {
        PUBLISHED, // 已发布
        DRAFT, // 草稿
        PENDING // 待审核
    }

    /**
     * 添加标签
     * 
     * @param tag 要添加的标签
     */
    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getPostsInternal().add(this);
    }

    /**
     * 移除标签
     * 
     * @param tag 要移除的标签
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getPostsInternal().remove(this);
    }

    /**
     * 添加评论
     * 
     * @param comment 要添加的评论
     */
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPostInternal(this);
        this.commentCount++; // 更新评论计数
    }

    /**
     * 移除评论
     * 
     * @param comment 要移除的评论
     */
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPostInternal(null);
        if (this.commentCount > 0) {
            this.commentCount--; // 更新评论计数
        }
    }

    /**
     * 增加浏览量
     */
    public void incrementViewCount() {
        this.viewCount++;
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
     * 增加收藏数
     */
    public void incrementFavoriteCount() {
        this.favoriteCount++;
    }

    /**
     * 减少收藏数
     */
    public void decrementFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }
    }

    /**
     * 发布文章
     */
    public void publish() {
        this.status = PostStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    /**
     * 设置分类（内部方法，供Category类使用）
     * 
     * @param category 分类对象
     */
    public void setCategoryInternal(Category category) {
        this.category = category;
    }
} 