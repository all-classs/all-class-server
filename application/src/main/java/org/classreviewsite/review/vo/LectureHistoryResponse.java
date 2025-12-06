package org.classreviewsite.review.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.user.User;

@Getter
@AllArgsConstructor
public class LectureHistoryResponse {
    private Lecture lecture;
    private User student;

    public static LectureHistoryResponse of(Lecture lecture, User student) {
        return new LectureHistoryResponse(
                lecture,
                student
        );
    }

}
