package org.classreviewsite.handler.exception;

public class LectureNotFoundException extends RuntimeException{
    public LectureNotFoundException(String message){
        super(message);
    }
}
