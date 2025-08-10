package org.classreviewsite.review.infrastructure;

import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.user.infrastructure.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassReviewDataRepository extends JpaRepository<ClassReview, Long> {

    @Query("select m from ClassReview m join fetch m.lecId join fetch m.userNumber where m.lecId.lectureId = :lectureId")
    List<ClassReview> findAll(@Param("lectureId") Long lectureId);

    Optional<ClassReview> findByUserNumberAndLecId(@Param("userNumber") User userNumber, @Param("lecId") Lecture lecId);

    List<ClassReview> findAllByLecIdOrderByStarLatingDesc(Lecture lecId);

    List<ClassReview> findAllByLecIdOrderByStarLatingAsc(Lecture lecId);

    List<ClassReview> findAllByLecIdOrderByLikesDesc(Lecture lecId );

    List<ClassReview> findAllByLecIdOrderByLikesAsc(Lecture lecId);

    List<ClassReview> findAllByLecIdOrderByCreatedDateDesc(Lecture lecId);


    @Query("select m from ClassReview m join fetch m.lecId join fetch m.userNumber where m.userNumber.userNumber = :userNumber")
    List<ClassReview> findByUserNumber(@Param("userNumber") int userNumber);

    List<ClassReview> findByLecId(@Param("lecId") Lecture lecId);

    Optional<ClassReview> findByReviewIdAndUserNumber(Long reviewId, User userNumber);

    List<ClassReview> findAllByUserNumber(User userNumber);

}
