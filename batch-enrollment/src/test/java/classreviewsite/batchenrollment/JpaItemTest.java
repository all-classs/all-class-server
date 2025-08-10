package classreviewsite.batchenrollment;

import classreviewsite.batchenrollment.config.data.EnrollmentCsv;
import classreviewsite.batchenrollment.writer.CsvToEntityConverter;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.classreviewsite.lecture.infrastructure.Enrollment;
import org.classreviewsite.lecture.infrastructure.EnrollmentDataRepository;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureDataRepository;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaItemTest {

    @Autowired private LectureDataRepository lectureDataRepository;
    @Autowired private UserDataRepository userDataRepository;
    @Autowired private EnrollmentDataRepository enrollmentDataRepository;
    @Autowired EntityManager em;


    @Test
    @Transactional
    @DisplayName("write 메서드가 동작할때, 존재하는 Enrollment 데이터가 두개미만이 조회된다.")
    void findByUserNumberAndLectureId() {
        //given
        EnrollmentCsv item = new EnrollmentCsv();
        item.setLectureId(120002L);
        item.setStudentNumber(20221180);

        Lecture lecture = lectureDataRepository.findById(item.getLectureId()).get();
        User user = userDataRepository.findByUserNumber(item.getStudentNumber()).get();
        Enrollment enrollment = CsvToEntityConverter.toEnrollment(item, user, lecture);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureId(user.getUserNumber(), lecture.getLectureId()).get();});

        enrollmentDataRepository.save(enrollment);
        em.clear();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureId(user.getUserNumber(), lecture.getLectureId()).get();});
    }
}
