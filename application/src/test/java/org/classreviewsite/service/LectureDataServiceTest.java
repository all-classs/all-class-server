package org.classreviewsite.service;

import org.classreviewsite.handler.exception.LectureNotFoundException;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureDataRepository;
import org.classreviewsite.domain.lecture.LectureType;
import org.classreviewsite.domain.lecture.StarRating;
import org.classreviewsite.lecture.service.LectureDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LectureDataServiceTest {

    @InjectMocks
    LectureDataService lectureDataService;
    @Mock LectureDataRepository lectureDataRepository;

    @Nested
    @DisplayName("강의명으로 강의 조회 테스트")
    class findByLectureNameTest {
        
        @Test
        @DisplayName("존재하는 강의명으로 조회 시, 강의를 반환한다")
        void findByLectureNameSuccess() {
            // given
            String lectureName = "자바프로그래밍";
            Lecture expectedLecture = new Lecture(1L, lectureName, StarRating.createRatingBuilder(),
                                                "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수, 0L);
            
            given(lectureDataRepository.findByLectureName(lectureName)).willReturn(Optional.of(expectedLecture));
            
            // when
            Lecture result = lectureDataService.findByLectureName(lectureName);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result.getLectureName()).isEqualTo(lectureName);
            assertThat(result.getProfessor()).isEqualTo("김교수");
            assertThat(result.getDepartment()).isEqualTo("소프트웨어학과");
            assertThat(result.getUniversity()).isEqualTo("한국대학교");
            assertThat(result.getLectureType()).isEqualTo(LectureType.전공필수);
        }
        
        @Test
        @DisplayName("존재하지 않는 강의명으로 조회 시, LectureNotFoundException을 발생한다")
        void findByLectureNameNotFound() {
            // given
            String lectureName = "존재하지않는강의";
            given(lectureDataRepository.findByLectureName(lectureName)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> lectureDataService.findByLectureName(lectureName))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("존재하지 않는 강의입니다.");
        }
        
        @Test
        @DisplayName("null 강의명으로 조회 시, LectureNotFoundException을 발생한다")
        void findByLectureNameWithNull() {
            // given
            String lectureName = null;
            given(lectureDataRepository.findByLectureName(lectureName)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> lectureDataService.findByLectureName(lectureName))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("존재하지 않는 강의입니다.");
        }
    }

    @Nested
    @DisplayName("강의 ID로 강의 조회 테스트")
    class findByIdTest {
        
        @Test
        @DisplayName("존재하는 ID로 조회 시, 강의를 반환한다")
        void findByIdSuccess() {
            // given
            Long lectureId = 1L;
            Lecture expectedLecture = new Lecture(lectureId, "자바프로그래밍", StarRating.createRatingBuilder(),
                                                "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수, 0L);
            
            given(lectureDataRepository.findById(lectureId)).willReturn(Optional.of(expectedLecture));
            
            // when
            Lecture result = lectureDataService.findById(lectureId);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result.getLectureId()).isEqualTo(lectureId);
            assertThat(result.getLectureName()).isEqualTo("자바프로그래밍");
        }
        
        @Test
        @DisplayName("존재하지 않는 ID로 조회 시, LectureNotFoundException을 발생한다")
        void findByIdNotFound() {
            // given
            Long lectureId = 999L;
            given(lectureDataRepository.findById(lectureId)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> lectureDataService.findById(lectureId))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("존재하지 않는 강의입니다.");
        }
        
        @Test
        @DisplayName("null ID로 조회 시, LectureNotFoundException을 발생한다")
        void findByIdWithNull() {
            // given
            Long lectureId = null;
            given(lectureDataRepository.findById(lectureId)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> lectureDataService.findById(lectureId))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("존재하지 않는 강의입니다.");
        }
    }

    @Nested
    @DisplayName("대학명으로 강의 목록 조회 테스트")
    class findByUniversityTest {
        
        @Test
        @DisplayName("존재하는 대학명으로 조회 시, 강의 목록을 반환한다")
        void findByUniversitySuccess() {
            // given
            String university = "한국대학교";
            Lecture lecture1 = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", university, "김교수", LectureType.전공필수, 0L);
            Lecture lecture2 = new Lecture(2L, "데이터베이스", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", university, "이교수", LectureType.전공선택, 0L);
            
            List<Lecture> expectedLectures = Arrays.asList(lecture1, lecture2);
            
            given(lectureDataRepository.findByUniversity(university)).willReturn(expectedLectures);
            
            // when
            List<Lecture> result = lectureDataService.findByUniversity(university);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getUniversity()).isEqualTo(university);
            assertThat(result.get(1).getUniversity()).isEqualTo(university);
        }
        
        @Test
        @DisplayName("강의가 없는 대학명으로 조회 시, LectureNotFoundException을 발생한다")
        void findByUniversityNotFound() {
            // given
            String university = "존재하지않는대학교";
            given(lectureDataRepository.findByUniversity(university)).willReturn(List.of());
            
            // when & then
            assertThatThrownBy(() -> lectureDataService.findByUniversity(university))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("해당 대학의 강의가 존재하지 않습니다.");
        }
        
        @Test
        @DisplayName("null 대학명으로 조회 시, LectureNotFoundException을 발생한다")
        void findByUniversityWithNull() {
            // given
            String university = null;
            given(lectureDataRepository.findByUniversity(university)).willReturn(List.of());
            
            // when & then
            assertThatThrownBy(() -> lectureDataService.findByUniversity(university))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("해당 대학의 강의가 존재하지 않습니다.");
        }
        
        @Test
        @DisplayName("여러 학과의 강의가 있는 대학 조회 시, 모든 강의를 반환한다")
        void findByUniversityMultipleDepartments() {
            // given
            String university = "한국대학교";
            Lecture lecture1 = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", university, "김교수", LectureType.전공필수, 0L);
            Lecture lecture2 = new Lecture(2L, "수학", StarRating.createRatingBuilder(),
                                         "수학과", university, "박교수", LectureType.교양필수, 0L);
            
            List<Lecture> expectedLectures = Arrays.asList(lecture1, lecture2);
            
            given(lectureDataRepository.findByUniversity(university)).willReturn(expectedLectures);
            
            // when
            List<Lecture> result = lectureDataService.findByUniversity(university);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getDepartment()).isEqualTo("소프트웨어학과");
            assertThat(result.get(1).getDepartment()).isEqualTo("수학과");
        }
    }

    @Nested
    @DisplayName("강의 ID로 강의 조회 테스트 (findByLectureId)")
    class findByLectureIdTest {
        
        @Test
        @DisplayName("존재하는 강의 ID로 조회 시, 강의를 반환한다")
        void findByLectureIdSuccess() {
            // given
            Long lectureId = 12345L;
            Lecture expectedLecture = new Lecture(lectureId, "알고리즘", StarRating.createRatingBuilder(),
                                                "소프트웨어학과", "한국대학교", "최교수", LectureType.전공선택, 0L);
            
            given(lectureDataRepository.findByLectureId(lectureId)).willReturn(Optional.of(expectedLecture));
            
            // when
            Lecture result = lectureDataService.findByLectureId(lectureId);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result.getLectureId()).isEqualTo(lectureId);
            assertThat(result.getLectureName()).isEqualTo("알고리즘");
            assertThat(result.getProfessor()).isEqualTo("최교수");
        }
        
        @Test
        @DisplayName("존재하지 않는 강의 ID로 조회 시, LectureNotFoundException을 발생한다")
        void findByLectureIdNotFound() {
            // given
            Long lectureId = 99999L;
            given(lectureDataRepository.findByLectureId(lectureId)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> lectureDataService.findByLectureId(lectureId))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("존재하지 않는 강의입니다.");
        }
        
        @Test
        @DisplayName("음수 강의 ID로 조회 시, LectureNotFoundException을 발생한다")
        void findByLectureIdWithNegative() {
            // given
            Long lectureId = -1L;
            given(lectureDataRepository.findByLectureId(lectureId)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> lectureDataService.findByLectureId(lectureId))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("존재하지 않는 강의입니다.");
        }
    }
}