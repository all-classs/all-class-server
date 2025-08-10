package org.classreviewsite.dto;

import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.review.controller.data.Response.ReviewResponse;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.user.controller.data.response.UserResponse;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewResponseTest {

    @Nested
    @DisplayName("ReviewResponse 생성 테스트")
    class createReviewResponseTest {
        
        @Test
        @DisplayName("모든 필드를 포함하여 응답 객체를 생성한다")
        void createWithAllFields() {
            // given
            Long postId = 1L;
            String postTitle = "좋은 강의";
            String postContent = "정말 유익한 강의였습니다";
            Double starRating = 4.5;
            Integer likes = 10;
            String createDate = "2023-12-25";
            UserResponse userResponse = createMockUserResponse();
            
            // when
            ReviewResponse response = new ReviewResponse(
                    postId, postTitle, postContent, starRating, likes, createDate, userResponse
            );
            
            // then
            assertThat(response.getPostId()).isEqualTo(postId);
            assertThat(response.getPostTitle()).isEqualTo(postTitle);
            assertThat(response.getPostContent()).isEqualTo(postContent);
            assertThat(response.getStarLating()).isEqualTo(starRating);
            assertThat(response.getLikes()).isEqualTo(likes);
            assertThat(response.getCreateDate()).isEqualTo(createDate);
            assertThat(response.getUser()).isEqualTo(userResponse);
        }
        
        @Test
        @DisplayName("ClassReview 엔티티로부터 응답 객체를 생성한다")
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
                    lecture, user, 4.5, 8, "유익한 강의", "많이 배웠습니다"
            );
            
            // when
            ReviewResponse response = ReviewResponse.from(classReview);
            
            // then
            assertThat(response.getPostId()).isEqualTo(classReview.getReviewId());
            assertThat(response.getPostTitle()).isEqualTo("유익한 강의");
            assertThat(response.getPostContent()).isEqualTo("많이 배웠습니다");
            assertThat(response.getStarLating()).isEqualTo(4.5);
            assertThat(response.getLikes()).isEqualTo(8);
            assertThat(response.getUser()).isNotNull();
        }
        
        @Test
        @DisplayName("좋아요가 0인 리뷰 응답 객체를 생성한다")
        void createWithZeroLikes() {
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
                    lecture, user, 3.0, 0, "평범한 강의", "보통입니다"
            );
            
            // when
            ReviewResponse response = ReviewResponse.from(classReview);
            
            // then
            assertThat(response.getLikes()).isEqualTo(0);
        }
        
        @Test
        @DisplayName("높은 좋아요 수를 가진 리뷰 응답 객체를 생성한다")
        void createWithHighLikes() {
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
                    lecture, user, 5.0, 100, "최고의 강의", "강력 추천!"
            );
            
            // when
            ReviewResponse response = ReviewResponse.from(classReview);
            
            // then
            assertThat(response.getLikes()).isEqualTo(100);
            assertThat(response.getStarLating()).isEqualTo(5.0);
        }
        
        @Test
        @DisplayName("최고 평점의 리뷰 응답 객체를 생성한다")
        void createWithMaxRating() {
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
                    lecture, user, 5.0, 15, "완벽한 강의", "모든 면에서 완벽합니다"
            );
            
            // when
            ReviewResponse response = ReviewResponse.from(classReview);
            
            // then
            assertThat(response.getStarLating()).isEqualTo(5.0);
            assertThat(response.getPostTitle()).isEqualTo("완벽한 강의");
        }
        
        @Test
        @DisplayName("최저 평점의 리뷰 응답 객체를 생성한다")
        void createWithMinRating() {
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
                    lecture, user, 1.0, 2, "아쉬운 강의", "개선이 필요합니다"
            );
            
            // when
            ReviewResponse response = ReviewResponse.from(classReview);
            
            // then
            assertThat(response.getStarLating()).isEqualTo(1.0);
            assertThat(response.getPostTitle()).isEqualTo("아쉬운 강의");
        }
        
        @Test
        @DisplayName("긴 내용의 리뷰 응답 객체를 생성한다")
        void createWithLongContent() {
            // given
            String longContent = "이 강의는 정말로 훌륭했습니다. 교수님의 설명이 매우 명확하고 이해하기 쉬웠으며, " +
                    "실습 예제들도 실무에 바로 적용할 수 있는 내용들이었습니다. 특히 프로젝트 과제를 통해 " +
                    "배운 내용을 직접 구현해볼 수 있어서 더욱 의미있었습니다.";
            
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
                    lecture, user, 4.8, 25, "상세한 후기", longContent
            );
            
            // when
            ReviewResponse response = ReviewResponse.from(classReview);
            
            // then
            assertThat(response.getPostContent()).isEqualTo(longContent);
            assertThat(response.getPostContent().length()).isGreaterThan(100);
        }
        
        @Test
        @DisplayName("다양한 강의 타입의 리뷰 응답 객체를 생성한다")
        void createForVariousLectureTypes() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            LectureType[] types = {LectureType.전공필수, LectureType.교양선택, LectureType.산학};
            String[] lectureNames = {"자바프로그래밍", "영어회화", "캡스톤디자인"};
            
            for (int i = 0; i < types.length; i++) {
                Lecture lecture = new Lecture((long) (i + 1), lectureNames[i], StarRating.createRatingBuilder(),
                                            "소프트웨어학과", "한국대학교", "교수" + i, types[i]);
                
                ClassReview classReview = ClassReview.create(
                        lecture, user, 4.0, i * 5, "좋은 강의 " + i, "내용 " + i
                );
                
                // when
                ReviewResponse response = ReviewResponse.from(classReview);
                
                // then
                assertThat(response.getPostTitle()).isEqualTo("좋은 강의 " + i);
                assertThat(response.getPostContent()).isEqualTo("내용 " + i);
                assertThat(response.getLikes()).isEqualTo(i * 5);
            }
        }
        
        @Test
        @DisplayName("응답 객체의 날짜 형식이 올바르게 설정된다")
        void createWithCorrectDateFormat() {
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
                    lecture, user, 4.0, 5, "날짜 테스트", "날짜 형식 확인"
            );
            
            // when
            ReviewResponse response = ReviewResponse.from(classReview);
            
            // then
            assertThat(response.getCreateDate()).isNotNull();
            assertThat(response.getCreateDate()).matches("\\d{4}-\\d{1,2}-\\d{1,2}"); // YYYY-M-D 형식
        }
    }
    
    // 헬퍼 메서드
    private UserResponse createMockUserResponse() {
        // UserResponse 객체를 생성하는 헬퍼 메서드
        // 실제 구현은 UserResponse의 생성자나 팩토리 메서드에 따라 달라집니다
        return null; // 실제로는 적절한 객체를 반환해야 합니다
    }
}