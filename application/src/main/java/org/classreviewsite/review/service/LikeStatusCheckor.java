package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.handler.exception.AlreadyLikeException;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.review.Likes;
import org.classreviewsite.domain.review.LikesDataRepository;
import org.classreviewsite.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.classreviewsite.review.service.LikedStatus.ALREADY_LIKE;
import static org.classreviewsite.review.service.LikedStatus.POSSIBLE_LIKE;

@Service
@RequiredArgsConstructor
public class LikeStatusCheckor {

    private final LikesDataRepository likesDataRepository;

    @Transactional(noRollbackFor = AlreadyLikeException.class)
    public String check(User user, ClassReview classReview){
        Likes likes = likesDataRepository.findByUserAndClassReview(user, classReview);


        if (likes.equals(ALREADY_LIKE)) {
            return "좋아요가 취소되었습니다.";
        } else if (likes.equals(POSSIBLE_LIKE)) {
            return "좋아요가 추가되었습니다.";
        }
        return "좋아요 처리 중 문제가 발생했습니다.";
        // 일단 이렇게 처리해두자.

//        switch (likes) {
//            case ALREADY_LIKE:
//                return "좋아요가 취소되었습니다.";
//            case POSSIBLE_LIKE:
//                return "좋아요가 추가되었습니다.";
//            default:
//                return "좋아요 처리 중 문제가 발생했습니다.";
//        }
//        return "";
    }
}
