package org.classreviewsite.review.service;

public enum LikedStatus {
    ALREADY_LIKE("이미 좋아요한 상태"),
    POSSIBLE_LIKE("좋아요 가능한 상태");

    private final String message;

    LikedStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
