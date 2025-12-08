package org.classreviewsite.domain;

import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureType;
import org.classreviewsite.domain.lecture.StarRating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class LectureTest {

    @Nested
    @DisplayName("Lecture 비즈니스 로직 테스트")
    class lectureBusinessLogicTest {
        
        @Test
        @DisplayName("별점 수정 시 기존 별점을 제거하고 새 별점을 추가한다")
        void shouldUpdateStarRatingWhenModifyingReview() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수, 0L);
            lecture.addStarRating(4.0);
            lecture.addStarRating(5.0);
            // 현재: 총 9.0, 평균 4.5
            
            // when - 첫 번째 리뷰의 별점을 4.0에서 3.0으로 수정
            lecture.updateStarRating(4.0, 3.0);
            
            // then - 총 8.0, 평균 4.0
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(2L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(8.0);
            assertThat(lecture.getStarRating().getAverageRating()).isEqualTo(4.0);
        }
        
        @Test
        @DisplayName("교수명을 업데이트한다")
        void shouldUpdateProfessorName() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수, 0L);
            String newProfessorName = "이교수";
            
            // when
            lecture.updateProfessorName(newProfessorName);
            
            // then
            assertThat(lecture.getProfessor()).isEqualTo(newProfessorName);
        }
        
        @Test
        @DisplayName("디미터 법칙을 준수하여 평균 별점을 반환한다")
        void shouldReturnAverageRatingWithoutViolatingLawOfDemeter() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수, 0L);
            lecture.addStarRating(4.0);
            lecture.addStarRating(5.0);

            // when
            Double averageRating = lecture.getAverageRating();

            // then
            assertThat(averageRating).isEqualTo(4.5);
            assertThat(averageRating).isInstanceOf(Double.class);
        }
    }
}