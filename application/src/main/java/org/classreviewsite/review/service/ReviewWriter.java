package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.review.ClassReviewDataRepository;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewWriter {

    private final ClassReviewDataRepository classReviewDataRepository;

    public void write(Lecture lecture, User user, ClassReviewRequest reviewRequest) {

        ClassReview classReview = ClassReview.create(lecture, user, reviewRequest.getStarLating(), reviewRequest.getPostContent(), reviewRequest.getPostTitle());

        classReviewDataRepository.save(classReview);
    }
}
