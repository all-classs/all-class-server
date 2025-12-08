package org.classreviewsite.service;

import org.classreviewsite.handler.exception.LectureNotFoundException;
import org.classreviewsite.domain.lecture.ImageUrl;
import org.classreviewsite.review.service.ImageUrlService;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.classreviewsite.review.service.ClassListAndDetailService;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureType;
import org.classreviewsite.domain.lecture.StarRating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ClassListAndDetailServiceTest {

    @InjectMocks
    ClassListAndDetailService classListAndDetailService;
    @Mock LectureDataService lectureDataService;
    @Mock ImageUrlService imageUrlService;

    @Nested
    @DisplayName("수강후기 상세 조회 테스트")
    class classDetail {
        @Test
        @DisplayName("수강후기 상세 조회 시, 강의 및 교수 정보를 반환한다")
        void success() {
            // given
            ImageUrl imageUrl = new ImageUrl(1L, "이미지명", "imageurl");
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(), "학과명", "학교명", "교수명", LectureType.교양선택, 0L);

            given(lectureDataService.findByLectureId(1L)).willReturn(lecture);
            given(imageUrlService.findById(1L)).willReturn(imageUrl);

            // when
            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListAndDetailService.detail(1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getLectureName()).isEqualTo("강의명");
            assertThat(result.getProfessor()).isEqualTo("교수명");
        }

        @Test
        @DisplayName("수강후기 상세 조회 시, 존재하지 않는 강의 ID일 경우 예외를 반환한다")
        void notExistIDException() {
            // given
            Long id = 999L;
            given(lectureDataService.findByLectureId(id)).willThrow(new LectureNotFoundException("존재하지 않는 강의입니다."));

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.detail(id))
                    .isInstanceOf(LectureNotFoundException.class)
                    .hasMessage("존재하지 않는 강의입니다.");
        }

        @Test
        @DisplayName("교수 정보가 null인 경우 Null-safe하게 처리한다")
        void notExistProfessor() {
            // given
            ImageUrl imageUrl = new ImageUrl(1L, "이미지명", "imageurl");
            Lecture lecture = new Lecture(1L, "강의명", StarRating.createRatingBuilder(), "학과명", "학교명", "교수명", LectureType.교양선택, 0L);

            given(lectureDataService.findByLectureId(1L)).willReturn(lecture);
            given(imageUrlService.findById(1L)).willReturn(imageUrl);

            // when
            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListAndDetailService.detail(1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getProfessor()).isNotNull();
        }

        @Test
        @DisplayName("반환된 데이터가 DTO 스펙에 맞게 매핑된다")
        void dtoSpec() {
            // given
            ImageUrl imageUrl = new ImageUrl(1L, "이미지명", "imageurl");
            Lecture lecture = new Lecture(12345L, "강의명", StarRating.createRatingBuilder(), "학과명", "학교명", "교수명", LectureType.교양선택, 0L);

            given(lectureDataService.findByLectureId(1L)).willReturn(lecture);
            given(imageUrlService.findById(1L)).willReturn(imageUrl);

            // when
            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = classListAndDetailService.detail(1L);

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