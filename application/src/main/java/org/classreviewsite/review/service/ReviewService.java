package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.handler.exception.AlreadyLikeException;
import org.classreviewsite.handler.exception.AlreadyWritePostException;
import org.classreviewsite.handler.exception.ReviewNotFoundException;
import org.classreviewsite.handler.exception.UpdateFailedException;
import org.classreviewsite.lecture.infrastructure.Lecture;
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
        if(request.getDifficulty() == null ||
                request.getFunny() == null ||
                request.getImportant() == null){
            throw new UpdateFailedException("수강후기 수정이 정상적으로 진행되지 않았습니다.");
        }

        ClassReview post = classReviewDataRepository.findById(request.getPostId()).orElseThrow(
                () -> new ReviewNotFoundException("해당 수강후기가 존재하지 않습니다.")
        );

        // 별점 수정
//        lectureService.cancelTotalStarLating(post.getLectureId(), post.getStarLating());
//        Double star =  lectureService.updateTotalStarLating(post.getLectureId(), request.getStarLating());
//        lectureService.updateAverageStarLating(post.getLectureId(), star, post.getLectureId().getReviewCount());

        return post.getReviewId();
    }

    @Transactional
    public void deleteReviewPost(DeleteReviewRequest request){
        User user = userService.findById(Long.valueOf(request.getUserNumber()));

        ClassReview deletedClassReview = findByReviewIdAndUserNumber(request.getPostId(), user);

        deleteById(request.getPostId());
        likeService.deleteAllByClassReview(deletedClassReview);

        Lecture updateLecture = lectureService.findById(deletedClassReview.getLecId().getLectureId());

//        Double star = lectureService.cancelTotalStarLating(updateLecture, deletedClassReview.getStarLating());
//        lectureService.updateAverageStarLating(updateLecture, star);
    }

    @Transactional
    public String likeReview(LikeRequest request){

        User user = userService.findById(Long.valueOf(request.getUserNumber()));
        ClassReview classReview = findById(request.getPostId());

        try{
            likeService.check(user, classReview);
        }catch (AlreadyLikeException exception){
            classReview.like(classReview.getLikes()-1);
            likeService.deleteByClassReviewAndUser(classReview, user);
            return "좋아요가 취소되었습니다.";
        }

        classReview.like();
        Likes like = Likes.toEntity(classReview, user);
        likeService.save(like);

        return "좋아요가 추가되었습니다.";

    }

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
