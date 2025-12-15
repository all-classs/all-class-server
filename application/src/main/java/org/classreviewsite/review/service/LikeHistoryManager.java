package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.review.Likes;
import org.classreviewsite.domain.review.LikesDataRepository;
import org.classreviewsite.domain.user.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeHistoryManager {

    private final LikesDataRepository likesDataRepository;


    @Transactional
    public Likes save(Likes likes){
        return likesDataRepository.save(likes);
    }

    @Transactional
    public void deleteByClassReviewAndUser(ClassReview classReview, User user){
        if (classReview == null) {
            throw new IllegalArgumentException("수강후기 객체가 잘못 전달되었습니다.");
        }
        if (user == null) {
            throw new IllegalArgumentException("사용자 객체가 잘못 전달되었습니다.");
        }
        likesDataRepository.deleteByClassReviewAndUser(classReview, user);
    }

    @Transactional
    public void deleteAllByClassReview(ClassReview classReview){
        if (classReview == null) {
            throw new IllegalArgumentException("수강후기 객체가 잘못 전달되었습니다.");
        }
        likesDataRepository.deleteAllByClassReview(classReview);
    }


}
