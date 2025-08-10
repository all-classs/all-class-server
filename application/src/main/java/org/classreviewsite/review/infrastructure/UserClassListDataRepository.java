package org.classreviewsite.review.infrastructure;

import org.classreviewsite.lecture.infrastructure.Enrollment;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.user.infrastructure.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserClassListDataRepository extends JpaRepository<Enrollment, Long> {

    Optional<List<Enrollment>> findByUserNumber(@Param("userNumber") User userNumber);

    Optional<Enrollment> findByUserNumberAndLecture(@Param("userNumber") User userNumber, @Param("lecture") Lecture lectureNumber);

    @Query("select m from Enrollment m join fetch m.userNumber and join fetch m.lecture where m.userNumber.userNumber = :userNumber")
    List<Enrollment> findByUserNumberWithClassAndUser(@Param("userNumber") int userNumber);


}
