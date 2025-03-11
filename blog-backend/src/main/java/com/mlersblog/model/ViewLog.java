package com.mlersblog.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 浏览记录实体类
 * 
 * 对应数据库中的view_log表，记录文章的浏览信息
 * 包括浏览时间、IP地址、用户信息等
 */
@Entity
@Table(name = "view_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    // 多对一关系：多个浏览记录对应一篇文章
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post; // 浏览的文章
    
    @Column
    private String ip; // 访问者IP地址
    
    // 多对一关系：多个浏览记录对应一个用户（游客浏览时可为null）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user; // 浏览用户，可为null表示游客
    
    @Column(name = "user_agent")
    private String userAgent; // 浏览器用户代理
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 浏览时间
} 