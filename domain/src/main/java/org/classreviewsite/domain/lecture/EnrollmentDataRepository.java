package org.classreviewsite.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentDataRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserNumber_UserNumberAndLecture_LectureId(int userNumber, Long lectureId);
    Optional<List<Enrollment>> findByUserNumber_UserNumber(int userNumber);
    Optional<Enrollment> findByUserNumber_UserNumberAndLecture_LectureName(int userNumber, String lectureName);
}








