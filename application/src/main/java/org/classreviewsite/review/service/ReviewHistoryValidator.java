package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.handler.exception.AlreadyWritePostException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewHistoryValidator {

    private final ReviewDataService reviewDataService;

    public void validateHistory(final User student, final Lecture lecture) {
        reviewDataService.getReviewByUserNumberAndLectureId(student, lecture)
                .ifPresent(m -> {
                    throw new AlreadyWritePostException("이미 작성한 강의입니다.");
                });
    }

    public void validateReviewAccess(final int userNumber, final int postUserNumber) {
        reviewDataService.
    }
}
