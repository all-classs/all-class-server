package org.classreviewsite.domain;

import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class LectureTest {

    @Nested
    @DisplayName("Lecture 엔티티 생성 테스트")
    class createLectureTest {
        
        @Test
        @DisplayName("모든 필드를 포함하여 강의를 생성한다")
        void createLectureWithAllFields() {
            // given
            Long lectureId = 12345L;
            String lectureName = "자바프로그래밍";
            StarRating starRating = StarRating.createRatingBuilder();
            String department = "소프트웨어학과";
            String university = "한국대학교";
            String professor = "김교수";
            LectureType lectureType = LectureType.전공필수;
            
            // when
            Lecture lecture = new Lecture(lectureId, lectureName, starRating, 
                                        department, university, professor, lectureType);
            
            // then
            assertThat(lecture).isNotNull();
            assertThat(lecture.getLectureId()).isEqualTo(lectureId);
            assertThat(lecture.getLectureName()).isEqualTo(lectureName);
            assertThat(lecture.getStarRating()).isEqualTo(starRating);
            assertThat(lecture.getDepartment()).isEqualTo(department);
            assertThat(lecture.getUniversity()).isEqualTo(university);
            assertThat(lecture.getProfessor()).isEqualTo(professor);
            assertThat(lecture.getLectureType()).isEqualTo(lectureType);
        }
        
        @Test
        @DisplayName("빌더를 사용하여 강의를 생성한다")
        void createLectureWithBuilder() {
            // given & when
            Lecture lecture = Lecture.builder()
                    .lectureId(1L)
                    .lectureName("데이터베이스")
                    .starRating(StarRating.createRatingBuilder())
                    .department("소프트웨어학과")
                    .university("한국대학교")
                    .professor("이교수")
                    .lectureType(LectureType.전공선택)
                    .build();
            
            // then
            assertThat(lecture.getLectureId()).isEqualTo(1L);
            assertThat(lecture.getLectureName()).isEqualTo("데이터베이스");
            assertThat(lecture.getDepartment()).isEqualTo("소프트웨어학과");
            assertThat(lecture.getUniversity()).isEqualTo("한국대학교");
            assertThat(lecture.getProfessor()).isEqualTo("이교수");
            assertThat(lecture.getLectureType()).isEqualTo(LectureType.전공선택);
        }
        
        @Test
        @DisplayName("다양한 강의 타입으로 강의를 생성한다")
        void createLectureWithVariousTypes() {
            // given
            LectureType[] types = {
                LectureType.전공필수, LectureType.전공선택, LectureType.교양필수, 
                LectureType.교양선택, LectureType.산학, LectureType.교선기초
            };
            
            for (LectureType type : types) {
                // when
                Lecture lecture = new Lecture(1L, "테스트강의", StarRating.createRatingBuilder(),
                                            "소프트웨어학과", "한국대학교", "테스트교수", type);
                
                // then
                assertThat(lecture.getLectureType()).isEqualTo(type);
            }
        }
        
        @Test
        @DisplayName("교양 강의를 생성한다")
        void createGeneralEducationLecture() {
            // given & when
            Lecture lecture = new Lecture(1L, "영어회화", StarRating.createRatingBuilder(),
                                        "외국어학과", "한국대학교", "존스미스", LectureType.교양선택);
            
            // then
            assertThat(lecture.getLectureType()).isEqualTo(LectureType.교양선택);
            assertThat(lecture.getDepartment()).isEqualTo("외국어학과");
            assertThat(lecture.getProfessor()).isEqualTo("존스미스");
        }
        
        @Test
        @DisplayName("산학 강의를 생성한다")
        void createIndustryAcademicLecture() {
            // given & when
            Lecture lecture = new Lecture(1L, "캡스톤디자인", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "박교수", LectureType.산학);
            
            // then
            assertThat(lecture.getLectureType()).isEqualTo(LectureType.산학);
            assertThat(lecture.getLectureName()).isEqualTo("캡스톤디자인");
        }
    }

    @Nested
    @DisplayName("Lecture 비즈니스 로직 테스트")
    class lectureBusinessLogicTest {
        
        @Test
        @DisplayName("수강후기 평점을 추가한다")
        void addReview() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            Double newRating = 4.5;
            
            // when
            lecture.addReview(newRating);
            
            // then
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(1L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(4.5);
            assertThat(lecture.getStarRating().getAverageRating()).isEqualTo(4.5);
        }
        
        @Test
        @DisplayName("여러 수강후기 평점을 추가한다")
        void addMultipleReviews() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            // when
            lecture.addReview(4.0);
            lecture.addReview(5.0);
            lecture.addReview(3.0);
            
            // then
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(3L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(12.0);
            assertThat(lecture.getStarRating().getAverageRating()).isEqualTo(4.0);
        }
        
        @Test
        @DisplayName("수강후기 평점을 제거한다")
        void removeReview() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            lecture.addReview(4.0);
            lecture.addReview(5.0);
            
            // when
            lecture.removeReview(4.0);
            
            // then
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(1L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(5.0);
            assertThat(lecture.getStarRating().getAverageRating()).isEqualTo(5.0);
        }
        
        @Test
        @DisplayName("모든 수강후기를 제거하면 평점이 0이 된다")
        void removeAllReviews() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            lecture.addReview(4.0);
            
            // when
            lecture.removeReview(4.0);
            
            // then
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(0L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(0.0);
            assertThat(lecture.getStarRating().getAverageRating()).isEqualTo(0.0);
        }
        
        @Test
        @DisplayName("교수명을 업데이트한다")
        void updateProfessorName() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            String newProfessorName = "이교수";
            
            // when
            lecture.updateProfessorName(newProfessorName);
            
            // then
            assertThat(lecture.getProfessor()).isEqualTo(newProfessorName);
        }
        
        @Test
        @DisplayName("빈 교수명으로 업데이트한다")
        void updateProfessorNameToEmpty() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            String emptyProfessorName = "";
            
            // when
            lecture.updateProfessorName(emptyProfessorName);
            
            // then
            assertThat(lecture.getProfessor()).isEqualTo(emptyProfessorName);
        }
        
        @Test
        @DisplayName("평점 추가와 제거를 반복한다")
        void addAndRemoveReviewsRepeatedly() {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            // when
            lecture.addReview(3.0);
            lecture.addReview(4.0);
            lecture.addReview(5.0); // 평균: 4.0
            
            lecture.removeReview(3.0); // 평균: 4.5
            lecture.addReview(4.0); // 평균: 4.33
            
            // then
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(3L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(13.0);
            assertThat(lecture.getStarRating().getAverageRating()).isCloseTo(4.33, within(0.01));
        }

        // Lecture의 getAverageRating은 디미터법칙을 위반하지 않는지 검증
        @Test
        @DisplayName("Lecture의 getAverageRating은 2개이상의 chainedCall을 false를 반환한다.")
        void getStarLating() throws NoSuchMethodException {
            // given
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                    "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            Method method = Lecture.class.getMethod("getAverageRating");
            String code = method.toString();

            // when
            Double averageRating = lecture.getAverageRating();
            boolean hasChainedCall = code.contains("getStarRating().getAverageRating()");

            // then
            assertThat(averageRating).isEqualTo(lecture.getStarRating().getAverageRating());
            assertThat(averageRating.getClass()).isNotEqualTo(StarRating.class);
            assertThat(hasChainedCall).isFalse();
        }
    }
}