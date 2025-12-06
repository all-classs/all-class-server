package org.classreviewsite.util;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiResponse<T>{

    private final ResultCode code;
    private final T data;
    private final String message;

    private ApiResponse(ResultCode code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(
                ResultCode.SUCCESS,
                data,
                message
        );
    }

    public static <T> ApiResponse<T> failure(ResultCode resultCode, String message) {
        return new ApiResponse<>(
                resultCode,
                null,
                message
        );
    }


}