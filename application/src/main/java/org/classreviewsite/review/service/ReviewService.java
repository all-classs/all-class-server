package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.handler.exception.*;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.service.EnrollmentDataService;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Response.ReviewMeResponse;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.classreviewsite.review.controller.data.Response.ReviewResponse;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.classreviewsite.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDataService reviewDataService;

    private final LectureDataService lectureDataService;

    private final UserService userService;

    private final LikeDataService likeDataService;

    private final LikeUserService likeUserService;

    private final EnrollmentDataService enrollmentDataService;


    @Transactional(readOnly = true)
    public List<ReviewResponse> findAll(final Long lectureId){

        List<ClassReview> reviews = reviewDataService.getAll(lectureId);

        validateReviewsExist(reviews);

        return reviews.stream().map(ReviewResponse::from).toList();
    }

    @Transactional
    public void addReviewPost(final ClassReviewRequest request){
        final var userNumber = request.getUserNumber();
        final var lectureName = request.getLectureName();

        noPermissionCheck(userNumber.intValue(), lectureName); // 수강한 강의인지 확인

        Lecture foundLecture = lectureDataService.findByLectureName(lectureName);

        User foundUser = userService.findUser(userNumber);

        validateCheckPost(foundUser, foundLecture);

        ClassReview classReview = ClassReview.create(foundLecture, foundUser, request.getStarLating(), request.getPostContent(), request.getPostTitle());

        reviewDataService.save(classReview);

        foundLecture.addStarRating(request.getStarLating());

    }

    @Transactional(readOnly = true)
    public void noPermissionCheck(final int userNumber,final String lectureName) {
        enrollmentDataService.findByUserNumber(userNumber, lectureName);
    }


    @Transactional(readOnly = true)
    public void validateCheckPost(final User user,final Lecture lecture){
       reviewDataService.getReviewByUserNumberAndLectureId(user, lecture)
               .ifPresent(m -> {
                   throw new AlreadyWritePostException("이미 작성한 강의입니다.");
               });
    }

    @Transactional
    public Long updateReviewPost(final UpdateReviewRequest request){

        ClassReview post = reviewDataService.getReviewById(request.getPostId());

        validateCheckReviewAccess(request.getUserNumber().intValue(), post.getUserNumber().getUserNumber());

        post.update(request.getPostTitle(), request.getPostContent(), request.getStarLating());

        // 별점 수정
        Lecture lecture = post.getLecId();
        lecture.updateStarRating(post.getStarLating(), request.getStarLating());

        return post.getReviewId();
    }

    private void validateCheckReviewAccess(final int requestUserNumber, final int postedUserNumber) {
        if(requestUserNumber != postedUserNumber) {
            throw new InValidReviewAccessException("작성하지 않은 사용자의 요청입니다.");
        }
    }

    @Transactional
    public void deleteReviewPost(DeleteReviewRequest request){
        User user = userService.findUser(Long.valueOf(request.getUserNumber()));

        ClassReview deletedClassReview = reviewDataService.findByReviewIdAndUserNumber(request.getPostId(), user);

        likeDataService.deleteAllByClassReview(deletedClassReview);

        deletedClassReview.getLecId().removeStarRating(deletedClassReview.getStarLating());

        reviewDataService.deleteById(request.getPostId());

    }

    @Transactional
    public String likeReview(LikeRequest request){
        final var userNumber = request.getUserNumber();
        final var likedReviewId = request.getPostId();

        User user = userService.findUser(Long.valueOf(userNumber));

        ClassReview classReview = reviewDataService.getReviewById(likedReviewId);

        final LikedStatus liked = likeUserService.isLiked(user, classReview);

        switch (liked) {
            case ALREADY_LIKE:
                return "좋아요가 취소되었습니다.";
            case POSSIBLE_LIKE:
                return "좋아요가 추가되었습니다.";
            default:
                return "좋아요 처리 중 문제가 발생했습니다.";
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewMeResponse> findMyReview(int userNumber){

        List<ClassReview> reviews = reviewDataService.getReviewsByUserNumber(userNumber);

        validateReviewsExist(reviews);

        return reviews.stream().map(ReviewMeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByStarLatingDesc(Long lectureId){

        Lecture lecture = lectureDataService.findById(lectureId);

        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByStarLatingDesc(lecture);// desc
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByStarLatingAsc(Long lectureId){
        Lecture lecture = lectureDataService.findById(lectureId);

        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByStarLatingAsc(lecture);
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByLikesDesc(Long lectureId){
        Lecture lecture = lectureDataService.findById(lectureId);
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByLikesDesc(lecture);
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }

        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByLikesAsc(Long lectureId){
        Lecture lecture = lectureDataService.findById(lectureId);
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByLikesAsc(lecture);
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByCreateDateDesc(Long lectureId){
        Lecture lecture = lectureDataService.findById(lectureId);
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByCreatedDateDesc(lecture);
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }

        return result.stream().map(ReviewResponse::from).toList();
    }


    private void validateReviewsExist(List<ClassReview> reviews) {
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException("수강 후기가 존재하지 않습니다.");
        }
    }
}
