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

        // TODO: ClassReview.create() 정적 팩토리 메서드 구현 필요 또는 생성자 사용
        // ClassReview classReview = ClassReview.create(lecture, user, reviewRequest.getStarLating(), reviewRequest.getPostContent(), reviewRequest.getPostTitle());
        // classReviewDataRepository.save(classReview);
    }
}
