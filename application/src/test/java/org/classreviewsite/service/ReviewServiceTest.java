package org.classreviewsite.service;

import jakarta.persistence.EntityManager;
import org.classreviewsite.handler.exception.*;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.lecture.service.EnrollmentService;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.classreviewsite.review.controller.data.Response.ReviewMeResponse;
import org.classreviewsite.review.controller.data.Response.ReviewResponse;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.ClassReviewDataRepository;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.review.infrastructure.LikesDataRepository;
import org.classreviewsite.review.service.LikeService;
import org.classreviewsite.review.service.ReviewService;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks ReviewService reviewService;
    
    @Mock ClassReviewDataRepository classReviewDataRepository;
    @Mock LikesDataRepository likesDataRepository;
    @Mock LectureDataService lectureDataService;
    @Mock UserService userService;
    @Mock LikeService likeService;
    @Mock EnrollmentService enrollmentService;
    @Mock EntityManager entityManager;

    @Nested
    @DisplayName("수강후기 조회 테스트")
    class findByIdTest {
        
        @Test
        @DisplayName("존재하는 ID로 조회 시, 수강후기를 반환한다")
        void findByIdSuccess() {
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview expectedReview = ClassReview.create(lecture, user, 4.5, 0, "내용", "제목");
            
            given(classReviewDataRepository.findById(reviewId)).willReturn(Optional.of(expectedReview));
            
            // when
            ClassReview result = reviewService.findById(reviewId);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result.getPostTitle()).isEqualTo("제목");
            assertThat(result.getPostContent()).isEqualTo("내용");
            assertThat(result.getStarLating()).isEqualTo(4.5);
        }
        
        @Test
        @DisplayName("존재하지 않는 ID로 조회 시, NoSuchElementException을 발생한다")
        void findByIdNotFound() {
            // given
            Long reviewId = 999L;
            given(classReviewDataRepository.findById(reviewId)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> reviewService.findById(reviewId))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("해당 수강후기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("수강후기 전체 조회 테스트")
    class findAllTest {
        
        @Test
        @DisplayName("강의 ID로 전체 조회 시, 수강후기 목록을 반환한다")
        void findAllSuccess() {
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview review1 = ClassReview.create(lecture, user, 4.5, 0, "내용1", "제목1");
            ClassReview review2 = ClassReview.create(lecture, user, 3.0, 5, "내용2", "제목2");
            
            List<ClassReview> reviews = Arrays.asList(review1, review2);
            
            given(classReviewDataRepository.findAll(lectureId)).willReturn(reviews);
            
            // when
            List<ReviewResponse> result = reviewService.findAll(lectureId);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
        }
        
        @Test
        @DisplayName("강의 ID로 전체 조회 시 결과가 없으면, ReviewNotFoundException을 발생한다")
        void findAllEmpty() {
            // given
            Long lectureId = 1L;
            given(classReviewDataRepository.findAll(lectureId)).willReturn(List.of());
            
            // when & then
            assertThatThrownBy(() -> reviewService.findAll(lectureId))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessage("수강 후기가 어디에도 없습니다.");
        }
    }

    @Nested
    @DisplayName("수강후기 작성 테스트")
    class addReviewPostTest {
        
        @Test
        @DisplayName("유효한 요청으로 수강후기 작성 시, 정상적으로 저장된다")
        void addReviewPostSuccess() {
            // given
            ClassReviewRequest request = new ClassReviewRequest(
                    "강의명", 20230857L, 4.5, "제목", "내용"
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            given(lectureDataService.findByLectureName("강의명")).willReturn(lecture);
            given(userService.get(20230857L)).willReturn(user);
            given(classReviewDataRepository.findByUserNumberAndLecId(user, lecture)).willReturn(Optional.empty());
            
            // when
            reviewService.addReviewPost(request);
            
            // then
            verify(classReviewDataRepository).save(any(ClassReview.class));
            verify(enrollmentService).findByUserNumber(20230857, "강의명");
        }
        
        @Test
        @DisplayName("이미 작성한 강의에 수강후기 작성 시, AlreadyWritePostException을 발생한다")
        void addReviewPostAlreadyWritten() {
            // given
            ClassReviewRequest request = new ClassReviewRequest(
                    "강의명", 20230857L , 4.5, "제목", "내용"
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview existingReview = ClassReview.create(lecture, user, 3.0, 0, "기존내용", "기존제목");
            
            given(lectureDataService.findByLectureName("강의명")).willReturn(lecture);
            given(userService.get(20230857L)).willReturn(user);
            given(classReviewDataRepository.findByUserNumberAndLecId(user, lecture))
                    .willReturn(Optional.of(existingReview));
            
            // when & then
            assertThatThrownBy(() -> reviewService.addReviewPost(request))
                    .isInstanceOf(AlreadyWritePostException.class)
                    .hasMessage("이미 작성한 강의입니다.");
        }
    }

    @Nested
    @DisplayName("수강후기 수정 테스트")
    class updateReviewPostTest {
        
        @Test
        @DisplayName("유효한 요청으로 수강후기 수정 시, 정상적으로 수정된다")
        void updateReviewPostSuccess() {
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview review = ClassReview.create(lecture, user, 4.5, 0, "원본내용", "원본제목");
            
            given(classReviewDataRepository.findById(1L)).willReturn(Optional.of(review));
            
            // when
            Long result = reviewService.updateReviewPost(request);
            
            // then
            assertThat(result).isEqualTo(review.getReviewId());
        }
        
        @Test
        @DisplayName("다른 사용자의 수강후기 수정 시, InValidReviewAccessException을 발생한다")
        void updateReviewPostInvalidUser() {
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview review = ClassReview.create(lecture, originalUser, 4.5, 0, "원본내용", "원본제목");
            
            given(classReviewDataRepository.findById(1L)).willReturn(Optional.of(review));
            
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
        void likeReviewFirst() {
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview review = ClassReview.create(lecture, user, 4.5, 0, "내용", "제목");
            
            given(userService.get(20230857L)).willReturn(user);
            given(classReviewDataRepository.findById(1L)).willReturn(Optional.of(review));
            given(likesDataRepository.findByUserAndClassReview(user, review)).willReturn(Optional.empty());
            
            // when
            String result = reviewService.likeReview(request);
            
            // then
            assertThat(result).isEqualTo("좋아요가 추가되었습니다.");
            verify(likeService).save(any(Likes.class));
        }
        
        @Test
        @DisplayName("이미 좋아요한 수강후기에 좋아요 요청 시, 좋아요가 취소된다")
        void likeReviewCancel() {
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview review = ClassReview.create(lecture, user, 4.5, 1, "내용", "제목");
            Likes existingLike = Likes.toEntity(review, user);
            
            given(userService.get(20230857L)).willReturn(user);
            given(classReviewDataRepository.findById(1L)).willReturn(Optional.of(review));
            given(likesDataRepository.findByUserAndClassReview(user, review)).willReturn(Optional.of(existingLike));
            
            // when
            String result = reviewService.likeReview(request);
            
            // then
            assertThat(result).isEqualTo("좋아요가 취소되었습니다.");
            verify(likeService).deleteByClassReviewAndUser(review, user);
        }
    }

    @Nested
    @DisplayName("내 수강후기 조회 테스트")
    class findMyReviewTest {
        
        @Test
        @DisplayName("사용자 번호로 내 수강후기 조회 시, 수강후기 목록을 반환한다")
        void findMyReviewSuccess() {
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview review1 = ClassReview.create(lecture, user, 4.5, 0, "내용1", "제목1");
            ClassReview review2 = ClassReview.create(lecture, user, 3.0, 5, "내용2", "제목2");
            
            List<ClassReview> reviews = Arrays.asList(review1, review2);
            
            given(classReviewDataRepository.findByUserNumber(userNumber)).willReturn(reviews);
            
            // when
            List<ReviewMeResponse> result = reviewService.findMyReview(userNumber);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
        }
        
        @Test
        @DisplayName("내 수강후기가 없을 때 조회 시, ReviewNotFoundException을 발생한다")
        void findMyReviewEmpty() {
            // given
            int userNumber = 20230857;
            given(classReviewDataRepository.findByUserNumber(userNumber)).willReturn(List.of());
            
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
        void deleteReviewPostSuccess() {
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
                                        "소프트웨어학과", "한국대학교", "교수명", LectureType.교양선택);
            
            ClassReview review = ClassReview.create(lecture, user, 4.5, 0, "내용", "제목");
            
            given(userService.get(20230857L)).willReturn(user);
            given(classReviewDataRepository.findByReviewIdAndUserNumber(1L, user)).willReturn(Optional.of(review));
            given(lectureDataService.findById(1L)).willReturn(lecture);
            
            // when
            reviewService.deleteReviewPost(request);
            
            // then
            verify(likeService).deleteAllByClassReview(review);
            verify(classReviewDataRepository).deleteById(1L);
        }
    }
}