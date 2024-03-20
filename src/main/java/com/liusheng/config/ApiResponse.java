package com.liusheng.config;

import lombok.Data;

import java.util.HashMap;

/**
 * @author liusheng
 * @date 2024/3/209:56
 * @desc TODO
 */
@Data
public final class ApiResponse {
    private Integer code;
    private String errorMsg;
    private Object data;

    private ApiResponse(int code, String errorMsg, Object data) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public static ApiResponse ok() {
        return new ApiResponse(0, "", new HashMap<>());
    }

    public static ApiResponse ok(Object data) {
        return new ApiResponse(0, "", data);
    }

    public static ApiResponse error(String errorMsg) {
        return new ApiResponse(0, errorMsg, new HashMap<>());
    }
}
