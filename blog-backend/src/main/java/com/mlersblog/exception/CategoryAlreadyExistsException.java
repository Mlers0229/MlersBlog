package com.mlersblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 分类已存在异常
 * 
 * 当尝试创建一个已存在名称的分类时抛出此异常
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 使用指定的详细消息构造一个新的CategoryAlreadyExistsException
     * 
     * @param message 详细消息
     */
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细消息和原因构造一个新的CategoryAlreadyExistsException
     * 
     * @param message 详细消息
     * @param cause 原因
     */
    public CategoryAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 