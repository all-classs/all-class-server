package org.classreviewsite.dto;

import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassReviewRequestTest {

    @Nested
    @DisplayName("ClassReviewRequest 생성 테스트")
    class createClassReviewRequestTest {
        
        @Test
        @DisplayName("모든 필드를 포함하여 요청 객체를 생성한다")
        void createWithAllFields() {
            // given
            String lectureName = "자바프로그래밍";
            Long userNumber = 20230857L;
            Double starRating = 4.5;
            String postTitle = "좋은 강의";
            String postContent = "정말 유익한 강의였습니다";
            
            // when
            ClassReviewRequest request = new ClassReviewRequest(
                    lectureName, userNumber, starRating, postTitle, postContent
            );
            
            // then
            assertThat(request.getLectureName()).isEqualTo(lectureName);
            assertThat(request.getUserNumber()).isEqualTo(userNumber);
            assertThat(request.getStarLating()).isEqualTo(starRating);
            assertThat(request.getPostTitle()).isEqualTo(postTitle);
            assertThat(request.getPostContent()).isEqualTo(postContent);
        }
        
        @Test
        @DisplayName("빌더를 사용하여 요청 객체를 생성한다")
        void createWithBuilder() {
            // given & when
            ClassReviewRequest request = ClassReviewRequest.builder()
                    .lectureName("데이터베이스")
                    .userNumber(20230858L)
                    .starLating(5.0)
                    .postTitle("완벽한 강의")
                    .postContent("모든 내용이 완벽했습니다")
                    .build();
            
            // then
            assertThat(request.getLectureName()).isEqualTo("데이터베이스");
            assertThat(request.getUserNumber()).isEqualTo(20230858L);
            assertThat(request.getStarLating()).isEqualTo(5.0);
            assertThat(request.getPostTitle()).isEqualTo("완벽한 강의");
            assertThat(request.getPostContent()).isEqualTo("모든 내용이 완벽했습니다");
        }
        
        @Test
        @DisplayName("기본 생성자를 사용하여 빈 요청 객체를 생성한다")
        void createWithNoArgsConstructor() {
            // when
            ClassReviewRequest request = new ClassReviewRequest();
            
            // then
            assertThat(request.getLectureName()).isNull();
            assertThat(request.getUserNumber()).isNull();
            assertThat(request.getStarLating()).isNull();
            assertThat(request.getPostTitle()).isNull();
            assertThat(request.getPostContent()).isNull();
        }
        
        @Test
        @DisplayName("정적 팩토리 메서드 of()를 사용하여 요청 객체를 생성한다")
        void createWithOfMethod() {
            // given
            String postTitle = "훌륭한 강의";
            String postContent = "많은 것을 배웠습니다";
            Long userNumber = 20230859L;
            Double starRating = 4.8;
            String lectureName = "알고리즘";
            
            // when
            ClassReviewRequest request = ClassReviewRequest.of(
                    postTitle, postContent, userNumber, starRating, lectureName
            );
            
            // then
            assertThat(request.getPostTitle()).isEqualTo(postTitle);
            assertThat(request.getPostContent()).isEqualTo(postContent);
            assertThat(request.getUserNumber()).isEqualTo(userNumber);
            assertThat(request.getStarLating()).isEqualTo(starRating);
            assertThat(request.getLectureName()).isEqualTo(lectureName);
        }
        
        @Test
        @DisplayName("ClassReview 엔티티로부터 요청 객체를 생성한다")
        void createFromClassReview() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            ClassReview classReview = ClassReview.create(
                    lecture, user, 4.0, 5, "유익한 강의", "많이 배웠습니다"
            );
            
            // when
            ClassReviewRequest request = ClassReviewRequest.from(classReview);
            
            // then
            assertThat(request.getLectureName()).isEqualTo("자바프로그래밍");
            assertThat(request.getUserNumber()).isEqualTo(20230857L);
            assertThat(request.getStarLating()).isEqualTo(4.0);
            assertThat(request.getPostContent()).isEqualTo("유익한 강의");
            assertThat(request.getPostTitle()).isEqualTo("많이 배웠습니다");
        }
        
        @Test
        @DisplayName("최고 평점으로 요청 객체를 생성한다")
        void createWithMaxRating() {
            // given & when
            ClassReviewRequest request = ClassReviewRequest.of(
                    "최고의 강의", "완벽합니다", 20230857L, 5.0, "자바프로그래밍"
            );
            
            // then
            assertThat(request.getStarLating()).isEqualTo(5.0);
        }
        
        @Test
        @DisplayName("최저 평점으로 요청 객체를 생성한다")
        void createWithMinRating() {
            // given & when
            ClassReviewRequest request = ClassReviewRequest.of(
                    "아쉬운 강의", "개선이 필요합니다", 20230857L, 1.0, "자바프로그래밍"
            );
            
            // then
            assertThat(request.getStarLating()).isEqualTo(1.0);
        }
        
        @Test
        @DisplayName("긴 내용으로 요청 객체를 생성한다")
        void createWithLongContent() {
            // given
            String longContent = "이 강의는 정말로 훌륭했습니다. 교수님의 설명이 매우 명확하고 이해하기 쉬웠으며, " +
                    "실습 예제들도 실무에 바로 적용할 수 있는 내용들이었습니다. 특히 프로젝트 과제를 통해 " +
                    "배운 내용을 직접 구현해볼 수 있어서 더욱 의미있었습니다.";
            
            // when
            ClassReviewRequest request = ClassReviewRequest.of(
                    "상세한 후기", longContent, 20230857L, 4.5, "자바프로그래밍"
            );
            
            // then
            assertThat(request.getPostContent()).isEqualTo(longContent);
            assertThat(request.getPostContent().length()).isGreaterThan(100);
        }
        
        @Test
        @DisplayName("다양한 강의 타입의 강의에 대한 요청 객체를 생성한다")
        void createForVariousLectureTypes() {
            // given
            String[] lectureNames = {"자바프로그래밍", "영어회화", "캡스톤디자인"};
            
            for (String lectureName : lectureNames) {
                // when
                ClassReviewRequest request = ClassReviewRequest.of(
                        "좋은 강의", "유익했습니다", 20230857L, 4.0, lectureName
                );
                
                // then
                assertThat(request.getLectureName()).isEqualTo(lectureName);
            }
        }
        
        @Test
        @DisplayName("8자리 학번으로 요청 객체를 생성한다")
        void createWithValidStudentNumber() {
            // given & when
            ClassReviewRequest request = ClassReviewRequest.of(
                    "좋은 강의", "유익했습니다", 20240841L, 4.0, "자바프로그래밍"
            );
            
            // then
            assertThat(request.getUserNumber()).isEqualTo(20240841L);
            assertThat(String.valueOf(request.getUserNumber())).hasSize(8);
        }
    }
}