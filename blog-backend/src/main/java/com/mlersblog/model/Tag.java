package com.mlersblog.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 文章标签实体类
 * 
 * 对应数据库中的tag表，用于对文章进行标签分类
 * 与Post实体是多对多关系，一篇文章可以有多个标签，一个标签可以被多篇文章使用
 */
@Entity
@Table(name = "tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 标签ID

    @Column(unique = true, nullable = false, length = 50)
    private String name; // 标签名称

    @CreationTimestamp
    private LocalDateTime createdAt; // 创建时间
    
    // 多对多关系：一个标签可以被多篇文章使用，一篇文章可以有多个标签
    // 使用mappedBy指定由Post实体中的tags字段来维护关联关系
    // 避免ToString和EqualsAndHashCode造成的循环引用问题
    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Post> posts = new HashSet<>();
    
    /**
     * 获取文章集合（内部方法，供Post类使用）
     * 
     * @return 文章集合
     */
    public Set<Post> getPostsInternal() {
        return posts;
    }
    
    /**
     * 构造函数 - 仅带标签名
     * 
     * @param name 标签名称
     */
    public Tag(String name) {
        this.name = name;
    }
} 