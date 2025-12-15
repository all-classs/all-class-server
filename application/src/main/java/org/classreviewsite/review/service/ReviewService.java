package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDataService reviewDataService;
    private final LectureDataService lectureDataService;
    private final UserService userService;
    private final LikeStateGenerator likeStateGenerator;
    private final ReviewWriter reviewWriter;
    private final ReviewHistoryValidator reviewHistoryValidator;
    private final LectureHistoryValidator lectureHistoryValidator;

    @Transactional
    public void write(final ClassReviewRequest request){
        final var userNumber = request.getUserNumber();
        final var lectureName = request.getLectureName();

        lectureHistoryValidator.validate(userNumber.intValue(), lectureName);

        Lecture foundLecture = lectureDataService.findByLectureName(lectureName);
        User foundUser = userService.findUser(userNumber);

        reviewHistoryValidator.validateHistory(foundUser, foundLecture);
        reviewWriter.write(foundLecture, foundUser, request);

        foundLecture.addStarRating(request.getStarLating());
    }

    @Transactional
    public String likeReview(LikeRequest request){
        final var userNumber = request.getUserNumber();
        final var likedReviewId = request.getPostId();

        User user = userService.findUser(userNumber);
        ClassReview review = reviewDataService.getReviewById(likedReviewId);
        String result =  likeStateGenerator.generate(user, review);
        return result;
    }
}
