package com.rootydev.ProductAnalytics.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private int code;
    private T data;
    private String subCode;
    private Object errors;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("Success", 200, data, null, null);
    }

    public static <T> ApiResponse<T> error(String message, int code, String subCode, Object errors) {
        return new ApiResponse<>(message, code, null, subCode, errors);
    }

    public static <T> ApiResponse<T> error(String message, int code, Object errors) {
        return new ApiResponse<>(message, code, null, null, errors);
    }
}
