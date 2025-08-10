package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.handler.exception.AlreadyLikeException;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.review.infrastructure.LikesDataRepository;
import org.classreviewsite.user.infrastructure.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikesDataRepository likesDataRepository;

    @Transactional
    public void check(User user, ClassReview classReview){
        likesDataRepository.findByUserAndClassReview(user, classReview)
                .ifPresent(m -> {
                    throw new AlreadyLikeException("좋아요가 취소 되었습니다.");
                });
    }

    @Transactional
    public void save(Likes likes){
        likesDataRepository.save(likes);
    }

    @Transactional
    public void deleteByClassReviewAndUser(ClassReview classReview, User user){
        likesDataRepository.deleteByClassReviewAndUser(classReview, user);
    }

    @Transactional
    public void deleteAllByClassReview(ClassReview classReview){
        likesDataRepository.deleteAllByClassReview(classReview);
    }

}
