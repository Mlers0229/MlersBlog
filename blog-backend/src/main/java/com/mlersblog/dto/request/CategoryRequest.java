package com.mlersblog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 分类请求
 * 
 * 用于创建或更新分类的请求数据
 */
@Data
public class CategoryRequest {
    
    @NotBlank(message = "分类名称不能为空")
    @Size(min = 2, max = 50, message = "分类名称长度必须在2-50个字符之间")
    private String name;
    
    @Size(max = 200, message = "分类描述不能超过200个字符")
    private String description;
} 