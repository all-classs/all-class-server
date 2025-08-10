package org.classreviewsite.review.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.classreviewsite.handler.exception.*;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.service.EnrollmentService;
import org.classreviewsite.review.controller.data.Response.ReviewMeResponse;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.classreviewsite.review.controller.data.Response.ReviewResponse;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.classreviewsite.review.infrastructure.ClassReviewDataRepository;
import org.classreviewsite.review.infrastructure.LikesDataRepository;
import org.classreviewsite.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ClassReviewDataRepository classReviewDataRepository;

    private final LikesDataRepository likesDataRepository;

    private final LectureDataService lectureDataService;

    private final UserService userService;

    private final LikeService likeService;

    private final EnrollmentService enrollmentService;

    private final EntityManager em;

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

        Lecture foundLecture = lectureDataService.findByLectureName(request.getLectureName());

        User foundUser = userService.get(request.getUserNumber());

        ClassReview classReview = ClassReview.create(
                foundLecture,
                foundUser,
                request.getStarLating(),
                0,
                request.getPostContent(),
                request.getPostTitle()
        );


        validateCheckPost(foundUser, foundLecture);
        foundLecture.addReview(request.getStarLating());
        classReviewDataRepository.save(classReview);

        em.flush();
        em.clear();
    }

    @Transactional(readOnly = true)
    public void noPermissionCheck(int userNumber, String lectureName) {
        enrollmentService.findByUserNumber(userNumber, lectureName);
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

        ClassReview post = classReviewDataRepository.findById(request.getPostId()).orElseThrow(
                () -> new ReviewNotFoundException("해당 수강후기가 존재하지 않습니다.")
        );

        if(request.getUserNumber() != post.getUserNumber().getUserNumber()) {
            throw new InValidReviewAccessException("작성하지 않은 사용자의 요청입니다.");
        }

        post.update(request.getPostTitle(), request.getPostContent(), request.getStarLating());

        // 별점 수정
        post.getLecId().removeReview(post.getStarLating());
        post.getLecId().addReview(request.getStarLating());

        em.flush();
        em.clear();

        return post.getReviewId();
    }

    @Transactional
    public void deleteReviewPost(DeleteReviewRequest request){
        User user = userService.get(Long.valueOf(request.getUserNumber()));

        ClassReview deletedClassReview = findByReviewIdAndUserNumber(request.getPostId(), user);

        likeService.deleteAllByClassReview(deletedClassReview);
        deleteById(request.getPostId());

        Lecture updateLecture = lectureDataService.findById(deletedClassReview.getLecId().getLectureId());

        updateLecture.removeReview(deletedClassReview.getStarLating());
        em.flush();
        em.clear();
    }

    @Transactional
    public String likeReview(LikeRequest request){

        User user = userService.get(Long.valueOf(request.getUserNumber()));
        ClassReview classReview = findById(request.getPostId());

        // 좋아요 중복 체크
        boolean alreadyLiked = likesDataRepository.findByUserAndClassReview(user, classReview).isPresent();
        
        if (alreadyLiked) {
            // 좋아요 취소
            classReview.like(classReview.getLikes()-1);
            likeService.deleteByClassReviewAndUser(classReview, user);
            return "좋아요가 취소되었습니다.";
        } else {
            // 좋아요 추가
            classReview.like();
            Likes like = Likes.toEntity(classReview, user);
            likeService.save(like);
            return "좋아요가 추가되었습니다.";
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewMeResponse> findMyReview(int userNumber){
        List<ClassReview> list = classReviewDataRepository.findByUserNumber(userNumber);
        if(list.isEmpty()){
            throw new ReviewNotFoundException("수강후기가 존재하지 않습니다.");
        }

        return list.stream().map(ReviewMeResponse::from).toList();
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
}
