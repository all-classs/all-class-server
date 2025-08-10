package org.classreviewsite.service;

import org.classreviewsite.handler.exception.LectureNotFoundException;
import org.classreviewsite.lecture.infrastructure.ClassList;
import org.classreviewsite.lecture.infrastructure.ImageUrl;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.classreviewsite.review.infrastructure.ClassListDataRepository;
import org.classreviewsite.review.service.ClassListService;
import org.classreviewsite.lecture.data.Lecture;
import org.classreviewsite.lecture.data.LectureType;
import org.classreviewsite.lecture.data.Professor;
import org.classreviewsite.lecture.infrastructure.LectureDataRepository;
import org.classreviewsite.review.infrastructure.StarRating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ClassListServiceTest {

    @InjectMocks ClassListService classListService;
    @Mock ClassListDataRepository classListDataRepository;
    @Mock LectureDataRepository lectureDataRepository;

    @Nested
    @DisplayName("수강후기 상세 조회 테스트")
    class classDetail {
        @Test
        @DisplayName("수강후기 상세 조회 시, 강의 및 교수 정보를 반환한다")
        void success() {
            ImageUrl imageUrl = new ImageUrl(12345L, "이미지명", "imageurl");
            Professor professor = new Professor(12345L, "교수명", "학과명", imageUrl, 12345L);
            Lecture lecture = new Lecture(12345L, "강의명", StarRating.createRatingBuilder(), "학과명", "학교명", "교수명", LectureType.교양선택);
            ClassList classes = new ClassList(12345L, "강의명", lecture, professor, imageUrl, true);

            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail data =
                    new ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail(1L, "강의명", 0.0, 0.0, 0L, "학과명", "학교명", LectureType.교양선택, "교수명", "소개", "linkurl", 123456L, "linkurl");

            given(classListDataRepository.findByLectureIdWithProfessorName(1L)).willReturn(Optional.of(classes));

            // when
            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListService.detail(1L);

            // then
            assertThat(result.getLectureName()).isEqualTo(data.getLectureName());
        }

        @Test
        @DisplayName("수강후기 상세 조회 시, 존재하지 않는 강의 ID일 경우 예외를 반환한다")
        void notExistIDException() {
            Long id = 1L;

            org.junit.jupiter.api.Assertions.assertThrows(LectureNotFoundException.class, () -> {
                classListService.detail(id);
            });
        }

        @Test
        @DisplayName("이미지 URL이 존재하지 않아도 기본값을 반환한다")
        void notExistImageURL() {
            ImageUrl imageUrl = new ImageUrl(12345L, "이미지명", null);
            Professor professor = new Professor(12345L, "교수명", "학과명", imageUrl, 12345L);
            Lecture lecture = new Lecture(12345L, "강의명", StarRating.createRatingBuilder(), "학과명", "학교명", "교수명" , LectureType.교양선택);
            ClassList classes = new ClassList(12345L, "강의명", lecture, professor, imageUrl, true);

            given(classListDataRepository.findByLectureIdWithProfessorName(1L)).willReturn(Optional.of(classes));

            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListService.detail(1L);

            assertThat(result.getIcon()).isNotNull();
        }

        @Test
        @DisplayName("교수 정보가 null인 경우 Null-safe하게 처리한다")
        void notExistProfessor() {
            ImageUrl imageUrl = new ImageUrl(12345L, "이미지명", null);
            Lecture lecture = new Lecture(12345L, "강의명", StarRating.createRatingBuilder() , "학과명", "학교명", "교수명" , LectureType.교양선택);
            ClassList classes = new ClassList(12345L, "강의명", lecture, null, imageUrl, true);

            given(classListDataRepository.findByLectureIdWithProfessorName(1L)).willReturn(Optional.of(classes));

            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListService.detail(1L);

            assertThat(result.getProfessor()).isNotNull();
        }

        @Test
        @DisplayName("반환된 데이터가 DTO 스펙에 맞게 매핑된다")
        void dtoSpec() {
            // given
            ImageUrl imageUrl = new ImageUrl(12345L, "이미지명", "imageurl");
            Professor professor = new Professor(12345L, "교수명", "학과명", imageUrl, 12345L);
            Lecture lecture = new Lecture(12345L, "강의명",StarRating.createRatingBuilder(), "학과명", "학교명", "교수명" ,LectureType.교양선택);
            ClassList classes = new ClassList(1L, "강의명", lecture, professor, imageUrl, true);

            given(classListDataRepository.findByLectureIdWithProfessorName(1L)).willReturn(Optional.of(classes));

            // when
            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListService.detail(1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getLectureId()).isEqualTo(12345L);
            assertThat(result.getLectureName()).isEqualTo("강의명");
            assertThat(result.getDepartment()).isEqualTo("학과명");
            assertThat(result.getUniversity()).isEqualTo("학교명");
            assertThat(result.getLectureType()).isEqualTo(LectureType.교양선택);
            assertThat(result.getProfessor()).isEqualTo("교수명");
            assertThat(result.getProfileImage()).isEqualTo("imageurl");
        }

    }

}