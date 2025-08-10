package org.classreviewsite.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Result<T>{

    private int status;

    private T data;

    private String message;

}