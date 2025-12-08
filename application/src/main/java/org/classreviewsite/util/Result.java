package org.classreviewsite.util;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Result<T>{

    private final ResultCode code;
    private final T data;
    private final String message;

    private Result(ResultCode code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(
                ResultCode.SUCCESS,
                data,
                message
        );
    }

    public static <T> Result<T> failure(ResultCode resultCode, String message) {
        return new Result<>(
                resultCode,
                null,
                message
        );
    }


}