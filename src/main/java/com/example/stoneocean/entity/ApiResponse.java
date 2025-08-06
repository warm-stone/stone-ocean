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


    public ApiResponse() {
    }

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

}
