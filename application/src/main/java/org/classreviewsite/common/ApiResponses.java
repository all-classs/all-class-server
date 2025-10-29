package org.classreviewsite.common;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiResponses<T>{

    private final ResultCode code;
    private final T data;
    private final String message;

    private ApiResponses(ResultCode code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponses<T> success(T data, String message) {
        return new ApiResponses<>(
                ResultCode.SUCCESS,
                data,
                message
        );
    }

    public static <T> ApiResponses<T> failure(ResultCode resultCode, String message) {
        return new ApiResponses<>(
                resultCode,
                null,
                message
        );
    }


}