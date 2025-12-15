package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.lecture.Enrollment;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.handler.exception.NoPermissionReviewException;
import org.classreviewsite.lecture.service.EnrollmentDataService;
import org.classreviewsite.review.vo.LectureHistoryResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureHistoryValidator {

    private final EnrollmentDataService lectureHistoryService;

    public LectureHistoryResponse validate(int userNumber, String lectureName) {
        Enrollment reviewHistory = lectureHistoryService.findByUserNumber(userNumber, lectureName)
                .orElseThrow(
                        () -> new NoPermissionReviewException("내역이 존재하지 않습니다.")
                );

        /**
         * 조금 더 내부 로직으로 못넣나? Enrollment 에서 쪼개서 전달해주는게 맞나?
         */
        Lecture lecture = reviewHistory.getLecture();
        User student = reviewHistory.getUserNumber();

        return LectureHistoryResponse.of(lecture, student);
    }
}
