package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.user.infrastructure.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeUserService {

    private final LikeDataService likeDataService;

    public enum LikeStatus {
        ALREADY_LIKE,
        POSSIBLE_LIKE
    }

    @Transactional
    public LikeStatus isLiked(User user, ClassReview classReview) {
        try {
            likeDataService.check(user, classReview);
            // If no exception, user hasn't liked yet - add like
            classReview.like();
            Likes like = Likes.toEntity(classReview, user);
            likeDataService.save(like);
            return LikeStatus.ALREADY_LIKE;
        } catch (Exception e) {
            // User already liked - remove like
            classReview.like(classReview.getLikes() - 1);
            likeDataService.deleteByClassReviewAndUser(classReview, user);
            return LikeStatus.POSSIBLE_LIKE;
        }
    }
}