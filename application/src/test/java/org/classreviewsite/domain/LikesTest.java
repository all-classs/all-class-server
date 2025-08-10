package org.classreviewsite.domain;

import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LikesTest {

    @Nested
    @DisplayName("Likes 엔티티 생성 테스트")
    class createLikesTest {
        
        @Test
        @DisplayName("정적 팩토리 메서드로 좋아요를 생성한다")
        void createLikesWithToEntity() {
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
            
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, 0, "좋은 강의", "추천");
            
            // when
            Likes likes = Likes.toEntity(classReview, user);
            
            // then
            assertThat(likes).isNotNull();
            assertThat(likes.getClassReview()).isEqualTo(classReview);
            assertThat(likes.getUser()).isEqualTo(user);
            assertThat(likes.getLikeId()).isNull(); // ID는 영속화 시에 생성됨
        }
        
        @Test
        @DisplayName("빌더를 사용하여 좋아요를 생성한다")
        void createLikesWithBuilder() {
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
            
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, 0, "좋은 강의", "추천");
            
            // when
            Likes likes = Likes.builder()
                    .classReview(classReview)
                    .user(user)
                    .build();
            
            // then
            assertThat(likes).isNotNull();
            assertThat(likes.getClassReview()).isEqualTo(classReview);
            assertThat(likes.getUser()).isEqualTo(user);
        }
        
        @Test
        @DisplayName("모든 인자를 포함한 생성자로 좋아요를 생성한다")
        void createLikesWithAllArgsConstructor() {
            // given
            Long likeId = 1L;
            
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
            
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, 0, "좋은 강의", "추천");
            
            // when
            Likes likes = new Likes(likeId, classReview, user);
            
            // then
            assertThat(likes).isNotNull();
            assertThat(likes.getLikeId()).isEqualTo(likeId);
            assertThat(likes.getClassReview()).isEqualTo(classReview);
            assertThat(likes.getUser()).isEqualTo(user);
        }
        
        @Test
        @DisplayName("기본 생성자로 좋아요를 생성한다")
        void createLikesWithNoArgsConstructor() {
            // when
            Likes likes = new Likes();
            
            // then
            assertThat(likes).isNotNull();
            assertThat(likes.getLikeId()).isNull();
            assertThat(likes.getClassReview()).isNull();
            assertThat(likes.getUser()).isNull();
        }
        
        @Test
        @DisplayName("다른 사용자의 수강후기에 좋아요를 생성한다")
        void createLikesForOtherUsersReview() {
            // given
            User reviewAuthor = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            User liker = User.builder()
                    .userNumber(20230858)
                    .userName("김철수")
                    .department("소프트웨어학과")
                    .nickname("kim456")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            ClassReview classReview = ClassReview.create(lecture, reviewAuthor, 4.5, 0, "좋은 강의", "추천");
            
            // when
            Likes likes = Likes.toEntity(classReview, liker);
            
            // then
            assertThat(likes.getClassReview().getUserNumber()).isEqualTo(reviewAuthor);
            assertThat(likes.getUser()).isEqualTo(liker);
            assertThat(likes.getUser()).isNotEqualTo(reviewAuthor);
        }
        
        @Test
        @DisplayName("여러 강의 타입의 수강후기에 좋아요를 생성한다")
        void createLikesForVariousLectureTypes() {
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
                
                ClassReview classReview = ClassReview.create(lecture, user, 4.0 + i * 0.5, 0, 
                                                           "내용" + i, "제목" + i);
                
                // when
                Likes likes = Likes.toEntity(classReview, user);
                
                // then
                assertThat(likes.getClassReview().getLecId().getLectureType()).isEqualTo(types[i]);
                assertThat(likes.getClassReview().getLecId().getLectureName()).isEqualTo(lectureNames[i]);
            }
        }
        
        @Test
        @DisplayName("높은 평점의 수강후기에 좋아요를 생성한다")
        void createLikesForHighRatedReview() {
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
            
            ClassReview highRatedReview = ClassReview.create(lecture, user, 5.0, 0, 
                                                           "정말 훌륭한 강의", "강력 추천");
            
            // when
            Likes likes = Likes.toEntity(highRatedReview, user);
            
            // then
            assertThat(likes.getClassReview().getStarLating()).isEqualTo(5.0);
            assertThat(likes.getClassReview().getPostTitle()).isEqualTo("강력 추천");
        }
        
        @Test
        @DisplayName("낮은 평점의 수강후기에도 좋아요를 생성할 수 있다")
        void createLikesForLowRatedReview() {
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
            
            ClassReview lowRatedReview = ClassReview.create(lecture, user, 1.0, 0, 
                                                          "솔직한 리뷰", "개선 필요");
            
            // when
            Likes likes = Likes.toEntity(lowRatedReview, user);
            
            // then
            assertThat(likes.getClassReview().getStarLating()).isEqualTo(1.0);
            assertThat(likes.getClassReview().getPostTitle()).isEqualTo("개선 필요");
            assertThat(likes.getUser()).isEqualTo(user);
        }
    }
}