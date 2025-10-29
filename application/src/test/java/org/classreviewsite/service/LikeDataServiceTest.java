package org.classreviewsite.service;

import org.classreviewsite.handler.exception.AlreadyLikeException;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.review.infrastructure.LikesDataRepository;
import org.classreviewsite.review.service.LikeDataService;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("LikeDataService 테스트")
class LikeDataServiceTest {

    @InjectMocks
    LikeDataService likeDataService;

    @Mock
    LikesDataRepository likesDataRepository;

    private User createTestUser(int userNumber) {
        return User.builder()
                .userNumber(userNumber)
                .userName("홍길동")
                .department("소프트웨어학과")
                .nickname("hong123")
                .password("password")
                .authorities(Set.of())
                .build();
    }

    private Lecture createTestLecture(Long id, String name) {
        return new Lecture(id, name, StarRating.createRatingBuilder(),
                "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
    }

    @Nested
    @DisplayName("좋아요 중복 체크 테스트")
    class CheckTest {

        @Test
        @DisplayName("이미 좋아요를 누른 경우, AlreadyLikeException을 발생한다")
        void checkWhenAlreadyLiked() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");
            Likes existingLike = Likes.toEntity(classReview, user);

            given(likesDataRepository.findByUserAndClassReview(user, classReview))
                    .willReturn(Optional.of(existingLike));

            // when & then
            assertThatThrownBy(() -> likeDataService.check(user, classReview))
                    .isInstanceOf(AlreadyLikeException.class)
                    .hasMessage("좋아요가 취소 되었습니다.");
        }

        @Test
        @DisplayName("좋아요를 누르지 않은 경우, 예외가 발생하지 않는다")
        void checkWhenNotLiked() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            given(likesDataRepository.findByUserAndClassReview(user, classReview))
                    .willReturn(Optional.empty());

            // when & then
            likeDataService.check(user, classReview);
            verify(likesDataRepository).findByUserAndClassReview(user, classReview);
        }

        @Test
        @DisplayName("null 사용자로 체크 시, 예외가 발생하지 않는다")
        void checkWithNullUser() {
            // given
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            User user = createTestUser(20230857);
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            given(likesDataRepository.findByUserAndClassReview(null, classReview))
                    .willReturn(Optional.empty());

            // when & then
            likeDataService.check(null, classReview);
        }

        @Test
        @DisplayName("null 리뷰로 체크 시, 예외가 발생하지 않는다")
        void checkWithNullClassReview() {
            // given
            User user = createTestUser(20230857);

            given(likesDataRepository.findByUserAndClassReview(user, null))
                    .willReturn(Optional.empty());

            // when & then
            likeDataService.check(user, null);
        }
    }

    @Nested
    @DisplayName("좋아요 저장 테스트")
    class SaveTest {

        @Test
        @DisplayName("좋아요를 정상적으로 저장한다")
        void saveSuccess() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");
            Likes likes = Likes.toEntity(classReview, user);

            // when
            likeDataService.save(likes);

            // then
            verify(likesDataRepository).save(likes);
        }

        @Test
        @DisplayName("null 좋아요 저장 시, 예외가 발생한다")
        void saveWithNull() {
            // when & then
            assertThatThrownBy(() -> likeDataService.save(null))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("좋아요 삭제 테스트")
    class DeleteByClassReviewAndUserTest {

        @Test
        @DisplayName("리뷰와 사용자로 좋아요를 삭제한다")
        void deleteByClassReviewAndUserSuccess() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            // when
            likeDataService.deleteByClassReviewAndUser(classReview, user);

            // then
            verify(likesDataRepository).deleteByClassReviewAndUser(classReview, user);
        }

        @Test
        @DisplayName("null 리뷰로 삭제 시, IllegalArgumentException을 발생한다")
        void deleteByClassReviewAndUserWithNullReview() {
            // given
            User user = createTestUser(20230857);

            // when & then
            assertThatThrownBy(() -> likeDataService.deleteByClassReviewAndUser(null, user))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("수강후기 객체가 잘못 전달되었습니다.");

            verify(likesDataRepository, never()).deleteByClassReviewAndUser(any(), any());
        }

        @Test
        @DisplayName("null 사용자로 삭제 시, IllegalArgumentException을 발생한다")
        void deleteByClassReviewAndUserWithNullUser() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            // when & then
            assertThatThrownBy(() -> likeDataService.deleteByClassReviewAndUser(classReview, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("사용자 객체가 잘못 전달되었습니다.");

            verify(likesDataRepository, never()).deleteByClassReviewAndUser(any(), any());
        }

        @Test
        @DisplayName("모두 null로 삭제 시, IllegalArgumentException을 발생한다")
        void deleteByClassReviewAndUserWithBothNull() {
            // when & then
            assertThatThrownBy(() -> likeDataService.deleteByClassReviewAndUser(null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("수강후기 객체가 잘못 전달되었습니다.");

            verify(likesDataRepository, never()).deleteByClassReviewAndUser(any(), any());
        }
    }

    @Nested
    @DisplayName("리뷰의 모든 좋아요 삭제 테스트")
    class DeleteAllByClassReviewTest {

        @Test
        @DisplayName("리뷰의 모든 좋아요를 삭제한다")
        void deleteAllByClassReviewSuccess() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            // when
            likeDataService.deleteAllByClassReview(classReview);

            // then
            verify(likesDataRepository).deleteAllByClassReview(classReview);
        }

        @Test
        @DisplayName("null 리뷰로 모든 좋아요 삭제 시, IllegalArgumentException을 발생한다")
        void deleteAllByClassReviewWithNull() {
            // when & then
            assertThatThrownBy(() -> likeDataService.deleteAllByClassReview(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("수강후기 객체가 잘못 전달되었습니다.");

            verify(likesDataRepository, never()).deleteAllByClassReview(any());
        }
    }
}