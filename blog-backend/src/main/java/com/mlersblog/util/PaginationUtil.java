package com.mlersblog.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页工具类
 * 
 * 用于创建统一的分页响应格式
 */
public class PaginationUtil {
    
    /**
     * 创建分页响应
     * 
     * @param content 内容
     * @param currentPage 当前页码
     * @param totalItems 总项目数
     * @param totalPages 总页数
     * @return 包含分页信息的响应Map
     */
    public static Map<String, Object> createResponse(Object content, int currentPage, long totalItems, int totalPages) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        response.put("currentPage", currentPage);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);
        return response;
    }
} 