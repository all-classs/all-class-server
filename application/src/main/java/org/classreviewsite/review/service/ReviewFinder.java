package org.classreviewsite.review.service;

import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.handler.exception.ReviewNotFoundException;
import org.classreviewsite.review.controller.data.Response.ReviewMeResponse;
import org.classreviewsite.review.controller.data.Response.ReviewResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewFinder {

    // TODO: 의존성 필드 추가 필요
    // private final ReviewDataService reviewDataService;
    // private final LectureDataService lectureDataService;
    // private final ClassReviewDataRepository classReviewDataRepository;

    /*
    public ClassReview find() {

    }


    @Transactional(readOnly = true)
    public List<ReviewResponse> findAll(final Long lectureId){

        List<ClassReview> reviews = reviewDataService.getAll(lectureId);

        validateReviewsExist(reviews);

        return reviews.stream().map(ReviewResponse::from).toList();
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
        if(reviews.isEmpty()){
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
    }
    */
}
