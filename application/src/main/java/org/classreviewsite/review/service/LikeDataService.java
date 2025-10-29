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
public class LikeDataService {

    private final LikesDataRepository likesDataRepository;

    @Transactional(noRollbackFor = AlreadyLikeException.class)
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
