package org.classreviewsite.lecture.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureDataRepository extends JpaRepository<Lecture, Long> {


    @Query("select distinct m.department from Lecture m")
    List<String> findDistinctDepartment();


    Optional<Lecture> findByLectureName(@Param("lectureName") String lectureName);


    List<Lecture> findByUniversity(@Param("university") String university);



}
