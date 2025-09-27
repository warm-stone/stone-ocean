package com.example.stoneocean.entity;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/*
 * 通用返回结构
 * */

@Data
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int statusCode;
    private String message;
    private T data;


    private ApiResponse() {
    }

    private ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "成功");
    }

    public static <T> ApiResponse<T> success(T data, String message ) {
        return new ApiResponse<T>(200, message, data);
    }

    public static <T> ApiResponse<T> failed(String message) {
        return new ApiResponse<T>(500, message, null);
    }

}
