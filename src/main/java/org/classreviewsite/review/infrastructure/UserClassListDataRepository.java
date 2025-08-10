package org.classreviewsite.review.infrastructure;

import org.classreviewsite.lecture.data.Lecture;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.lecture.infrastructure.UserClassList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserClassListDataRepository extends JpaRepository<UserClassList, Long> {

    Optional<List<UserClassList>> findByUserNumber(@Param("userNumber") User userNumber);

    Optional<UserClassList> findByUserNumberAndLecture(@Param("userNumber") User userNumber, @Param("lecture") Lecture lectureNumber);

    @Query("select m from UserClassList m join fetch m.userNumber and join fetch m.lecture where m.userNumber.userNumber = :userNumber")
    List<UserClassList> findByUserNumberWithClassAndUser(@Param("userNumber") int userNumber);


}
