package org.classreviewsite.service;

import org.classreviewsite.handler.exception.LectureNotFoundException;
import org.classreviewsite.lecture.infrastructure.ImageUrl;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.classreviewsite.review.service.ClassListService;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.LectureDataRepository;
import org.classreviewsite.lecture.infrastructure.StarRating;
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
    @Mock LectureDataRepository lectureDataRepository;

    @Nested
    @DisplayName("수강후기 상세 조회 테스트")
    class classDetail {
        @Test
        @DisplayName("수강후기 상세 조회 시, 강의 및 교수 정보를 반환한다")
        void success() {
            ImageUrl imageUrl = new ImageUrl(12345L, "이미지명", "imageurl");
            Lecture lecture = new Lecture(12345L, "강의명", StarRating.createRatingBuilder(), "학과명", "학교명", "교수명", LectureType.교양선택);

            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail data =
                    new ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail(1L, "강의명", 0.0, 0.0, 0L, "학과명", "학교명", LectureType.교양선택, "교수명", "소개", imageUrl.getImageUrl());

            given(lectureDataRepository.findByLectureId(1L)).willReturn(Optional.of(lecture));

            // when
            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListService.detail(1L);

            // then
            assertThat(result.getLectureName()).isEqualTo(data.getLectureName());
        }

        @Test
        @DisplayName("수강후기 상세 조회 시, 존재하지 않는 강의 ID일 경우 예외를 반환한다")
        void notExistIDException() {
            Long id = 1L;
//            Lecture expectedLecture = Lecture.builder()
//                            .lectureId(id)
//                            .lectureName("rhrm")
//                            .lectureType(LectureType.교양선택)
//                            .department("소프트웨어학과")
//                            .professor("문미경")
//                            .starRating()
//                            .university()
//                            .build();
//            given(lectureDataRepository.findByLectureId(id)).willReturn(Optional.of())

            org.junit.jupiter.api.Assertions.assertThrows(LectureNotFoundException.class, () -> {
                classListService.detail(id);
            });
        }

        @Test
        @DisplayName("교수 정보가 null인 경우 Null-safe하게 처리한다")
        void notExistProfessor() {
            ImageUrl imageUrl = new ImageUrl(12345L, "이미지명", null);
            Lecture lecture = new Lecture(12345L, "강의명", StarRating.createRatingBuilder() , "학과명", "학교명", "교수명" , LectureType.교양선택);

            given(lectureDataRepository.findByLectureId(1L)).willReturn(Optional.of(lecture));

            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListService.detail(1L);

            assertThat(result.getProfessor()).isNotNull();
        }

        @Test
        @DisplayName("반환된 데이터가 DTO 스펙에 맞게 매핑된다")
        void dtoSpec() {
            // given
            ImageUrl imageUrl = new ImageUrl(12345L, "이미지명", "imageurl");
            Lecture lecture = new Lecture(12345L, "강의명",StarRating.createRatingBuilder(), "학과명", "학교명", "교수명" ,LectureType.교양선택);

            given(lectureDataRepository.findByLectureId(1L)).willReturn(Optional.of(lecture));

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