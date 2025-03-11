package com.mlersblog.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 分类数据传输对象
 * 
 * 用于API响应中返回分类信息
 */
@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
} 