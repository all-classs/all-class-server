package org.classreviewsite.review.service;

import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.classreviewsite.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;

public class ReviewDeletor {

    private final UserService userService;
    private final ReviewDataService reviewDataService;
    private final LikeStatusCheckor likeStatusCheckor;

    public ReviewDeletor(UserService userService, ReviewDataService reviewDataService, LikeStatusCheckor likeStatusCheckor) {
        this.userService = userService;
        this.reviewDataService = reviewDataService;
        this.likeStatusCheckor = likeStatusCheckor;
    }

    @Transactional
    public void delete(DeleteReviewRequest request){
        User user = userService.findUser(request.getUserNumber());

        ClassReview deletedClassReview = reviewDataService.findByReviewIdAndUserNumber(request.getPostId(), user);

        // TODO: LikeStatusCheckor.deleteAllByClassReview 메서드 구현 필요
        // likeStatusCheckor.deleteAllByClassReview(deletedClassReview);

        deletedClassReview.getLecId().removeStarRating(deletedClassReview.getStarLating());

        reviewDataService.deleteById(request.getPostId());
    }
}
