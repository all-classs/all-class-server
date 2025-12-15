package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewEditor {

    private final ReviewDataService reviewDataService;
    private final ReviewHistoryValidator reviewHistoryValidator;

    @Transactional
    public Long edit(final UpdateReviewRequest request){

        ClassReview post = reviewDataService.getReviewById(request.getPostId());

        validateCheckReviewAccess(request.getUserNumber().intValue(), post.getUserNumber().getUserNumber());

        post.update(request.getPostTitle(), request.getPostContent(), request.getStarLating());

        // 별점 수정
        Lecture lecture = post.getLecId();
        lecture.updateStarRating(post.getStarLating(), request.getStarLating());

        return post.getReviewId();
    }
}
