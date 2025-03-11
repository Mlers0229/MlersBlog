package com.mlersblog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户信息请求
 * 
 * 包含更新用户信息所需的字段和验证规则
 */
@Data
public class UpdateUserRequest {
    
    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 20, message = "昵称长度必须在2-20个字符之间")
    private String nickname;
    
    private String avatar;
    
    @Size(max = 500, message = "个人简介不能超过500个字符")
    private String bio;
} 