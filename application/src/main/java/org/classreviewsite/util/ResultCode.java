package org.classreviewsite.util;

public enum ResultCode {

    SUCCESS("성공"),
    STUDENT_NOT_FOUND("학생이 존재하지 않습니다."),
    STUDENT_NUMBER_LIMIT("학번자수를 초과하였습니다."),
    LECTURE_NOT_FOUND("존재하지 않는 강의힙니다.");

    private final String message;

    ResultCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
