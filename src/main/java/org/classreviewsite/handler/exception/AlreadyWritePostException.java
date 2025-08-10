package org.classreviewsite.handler.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlreadyWritePostException extends RuntimeException{
    public AlreadyWritePostException(String message){
        super(message);
    }

}
