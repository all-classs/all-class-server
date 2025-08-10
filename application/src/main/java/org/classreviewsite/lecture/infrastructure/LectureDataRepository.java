package org.classreviewsite.lecture.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureDataRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByLectureName(@Param("lectureName") String lectureName);
    List<Lecture> findByUniversity(@Param("university") String university);
    Optional<Lecture> findByLectureId(Long lectureId);
}
