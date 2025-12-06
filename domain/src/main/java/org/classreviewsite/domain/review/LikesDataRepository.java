package org.classreviewsite.domain.review;

import org.classreviewsite.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesDataRepository extends JpaRepository<Likes, Long> {

    void deleteAllByClassReview(ClassReview classReview);

    Likes findByUserAndClassReview(User user , ClassReview classReview);

    void deleteByClassReviewAndUser(ClassReview classReview, User user);


}








