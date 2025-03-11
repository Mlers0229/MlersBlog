package com.mlersblog.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章分类实体类
 * 
 * 对应数据库中的category表，用于对文章进行分类管理
 * 如技术、设计、生活等分类
 */
@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 分类ID

    @Column(unique = true, nullable = false, length = 50)
    private String name; // 分类名称，如技术、设计、生活等

    @Column
    private String description; // 分类描述

    @CreationTimestamp
    private LocalDateTime createdAt; // 创建时间

    // 一对多关系：一个分类可以包含多篇文章
    @OneToMany(mappedBy = "category")
    private Set<Post> posts = new HashSet<>();
    
    /**
     * 添加文章到分类
     * 
     * @param post 要添加的文章
     */
    public void addPost(Post post) {
        posts.add(post);
        post.setCategoryInternal(this);
    }
    
    /**
     * 从分类中移除文章
     * 
     * @param post 要移除的文章
     */
    public void removePost(Post post) {
        posts.remove(post);
        post.setCategoryInternal(null);
    }
} 