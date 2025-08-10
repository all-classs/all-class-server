package org.classreviewsite.handler.exception;

public class ClassNotFoundException extends RuntimeException{
    public ClassNotFoundException(String message){
        super(message);
    }
}
