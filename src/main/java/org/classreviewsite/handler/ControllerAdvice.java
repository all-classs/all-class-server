package org.classreviewsite.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.handler.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.lang.ClassNotFoundException;
import java.util.NoSuchElementException;


@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @Data
    @AllArgsConstructor
    class Result<T>{
        private int status;
        private T data;
        private String message;
    }


    @ExceptionHandler(value = NoSuchElementException.class)
    protected Result NoSuchElementExceptionHandler(NoSuchElementException exception){
        return new Result(401, null, exception.getMessage());
    }


    @ExceptionHandler(value = UserNotFoundException.class)
    protected Result UserNotFoundException(UserNotFoundException exception){
        return new Result(401, null, exception.getMessage());
    }

    @ExceptionHandler(value = UserExistException.class)
    protected Result UserExistException(UserExistException exception){
        return new Result(204, null, exception.getMessage());
    }

    @ExceptionHandler(value = UserNumberLimitException.class)
    protected Result UserNumberLimitException(UserNumberLimitException exception){
        return new Result(403, null, exception.getMessage());
    }

    @ExceptionHandler(value = LectureNotFoundException.class)
    protected Result LectureNotFoundException(LectureNotFoundException exception){
        return new Result(401, null, exception.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected Result IllegalArgumentException(IllegalArgumentException exception){
        return new Result(403, null, exception.getMessage());
    }

    @ExceptionHandler(value = ReviewNotFoundException.class)
    protected Result ReviewNotFoundException(ReviewNotFoundException exception){
        return new Result(202, null, exception.getMessage());
    }


    @ExceptionHandler(value = AlreadyWritePostException.class)
    protected Result AlreadyWritePostException(AlreadyWritePostException exception){
        return new Result(401, null, exception.getMessage());
    }

    @ExceptionHandler(value = AlreadyLikeException.class)
    protected Result AlreadyLikeException(AlreadyLikeException exception){
        return new Result(202, null, exception.getMessage());
    }

    @ExceptionHandler(value = ClassNotFoundException.class)
    protected Result ClassNotFoundException(ClassNotFoundException exception){
        return new Result(202, null, exception.getMessage());
    }

    @ExceptionHandler(value = SecurityException.class)
    protected Result SecurityException(SecurityException exception){
        return new Result(401, null, exception.getMessage());
    }


    @ExceptionHandler(value = JwtNotValidException.class)
    protected Result JwtNotValidException(JwtNotValidException exception){
        return new Result(401, null, exception.getMessage());
    }

    @ExceptionHandler(value = UpdateFailedException.class)
    protected Result UpdateFailedException(UpdateFailedException exception){
        return new Result(500, null, exception.getMessage());
    }

    @ExceptionHandler(value = NoPermissionReviewException.class)
    protected Result NoPermissionReviewException(NoPermissionReviewException exception){
        return new Result(403, null, "수강하지 않은 강의입니다.");
    }


}
