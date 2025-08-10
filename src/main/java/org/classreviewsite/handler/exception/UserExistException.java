package org.classreviewsite.handler.exception;

public class UserExistException extends RuntimeException{

    public UserExistException(String message){
        super(message);
    }

}
