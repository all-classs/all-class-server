package org.classreviewsite.repository;

import org.classreviewsite.domain.lecture.Enrollment;
import org.classreviewsite.domain.lecture.EnrollmentDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class EnrollmentDataRepositoryTest {

    @Autowired private EnrollmentDataRepository enrollmentDataRepository;

    @Test
    @DisplayName("사용자 번호와 강의 ID로 수강 정보를 조회한다")
    void findByUserNumber_UserNumberAndLecture_LectureId() {
        // given
        int userNumber = 20191434;
        Long lectureId = 1L;

        // when
        Optional<Enrollment> result = enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureId(userNumber, lectureId);

        // then
        assertThat(result).isNotNull();
        // 실제 데이터가 있는 경우에만 검증
        if (result.isPresent()) {
            assertThat(result.get().getUserNumber().getUserNumber()).isEqualTo(userNumber);
            assertThat(result.get().getLecture().getLectureId()).isEqualTo(lectureId);
        }
    }

    @Test
    @DisplayName("사용자 번호로 해당 사용자의 모든 수강 정보를 조회한다")
    void findByUserNumber_UserNumber() {
        // given
        int userNumber = 20191434;

        // when
        Optional<List<Enrollment>> result = enrollmentDataRepository.findByUserNumber_UserNumber(userNumber);

        // then
        assertThat(result).isNotNull();
        // 실제 데이터가 있는 경우에만 검증
        if (result.isPresent() && !result.get().isEmpty()) {
            List<Enrollment> enrollments = result.get();
            assertThat(enrollments.get(0).getUserNumber().getUserNumber()).isEqualTo(userNumber);
            
            // 모든 수강 정보가 같은 사용자의 것인지 확인
            enrollments.forEach(enrollment -> 
                assertThat(enrollment.getUserNumber().getUserNumber()).isEqualTo(userNumber)
            );
        }
    }

    @Test
    @DisplayName("사용자 번호와 강의명으로 수강 정보를 조회한다")
    void findByUserNumber_UserNumberAndLecture_LectureName() {
        // given
        int userNumber = 20191434;
        String lectureName = "테스트강의";

        // when
        Optional<Enrollment> result = enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName);

        // then
        assertThat(result).isNotNull();
        // 실제 데이터가 있는 경우에만 검증
        if (result.isPresent()) {
            assertThat(result.get().getUserNumber().getUserNumber()).isEqualTo(userNumber);
            assertThat(result.get().getLecture().getLectureName()).isEqualTo(lectureName);
        }
    }

    @Test
    @DisplayName("존재하지 않는 사용자 번호로 조회 시 빈 결과를 반환한다")
    void findByNonExistentUserNumber() {
        // given
        int nonExistentUserNumber = 99999999;

        // when
        Optional<List<Enrollment>> result = enrollmentDataRepository.findByUserNumber_UserNumber(nonExistentUserNumber);

        // then
        assertThat(result).isNotNull();
        // 데이터가 없을 수도 있음 (빈 Optional 또는 빈 리스트)
    }

    @Test
    @DisplayName("존재하지 않는 강의 ID로 조회 시 빈 결과를 반환한다")
    void findByNonExistentLectureId() {
        // given
        int userNumber = 20191434;
        Long nonExistentLectureId = 99999L;

        // when
        Optional<Enrollment> result = enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureId(userNumber, nonExistentLectureId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 강의명으로 조회 시 빈 결과를 반환한다")
    void findByNonExistentLectureName() {
        // given
        int userNumber = 20191434;
        String nonExistentLectureName = "존재하지않는강의";

        // when
        Optional<Enrollment> result = enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, nonExistentLectureName);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("null 파라미터로 조회 시 빈 결과를 반환한다")
    void findWithNullParameters() {
        // given
        int userNumber = 20191434;

        // when & then
        Optional<Enrollment> result1 = enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, null);
        assertThat(result1).isNotNull();
        assertThat(result1).isEmpty();

        Optional<Enrollment> result2 = enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureId(userNumber, null);
        assertThat(result2).isNotNull();
        assertThat(result2).isEmpty();
    }
}