package org.classreviewsite.handler.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtNotValidException extends RuntimeException{

    public JwtNotValidException(String message){
        super(message);
    }
}
