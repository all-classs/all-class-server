package org.classreviewsite.handler.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoPermissionReviewException extends RuntimeException {
    public NoPermissionReviewException(String message) {
        super(message);
    }
}
