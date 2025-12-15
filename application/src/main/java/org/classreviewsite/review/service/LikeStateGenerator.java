package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.review.Likes;
import org.classreviewsite.domain.user.User;
import org.springframework.stereotype.Service;

import static org.classreviewsite.review.service.LikedStatus.ALREADY_LIKE;
import static org.classreviewsite.review.service.LikedStatus.POSSIBLE_LIKE;

@Service
@RequiredArgsConstructor
public class LikeStateGenerator {

    LikeStatusCheckor likeStatusCheckor;

    public String generate(User user, ClassReview review) {
        final String liked = likeStatusCheckor.check(user, review);

        return liked;
    }
}
