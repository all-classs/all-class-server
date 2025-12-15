package org.classreviewsite.review.service;

import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.springframework.transaction.annotation.Transactional;

public class ReviewDeletor {

    @Transactional
    public void delete(DeleteReviewRequest request){
        User user = userService.findUser(request.getUserNumber());

        ClassReview deletedClassReview = reviewDataService.findByReviewIdAndUserNumber(request.getPostId(), user);

        likeStatusCheckor.deleteAllByClassReview(deletedClassReview);

        deletedClassReview.getLecId().removeStarRating(deletedClassReview.getStarLating());

        reviewDataService.deleteById(request.getPostId());


    }
