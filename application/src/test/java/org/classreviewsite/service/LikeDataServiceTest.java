package org.classreviewsite.service;

import org.classreviewsite.handler.exception.AlreadyLikeException;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureType;
import org.classreviewsite.domain.lecture.StarRating;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.review.Likes;
import org.classreviewsite.domain.review.LikesDataRepository;
import org.classreviewsite.review.like.LikeDataService;
import org.classreviewsite.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LikeDataServiceTest {

    @InjectMocks
    LikeDataService likeDataService;
    @Mock LikesDataRepository likesDataRepository;

    @Nested
    @DisplayName("좋아요 중복 체크 테스트")
    class checkTest {
        
        @Test
        @DisplayName("처음 좋아요하는 경우, 예외가 발생하지 않는다")
        void shouldNotThrowExceptionWhenFirstTimeLiking() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택, 0L);
            
            ClassReview classReview = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            
            given(likesDataRepository.findByUserAndClassReview(user, classReview)).willReturn(null);
            
            // when & then
            var result = likeDataService.check(user, classReview);
            
            // No exception should be thrown
        }
        
        @Test
        @DisplayName("이미 좋아요한 경우, AlreadyLikeException을 발생한다")
        void shouldThrowExceptionWhenAlreadyLiked() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택, 0L);
            
            ClassReview classReview = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            Likes existingLike = Likes.toEntity(classReview, user);
            
            given(likesDataRepository.findByUserAndClassReview(user, classReview))
                    .willReturn(existingLike);
            
            // when & then
            assertThatThrownBy(() -> likeDataService.check(user, classReview))
                    .isInstanceOf(AlreadyLikeException.class)
                    .hasMessage("좋아요가 취소 되었습니다.");
        }
    }

    @Nested
    @DisplayName("좋아요 저장 테스트")
    class saveTest {
        
        @Test
        @DisplayName("좋아요 저장 시, 리포지토리의 save가 호출된다")
        void shouldCallRepositorySaveWhenSavingLike() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택, 0L);
            
            ClassReview classReview = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            Likes likes = Likes.toEntity(classReview, user);
            
            // when
            likeDataService.save(likes);
            
            // then
            verify(likesDataRepository).save(likes);
        }
    }

    @Nested
    @DisplayName("좋아요 삭제 테스트")
    class deleteByClassReviewAndUserTest {
        
        @Test
        @DisplayName("유효한 파라미터로 좋아요 삭제 시, 리포지토리의 delete가 호출된다")
        void shouldCallRepositoryDeleteWhenDeletingLike() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택, 0L);
            
            ClassReview classReview = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            
            // when
            likeDataService.deleteByClassReviewAndUser(classReview, user);
            
            // then
            verify(likesDataRepository).deleteByClassReviewAndUser(classReview, user);
        }
        
        @Test
        @DisplayName("null ClassReview로 좋아요 삭제 시, IllegalArgumentException을 발생한다")
        void shouldThrowExceptionWhenDeletingLikeWithNullClassReview() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            // when & then
            assertThatThrownBy(() -> likeDataService.deleteByClassReviewAndUser(null, user))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("수강후기 객체가 잘못 전달되었습니다.");
        }
        
        @Test
        @DisplayName("null User로 좋아요 삭제 시, IllegalArgumentException을 발생한다")
        void shouldThrowExceptionWhenDeletingLikeWithNullUser() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택, 0L);
            
            ClassReview classReview = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            
            // when & then
            assertThatThrownBy(() -> likeDataService.deleteByClassReviewAndUser(classReview, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("사용자 객체가 잘못 전달되었습니다.");
        }
    }

    @Nested
    @DisplayName("수강후기의 모든 좋아요 삭제 테스트")
    class deleteAllByClassReviewTest {
        
        @Test
        @DisplayName("유효한 수강후기로 모든 좋아요 삭제 시, 리포지토리의 deleteAll이 호출된다")
        void shouldCallRepositoryDeleteAllWhenDeletingAllLikes() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택, 0L);
            
            ClassReview classReview = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            
            // when
            likeDataService.deleteAllByClassReview(classReview);
            
            // then
            verify(likesDataRepository).deleteAllByClassReview(classReview);
        }
        
        @Test
        @DisplayName("null ClassReview로 모든 좋아요 삭제 시, IllegalArgumentException을 발생한다")
        void shouldThrowExceptionWhenDeletingAllLikesWithNull() {
            // when & then
            assertThatThrownBy(() -> likeDataService.deleteAllByClassReview(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("수강후기 객체가 잘못 전달되었습니다.");
        }
    }
}