package org.classreviewsite.review.vo;

import org.classreviewsite.domain.review.Likes;
import org.classreviewsite.review.service.LikedStatus;

public class LikeResponse {
    private final Likes like;
    private final LikedStatus message;

    private LikeResponse(Likes like, LikedStatus message) {
        this.like = like;
        this.message = message;
    }

    public static LikeResponse of(Likes like, LikedStatus message) {
        return new LikeResponse(like, message);
    }
}
