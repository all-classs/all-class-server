package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.handler.exception.AlreadyLikeException;
import org.classreviewsite.handler.exception.AlreadyWritePostException;
import org.classreviewsite.handler.exception.ReviewNotFoundException;
import org.classreviewsite.handler.exception.UpdateFailedException;
import org.classreviewsite.lecture.data.Lecture;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.classreviewsite.review.controller.data.Response.ReviewResponse;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.lecture.service.LectureService;
import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.classreviewsite.review.infrastructure.ClassReviewDataRepository;
import org.classreviewsite.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ClassReviewDataRepository classReviewDataRepository;

    private final LectureService lectureService;

    private final UserService userService;

    private final LikeService likeService;

    private final UserClassListService userClassListService;

    @Transactional(readOnly = true)
    public ClassReview findById(Long id){
        return classReviewDataRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 수강후기가 존재하지 않습니다."));
    }

    public ClassReview findByReviewIdAndUserNumber(Long reviewId, User userNumber){
        return classReviewDataRepository.findByReviewIdAndUserNumber(reviewId, userNumber).orElseThrow(() -> new NullPointerException("해당 수강후기가 존재하지 않습니다."));
    }

    @Transactional
    public void deleteById(Long id){
        classReviewDataRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findAll(Long lectureId){
        List<ClassReview> list = classReviewDataRepository.findAll(lectureId);
        if(list.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
        return list.stream().map(ReviewResponse::from).toList();
    }

    @Transactional
    public void addReviewPost(ClassReviewRequest request){

        noPermissionCheck(request.getUserNumber().intValue(), request.getLectureName()); // 수강한 강의인지 확인

        Lecture foundLecture = lectureService.findByLectureName(request.getLectureName());

        User foundUser = userService.findById(request.getUserNumber());

        ClassReview classReview = ClassReview.create(
                foundLecture,
                foundUser,
                request.getStarLating(),
                0,
                request.getPostContent(),
                request.getPostTitle()
        );

        validateCheckPost(foundUser, foundLecture);

        classReviewDataRepository.save(classReview);

    }

    @Transactional(readOnly = true)
    public void noPermissionCheck(int userNumber, String lectureName) {
        userClassListService.findByUserNumber(userNumber, lectureName);
    }


    @Transactional(readOnly = true)
    public void validateCheckPost(User user, Lecture lecture){
       classReviewDataRepository.findByUserNumberAndLecId(user, lecture)
               .ifPresent(m -> {
                   throw new AlreadyWritePostException("이미 작성한 강의입니다.");
               });
    }

    @Transactional
    public Long updateReviewPost(UpdateReviewRequest request){
        ClassReview post = classReviewDataRepository.findById(request.getPostId()).orElseThrow(() -> new ReviewNotFoundException("해당 수강후기가 존재하지 않습니다."));
        post.updatePost(request.getPostTitle(), request.getPostContent());
        return post.getReviewId();
    }

    @Transactional
    public void deleteReviewPost(DeleteReviewRequest request){
        User user = userService.findById(Long.valueOf(request.getUserNumber())).orElseThrow(() -> new UserNotFoundException("해당 학생이 존재하지 않습니다."));

        ClassReview deletedClassReview = classReviewDataRepository.findByReviewIdAndUserNumber(request.getPostId(), user).orElseThrow(() -> new NullPointerException("해당 수강후기가 존재하지 않습니다."));

        classReviewDataRepository.deleteById(request.getPostId());

        likesDataRepository.deleteAllByClassReview(deletedClassReview);

        Lecture updateLecture = lectureService.findById(deletedClassReview.getLecId().getLectureId());

        Double difficulty = lectureService.updateDifficultyAsMinus(updateLecture, deletedClassReview);
        Double important = lectureService.updateImportantAsMinus(updateLecture, deletedClassReview);
        Double funny = lectureService.updateFunnyAsMinus(updateLecture, deletedClassReview);
        Double total = difficulty+important+funny;


        Long count = lectureService.cancelReviewCount(updateLecture);
        Long star = lectureService.cancelTotalStarLating(updateLecture, deletedClassReview.getStarLating());
        lectureService.updateAverageStarLating(updateLecture, star, count);

        if(updateLecture.getReviewCount()>0){
            lectureService.updateImportNormalization(updateLecture, important/total);
            lectureService.updateFunnyNormalization(updateLecture, funny/total);
            lectureService.updateDifficultyNormalization(updateLecture, difficulty/total);
        }



    }

    @Transactional
    public String addLikeReviewPost(LikeRequest request){

        // userNumber , reviewId
        User user = userService.findById(Long.valueOf(request.getUserNumber())).orElseThrow(() -> new UserNotFoundException("해당 학생이 존재하지 않습니다."));

        ClassReview classReview = classReviewDataRepository.findById(request.getPostId()).orElseThrow(() -> new NullPointerException("해당 수강후기가 존재하지 않습니다."));

        try{
            validateLikeReviewPost(user, classReview);
        }catch (AlreadyLikeException exception){
            int like = classReview.cancelLike(classReview.getLikes()-1);
            likesDataRepository.deleteByClassReviewAndUser(classReview, user);
            return "좋아요가 취소되었습니다.";
        }


        int like = classReview.like();

        Likes likesUser = Likes.builder()
                .classReview(classReview)
                .user(user)
                .build();

        likesDataRepository.save(likesUser);

        return "좋아요가 추가되었습니다.";

    }

//    @Transactional
    public void validateLikeReviewPost(User user , ClassReview classReview){
        likesDataRepository.findByUserAndClassReview(user, classReview)
                .ifPresent(m -> {
            throw new AlreadyLikeException("좋아요가 취소 되었습니다.");
                }
                );
    }


    public void cancelLikeReviewPost(User user , ClassReview classReview){
        likesDataRepository.deleteByClassReviewAndUser(classReview, user);

        throw new AlreadyLikeException("좋아요가 취소되었습니다.");

    }


    // 좋아요 탑 3 수강후기 찾기
    @Transactional(readOnly = true)
    public List<ClassReview> findByLecIdOrderByLikes(Long lecId){
        return classReviewDataRepository.findByLecIdOrderByLikesDesc(lecId);
    }


    // 내 수강 후기 모두 찾기
    @Transactional(readOnly = true)
    public List<ClassReview> findMyReview(int userNumber){
        List<ClassReview> list = classReviewDataRepository.findByUserNumber(userNumber);
        if(list.isEmpty()){
            throw new ReviewNotFoundException("수강후기가 존재하지 않습니다.");
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByStarLatingDesc(Long lectureId){
        Lecture lecture = lectureService.findById(lectureId);

        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByStarLatingDesc(lecture);// desc
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByStarLatingAsc(Long lectureId){
        Lecture lecture = lectureService.findById(lectureId);

        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByStarLatingAsc(lecture);
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByLikesDesc(Long lectureId){
        Lecture lecture = lectureService.findById(lectureId);
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByLikesDesc(lecture);
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }

        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByLikesAsc(Long lectureId){
        Lecture lecture = lectureService.findById(lectureId);
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByLikesAsc(lecture);
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByLectureIdOrderByCreateDateDesc(Long lectureId){
        Lecture lecture = lectureService.findById(lectureId);
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByCreatedDateDesc(lecture);
        if(result.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }

        return result.stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getMyReviews(Long userNumber){
        User user = userService.findById(userNumber);
        List<ClassReview> list = classReviewDataRepository.findAllByUserNumber(user);
        return list.stream().map(ReviewResponse::from).toList();
    }

}
