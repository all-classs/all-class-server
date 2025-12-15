package org.classreviewsite.config;

import org.classreviewsite.domain.review.ClassReviewDataRepository;
import org.classreviewsite.review.service.*;
import org.classreviewsite.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewConfig {

    @Bean
    public ReviewWriter reviewWriter(ClassReviewDataRepository classReviewDataRepository) {
        return new ReviewWriter(classReviewDataRepository);
    }

    @Bean
    public ReviewEditor reviewEditor(ReviewDataService reviewDataService, ReviewHistoryValidator reviewHistoryValidator) {
        return new ReviewEditor(reviewDataService, reviewHistoryValidator);
    }

    @Bean
    public ReviewDeletor reviewDeletor(UserService userService, ReviewDataService reviewDataService, LikeStatusCheckor likeStatusCheckor) {
        return new ReviewDeletor(userService, reviewDataService, likeStatusCheckor);
    }

    @Bean
    public ReviewFinder reviewFinder() {
        return new ReviewFinder();
    }
}
