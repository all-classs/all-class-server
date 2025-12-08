package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.review.ClassReviewDataRepository;
import org.classreviewsite.handler.exception.ReviewNotFoundException;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewDataService {

    private final ClassReviewDataRepository classReviewDataRepository;

    public List<ClassReview> getAll(Long lectureId) {
        List<ClassReview> reviews = classReviewDataRepository.findAll(lectureId);
        validateReviews(reviews);
        return reviews;
    }

    @Transactional(readOnly = true)
    public ClassReview getReviewById(Long reviewId) {
        return classReviewDataRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("해당 수강후기가 존재하지 않습니다."));
    }

    public void save(ClassReview review){
        classReviewDataRepository.save(review);
    }

    public ClassReview findByReviewIdAndUserNumber(Long reviewId, User userNumber) {
        return classReviewDataRepository.findByReviewIdAndUserNumber(reviewId, userNumber).orElseThrow(() -> new NullPointerException("해당 수강후기가 존재하지 않습니다."));
    }

    @Transactional
    public void deleteById(Long id) {
        classReviewDataRepository.deleteById(id);
    }

    public List<ClassReview> getReviewsByUserNumber(final int userNumber) {
        List<ClassReview> reviews = classReviewDataRepository.findByUserNumber(userNumber);
        validateReviews(reviews);

        return reviews;
    }

    public Optional<ClassReview> getReviewByUserNumberAndLectureId(User user, Lecture lecture) {
        return classReviewDataRepository.findByUserNumberAndLecId(user, lecture);
    }

    private void validateReviews(List<ClassReview> reviews) {
        if(reviews.isEmpty()) {
            throw new ReviewNotFoundException("수강 후기가 어디에도 없습니다.");
        }
    }

}
