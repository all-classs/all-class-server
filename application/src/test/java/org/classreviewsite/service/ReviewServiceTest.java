package org.classreviewsite.service;

import org.classreviewsite.handler.exception.*;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureType;
import org.classreviewsite.domain.lecture.StarRating;
import org.classreviewsite.lecture.service.EnrollmentDataService;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.classreviewsite.review.controller.data.Response.ReviewMeResponse;
import org.classreviewsite.review.controller.data.Response.ReviewResponse;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.review.ClassReviewDataRepository;
import org.classreviewsite.domain.review.LikesDataRepository;
import org.classreviewsite.review.service.LikeStatusCheckor;
import org.classreviewsite.review.service.LikeStateGenerator;
import org.classreviewsite.review.service.LikedStatus;
import org.classreviewsite.review.service.ReviewDataService;
import org.classreviewsite.review.service.ReviewService;
import org.classreviewsite.review.service.LectureHistoryValidator;
import org.classreviewsite.review.service.ReviewHistoryValidator;
import org.classreviewsite.review.vo.LectureHistoryResponse;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks ReviewService reviewService;
    
    @Mock LectureHistoryValidator lectureHistoryValidator;
    @Mock ReviewHistoryValidator reviewHistoryValidator;
    @Mock ReviewDataService reviewDataService;
    @Mock UserService userService;
    @Mock
    LikeStatusCheckor likeStatusCheckor;
    @Mock
    LikeStateGenerator likeStateGenerator;
    
    // Legacy mocks for backward compatibility in some tests
    @Mock ClassReviewDataRepository classReviewDataRepository;
    @Mock LikesDataRepository likesDataRepository;
    @Mock LectureDataService lectureDataService;
    @Mock EnrollmentDataService enrollmentDataService;

    @Nested
    @DisplayName("수강후기 조회 테스트")
    class findByIdTest {
        
        @Test
        @DisplayName("존재하는 ID로 조회 시, 수강후기를 반환한다")
        void shouldReturnReviewWhenValidIdProvided() {
            // given
            Long reviewId = 1L;
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
            
            ClassReview expectedReview = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            
            given(reviewDataService.getReviewById(reviewId)).willReturn(expectedReview);
            
            // when
            ClassReview result = reviewDataService.getReviewById(reviewId);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result.getPostTitle()).isEqualTo("제목");
            assertThat(result.getPostContent()).isEqualTo("내용");
            assertThat(result.getStarLating()).isEqualTo(4.5);
        }
        
        @Test
        @DisplayName("존재하지 않는 ID로 조회 시, ReviewNotFoundException을 발생한다")
        void shouldThrowExceptionWhenReviewNotFound() {
            // given
            Long reviewId = 999L;
            given(reviewDataService.getReviewById(reviewId))
                    .willThrow(new ReviewNotFoundException("해당 수강후기가 존재하지 않습니다."));
            
            // when & then
            assertThatThrownBy(() -> reviewDataService.getReviewById(reviewId))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessage("해당 수강후기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("수강후기 전체 조회 테스트")
    class findAllTest {
        
        @Test
        @DisplayName("강의 ID로 전체 조회 시, 수강후기 목록을 반환한다")
        void shouldReturnReviewListWhenValidLectureIdProvided() {
            // given
            Long lectureId = 1L;
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
            
            ClassReview review1 = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목1")
                    .postContent("내용1")
                    .build();
            ClassReview review2 = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(3.0)
                    .likes(0)
                    .postTitle("제목2")
                    .postContent("내용2")
                    .build();
            
            List<ClassReview> reviews = Arrays.asList(review1, review2);
            
            given(reviewDataService.getAll(lectureId)).willReturn(reviews);
            
            // when
            List<ReviewResponse> result = reviewService.findAll(lectureId);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
        }
        
        @Test
        @DisplayName("강의 ID로 전체 조회 시 결과가 없으면, ReviewNotFoundException을 발생한다")
        void shouldThrowExceptionWhenNoReviewsExist() {
            // given
            Long lectureId = 1L;
            given(reviewDataService.getAll(lectureId)).willReturn(List.of());
            
            // when & then
            assertThatThrownBy(() -> reviewService.findAll(lectureId))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessage("수강 후기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("수강후기 작성 테스트")
    class writeTest {
        
        @Test
        @DisplayName("유효한 요청으로 수강후기 작성 시, 정상적으로 저장된다")
        void shouldSaveReviewWhenValidRequestProvided() {
            // given
            ClassReviewRequest request = new ClassReviewRequest(
                    "강의명", 20230857, 4.5, "제목", "내용"
            );
            
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
            
            LectureHistoryResponse response = LectureHistoryResponse.of(lecture, user);
            
            given(lectureHistoryValidator.validate(20230857, "강의명")).willReturn(response);
            doNothing().when(reviewHistoryValidator).validateHistory(user, lecture);
            doNothing().when(reviewDataService).save(any(ClassReview.class));
            
            // when
            reviewService.write(request);
            
            // then
            verify(lectureHistoryValidator).validate(20230857, "강의명");
            verify(reviewHistoryValidator).validateHistory(user, lecture);
            verify(reviewDataService).save(any(ClassReview.class));
        }
        
        @Test
        @DisplayName("이미 작성한 강의에 수강후기 작성 시, AlreadyWritePostException을 발생한다")
        void shouldThrowExceptionWhenReviewAlreadyExists() {
            // given
            ClassReviewRequest request = new ClassReviewRequest(
                    "강의명", 20230857 , 4.5, "제목", "내용"
            );
            
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
            
            LectureHistoryResponse response = LectureHistoryResponse.of(lecture, user);
            
            given(lectureHistoryValidator.validate(20230857, "강의명")).willReturn(response);
            doThrow(new AlreadyWritePostException("이미 작성한 강의입니다."))
                    .when(reviewHistoryValidator).validateHistory(user, lecture);
            
            // when & then
            assertThatThrownBy(() -> reviewService.write(request))
                    .isInstanceOf(AlreadyWritePostException.class)
                    .hasMessage("이미 작성한 강의입니다.");
        }
    }

    @Nested
    @DisplayName("수강후기 수정 테스트")
    class updateReviewPostTest {
        
        @Test
        @DisplayName("유효한 요청으로 수강후기 수정 시, 정상적으로 수정된다")
        void shouldUpdateReviewWhenValidRequestProvided() {
            // given
            UpdateReviewRequest request = new UpdateReviewRequest(
                    1L, "수정된 제목", "수정된 내용", 3.0, 20230857L
            );
            
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
            
            ClassReview review = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("원본제목")
                    .postContent("원본내용")
                    .build();
            
            given(reviewDataService.getReviewById(1L)).willReturn(review);
            
            // when
            Long result = reviewService.updateReviewPost(request);
            
            // then
            assertThat(result).isEqualTo(review.getReviewId());
        }
        
        @Test
        @DisplayName("다른 사용자의 수강후기 수정 시, InValidReviewAccessException을 발생한다")
        void shouldThrowExceptionWhenUnauthorizedUserTriesToUpdate() {
            // given
            UpdateReviewRequest request = new UpdateReviewRequest(
                    1L, "수정된 제목", "수정된 내용", 3.0, 99999999L
            );
            
            User originalUser = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택, 0L);
            
            ClassReview review = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(originalUser)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("원본제목")
                    .postContent("원본내용")
                    .build();
            
            given(reviewDataService.getReviewById(1L)).willReturn(review);
            
            // when & then
            assertThatThrownBy(() -> reviewService.updateReviewPost(request))
                    .isInstanceOf(InValidReviewAccessException.class)
                    .hasMessage("작성하지 않은 사용자의 요청입니다.");
        }
    }

    @Nested
    @DisplayName("수강후기 좋아요 테스트")
    class likeReviewTest {
        
        @Test
        @DisplayName("처음 좋아요 요청 시, 좋아요가 추가된다")
        void shouldAddLikeWhenFirstTimeRequested() {
            // given
            LikeRequest request = new LikeRequest(20230857, 1L);
            
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
            
            ClassReview review = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            
            given(userService.findUser(20230857)).willReturn(user);
            given(reviewDataService.getReviewById(1L)).willReturn(review);
            given(likeStateGenerator.isLiked(user, review)).willReturn(LikedStatus.POSSIBLE_LIKE);
            
            // when
            String result = reviewService.likeReview(request);
            
            // then
            assertThat(result).isEqualTo("좋아요가 추가되었습니다.");
        }
        
        @Test
        @DisplayName("이미 좋아요한 수강후기에 좋아요 요청 시, 좋아요가 취소된다")
        void shouldRemoveLikeWhenAlreadyLiked() {
            // given
            LikeRequest request = new LikeRequest(20230857, 1L);
            
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
            
            ClassReview review = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            
            given(userService.findUser(20230857)).willReturn(user);
            given(reviewDataService.getReviewById(1L)).willReturn(review);
            given(likeStateGenerator.isLiked(user, review)).willReturn(LikedStatus.ALREADY_LIKE);
            
            // when
            String result = reviewService.likeReview(request);
            
            // then
            assertThat(result).isEqualTo("좋아요가 취소되었습니다.");
        }
    }

    @Nested
    @DisplayName("내 수강후기 조회 테스트")
    class findMyReviewTest {
        
        @Test
        @DisplayName("사용자 번호로 내 수강후기 조회 시, 수강후기 목록을 반환한다")
        void shouldReturnMyReviewsWhenValidUserNumberProvided() {
            // given
            int userNumber = 20230857;
            User user = User.builder()
                    .userNumber(userNumber)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택, 0L);
            
            ClassReview review1 = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목1")
                    .postContent("내용1")
                    .build();
            ClassReview review2 = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(3.0)
                    .likes(0)
                    .postTitle("제목2")
                    .postContent("내용2")
                    .build();
            
            List<ClassReview> reviews = Arrays.asList(review1, review2);
            
            given(reviewDataService.getReviewsByUserNumber(userNumber)).willReturn(reviews);
            
            // when
            List<ReviewMeResponse> result = reviewService.findMyReview(userNumber);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
        }
        
        @Test
        @DisplayName("내 수강후기가 없을 때 조회 시, ReviewNotFoundException을 발생한다")
        void shouldThrowExceptionWhenUserHasNoReviews() {
            // given
            int userNumber = 20230857;
            given(reviewDataService.getReviewsByUserNumber(userNumber)).willReturn(List.of());
            
            // when & then
            assertThatThrownBy(() -> reviewService.findMyReview(userNumber))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessage("수강후기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("수강후기 삭제 테스트")
    class deleteReviewPostTest {
        
        @Test
        @DisplayName("유효한 요청으로 수강후기 삭제 시, 정상적으로 삭제된다")
        void shouldDeleteReviewWhenValidRequestProvided() {
            // given
            DeleteReviewRequest request = new DeleteReviewRequest(1L, 20230857);
            
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
            
            ClassReview review = ClassReview.builder()
                    .lecId(lecture)
                    .userNumber(user)
                    .starLating(4.5)
                    .likes(0)
                    .postTitle("제목")
                    .postContent("내용")
                    .build();
            
            given(userService.findUser(20230857)).willReturn(user);
            given(reviewDataService.findByReviewIdAndUserNumber(1L, user)).willReturn(review);
            doNothing().when(likeStatusCheckor).deleteAllByClassReview(review);
            doNothing().when(reviewDataService).deleteById(1L);
            
            // when
            reviewService.deleteReviewPost(request);
            
            // then
            verify(likeStatusCheckor).deleteAllByClassReview(review);
            verify(reviewDataService).deleteById(1L);
        }
    }

    @Transactional
    @Nested
    @DisplayName("별점 계산 테스트")
    class starRatingCalculationTest {
        
        @Test
        @DisplayName("두 사용자가 수강후기를 작성했을 때, 별점이 정확하게 계산된다")
        void shouldCalculateCorrectAverageRatingWhenTwoUsersAddReviews() {
            // given
            // 첫 번째 사용자
            User user1 = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            // 두 번째 사용자
            User user2 = User.builder()
                    .userNumber(20230858)
                    .userName("김철수")
                    .department("소프트웨어학과")
                    .nickname("kim123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            // 강의 (초기 별점 0.0, 리뷰 수 0)
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수, 0L);
            
            // 첫 번째 수강후기 요청 (별점 4.0)
            ClassReviewRequest request1 = new ClassReviewRequest(
                    "자바프로그래밍", 20230857, 4.0, "좋은 강의", "내용1"
            );
            
            // 두 번째 수강후기 요청 (별점 5.0)
            ClassReviewRequest request2 = new ClassReviewRequest(
                    "자바프로그래밍", 20230858, 5.0, "훌륭한 강의", "내용2"
            );
            
            // Mock 설정
            LectureHistoryResponse response1 = LectureHistoryResponse.of(lecture, user1);
            LectureHistoryResponse response2 = LectureHistoryResponse.of(lecture, user2);
            
            given(lectureHistoryValidator.validate(20230857, "자바프로그래밍")).willReturn(response1);
            given(lectureHistoryValidator.validate(20230858, "자바프로그래밍")).willReturn(response2);
            doNothing().when(reviewHistoryValidator).validateHistory(user1, lecture);
            doNothing().when(reviewHistoryValidator).validateHistory(user2, lecture);
            doNothing().when(reviewDataService).save(any(ClassReview.class));
            
            // when
            // 첫 번째 사용자가 수강후기 작성
            reviewService.write(request1);
            
            // then - 첫 번째 리뷰 후 별점 확인
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(1L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(4.0);
            assertThat(lecture.getStarRating().getAverageRating()).isEqualTo(4.0);
            
            // when
            // 두 번째 사용자가 수강후기 작성
            reviewService.write(request2);
            
            // then - 두 번째 리뷰 후 별점 확인
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(2L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(9.0);
            assertThat(lecture.getStarRating().getAverageRating()).isEqualTo(4.5);
            
            // verify
            verify(reviewDataService, times(2)).save(any(ClassReview.class));
            verify(lectureHistoryValidator, times(2)).validate(anyInt(), eq("자바프로그래밍"));
            verify(reviewHistoryValidator, times(2)).validateHistory(any(User.class), eq(lecture));
        }
        
        @Test
        @DisplayName("여러 사용자가 다양한 별점으로 수강후기를 작성했을 때, 평균 별점이 정확하게 계산된다")
        void shouldCalculateCorrectAverageRatingWhenMultipleUsersAddVariousRatings() {
            // given
            User user1 = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            User user2 = User.builder()
                    .userNumber(20230858)
                    .userName("김철수")
                    .department("소프트웨어학과")
                    .nickname("kim123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            User user3 = User.builder()
                    .userNumber(20230859)
                    .userName("박영희")
                    .department("소프트웨어학과")
                    .nickname("park123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "데이터구조", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "이교수", LectureType.전공필수, 0L);
            
            ClassReviewRequest request1 = new ClassReviewRequest(
                    "데이터구조", 20230857, 3.0, "보통", "내용1"
            );
            ClassReviewRequest request2 = new ClassReviewRequest(
                    "데이터구조", 20230858, 4.5, "좋음", "내용2"
            );
            ClassReviewRequest request3 = new ClassReviewRequest(
                    "데이터구조", 20230859, 2.5, "아쉬움", "내용3"
            );
            
            // Mock 설정
            LectureHistoryResponse response1 = LectureHistoryResponse.of(lecture, user1);
            LectureHistoryResponse response2 = LectureHistoryResponse.of(lecture, user2);
            LectureHistoryResponse response3 = LectureHistoryResponse.of(lecture, user3);
            
            given(lectureHistoryValidator.validate(20230857, "데이터구조")).willReturn(response1);
            given(lectureHistoryValidator.validate(20230858, "데이터구조")).willReturn(response2);
            given(lectureHistoryValidator.validate(20230859, "데이터구조")).willReturn(response3);
            doNothing().when(reviewHistoryValidator).validateHistory(any(User.class), eq(lecture));
            doNothing().when(reviewDataService).save(any(ClassReview.class));
            
            // when
            reviewService.write(request1); // 3.0
            reviewService.write(request2); // 4.5
            reviewService.write(request3); // 2.5
            
            // then
            // 평균: (3.0 + 4.5 + 2.5) / 3 = 10.0 / 3 = 3.333...
            assertThat(lecture.getStarRating().getReviewCount()).isEqualTo(3L);
            assertThat(lecture.getStarRating().getTotalRating()).isEqualTo(10.0);
            assertThat(lecture.getStarRating().getAverageRating()).isCloseTo(3.333, org.assertj.core.data.Offset.offset(0.001));
            
            verify(reviewDataService, times(3)).save(any(ClassReview.class));
        }
    }
}