package org.classreviewsite.domain;

import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.review.infrastructure.StarRating;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class LectureTest {

    Lecture lecture;

    @BeforeEach
    void setUp() {
        lecture = new Lecture(12345L, "강의명", StarRating.createRatingBuilder(), "학과명", "학교명", "교수명", LectureType.교양선택);
    }

    @Test
    @DisplayName("수강후기를 작성하면, 별점이 추가된다.")
    void createReview() {
        ClassReviewRequest classReviewRequest = ClassReviewRequest.of("", "", 20201234L, 4.0, "강의명");

        lecture.addReview(classReviewRequest.getStarLating());

        assertThat(lecture.getStarRating().getAverageRating()).isEqualTo(4.0);
        assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("수강후기를 삭제하면, 별점이 감소된다.")
    void removeReview() {
        lecture.addReview(4.0);
        assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(4.0);

        lecture.removeReview(4.0);
        assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(0.0);
    }
}
