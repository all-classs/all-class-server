package org.classreviewsite.repository;

import org.classreviewsite.handler.exception.LectureNotFoundException;
import org.classreviewsite.domain.lecture.LectureDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest

public class LectureDataRepositoryTest {
    @Autowired private LectureDataRepository lectureDataRepository;

    @Test
    @DisplayName("존재하지않는 교과목 번호를 주면, 강의를 반환한다.")
    void findByLectureId() {
        Long lectureId = 99998L;

        org.junit.jupiter.api.Assertions.assertThrows(LectureNotFoundException.class, () -> lectureDataRepository.findByLectureId(lectureId).orElseThrow(() -> new LectureNotFoundException("강의가 존재하지 않습니다.")));
    }
}
