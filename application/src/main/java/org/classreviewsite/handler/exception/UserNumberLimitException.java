package org.classreviewsite.handler.exception;

public class UserNumberLimitException extends RuntimeException{
    public UserNumberLimitException(String message){
        super(message);
    }
}
