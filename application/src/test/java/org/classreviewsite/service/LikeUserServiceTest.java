package org.classreviewsite.service;

import org.classreviewsite.handler.exception.AlreadyLikeException;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.review.service.LikeDataService;
import org.classreviewsite.review.service.LikeUserService;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LikeUserService 테스트")
class LikeUserServiceTest {

    @InjectMocks
    LikeUserService likeUserService;

    @Mock
    LikeDataService likeDataService;

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
    @DisplayName("좋아요 상태 확인 및 토글 테스트")
    class IsLikedTest {

        @Test
        @DisplayName("좋아요를 처음 누르는 경우, ALREADY_LIKE 상태를 반환한다")
        void isLikedFirstTime() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            doNothing().when(likeDataService).check(user, classReview);

            // when
            LikeUserService.LikeStatus result = likeUserService.isLiked(user, classReview);

            // then
            assertThat(result).isEqualTo(LikeUserService.LikeStatus.ALREADY_LIKE);
            verify(likeDataService).check(user, classReview);
            verify(likeDataService).save(any(Likes.class));
            verify(likeDataService, never()).deleteByClassReviewAndUser(any(), any());
        }

        @Test
        @DisplayName("이미 좋아요를 누른 경우, POSSIBLE_LIKE 상태를 반환한다")
        void isLikedSecondTime() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");
            classReview.like(); // 좋아요 1 증가

            doThrow(new AlreadyLikeException("좋아요가 취소 되었습니다."))
                    .when(likeDataService).check(user, classReview);

            // when
            LikeUserService.LikeStatus result = likeUserService.isLiked(user, classReview);

            // then
            assertThat(result).isEqualTo(LikeUserService.LikeStatus.POSSIBLE_LIKE);
            verify(likeDataService).check(user, classReview);
            verify(likeDataService).deleteByClassReviewAndUser(classReview, user);
            verify(likeDataService, never()).save(any());
        }

        @Test
        @DisplayName("좋아요 추가 시, 리뷰의 좋아요 수가 증가한다")
        void isLikedIncreasesLikeCount() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");
            int initialLikes = classReview.getLikes();

            doNothing().when(likeDataService).check(user, classReview);

            // when
            likeUserService.isLiked(user, classReview);

            // then
            assertThat(classReview.getLikes()).isEqualTo(initialLikes + 1);
        }

        @Test
        @DisplayName("좋아요 취소 시, 리뷰의 좋아요 수가 감소한다")
        void isLikedDecreasesLikeCount() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");
            classReview.like(); // 좋아요 1로 설정
            int initialLikes = classReview.getLikes();

            doThrow(new AlreadyLikeException("좋아요가 취소 되었습니다."))
                    .when(likeDataService).check(user, classReview);

            // when
            likeUserService.isLiked(user, classReview);

            // then
            assertThat(classReview.getLikes()).isEqualTo(initialLikes - 1);
        }

        @Test
        @DisplayName("여러 사용자가 좋아요를 누를 수 있다")
        void multipleUsersCanLike() {
            // given
            User user1 = createTestUser(20230857);
            User user2 = createTestUser(20230858);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview classReview = ClassReview.create(lecture, user1, 4.5, "좋은 강의", "추천합니다");

            doNothing().when(likeDataService).check(any(User.class), eq(classReview));

            // when
            LikeUserService.LikeStatus result1 = likeUserService.isLiked(user1, classReview);
            LikeUserService.LikeStatus result2 = likeUserService.isLiked(user2, classReview);

            // then
            assertThat(result1).isEqualTo(LikeUserService.LikeStatus.ALREADY_LIKE);
            assertThat(result2).isEqualTo(LikeUserService.LikeStatus.ALREADY_LIKE);
            assertThat(classReview.getLikes()).isEqualTo(2);
            verify(likeDataService, times(2)).save(any(Likes.class));
        }
    }
}