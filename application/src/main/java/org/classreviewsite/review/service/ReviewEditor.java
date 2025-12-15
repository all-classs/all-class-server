package org.classreviewsite.review.service;

import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.springframework.transaction.annotation.Transactional;

public class ReviewEditor {

    private final ReviewDataService reviewDataService;
    private final ReviewHistoryValidator reviewHistoryValidator;

    public ReviewEditor(ReviewDataService reviewDataService, ReviewHistoryValidator reviewHistoryValidator) {
        this.reviewDataService = reviewDataService;
        this.reviewHistoryValidator = reviewHistoryValidator;
    }

    @Transactional
    public Long edit(final UpdateReviewRequest request){

        ClassReview post = reviewDataService.getReviewById(request.getPostId());

        // TODO: validateCheckReviewAccess 메서드 구현 필요
        // validateCheckReviewAccess(request.getUserNumber().intValue(), post.getUserNumber().getUserNumber());

        post.update(request.getPostTitle(), request.getPostContent(), request.getStarLating());

        // 별점 수정
        Lecture lecture = post.getLecId();
        lecture.updateStarRating(post.getStarLating(), request.getStarLating());

        return post.getReviewId();
    }
}
