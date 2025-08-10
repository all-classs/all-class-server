package org.classreviewsite.domain;

import org.classreviewsite.lecture.infrastructure.Enrollment;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EnrollmentTest {

    @Nested
    @DisplayName("Enrollment 엔티티 생성 테스트")
    class createEnrollmentTest {
        
        @Test
        @DisplayName("완전한 수강 정보를 생성한다")
        void createCompleteEnrollment() {
            // given
            Long completionNumber = 1L;
            String completionType = "이수";
            String completionYear = "2023";
            String semester = "1학기";
            
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            String professor = "김교수";
            
            // when
            Enrollment enrollment = new Enrollment(completionNumber, completionType, completionYear, 
                                                 semester, user, lecture, professor);
            
            // then
            assertThat(enrollment).isNotNull();
            assertThat(enrollment.getCompletionNumber()).isEqualTo(completionNumber);
            assertThat(enrollment.getCompletionType()).isEqualTo(completionType);
            assertThat(enrollment.getCompletionYear()).isEqualTo(completionYear);
            assertThat(enrollment.getSemester()).isEqualTo(semester);
            assertThat(enrollment.getUserNumber()).isEqualTo(user);
            assertThat(enrollment.getLecture()).isEqualTo(lecture);
            assertThat(enrollment.getProfessor()).isEqualTo(professor);
        }
        
        @Test
        @DisplayName("기본 생성자로 수강 정보를 생성한다")
        void createEnrollmentWithNoArgsConstructor() {
            // when
            Enrollment enrollment = new Enrollment();
            
            // then
            assertThat(enrollment).isNotNull();
            assertThat(enrollment.getCompletionNumber()).isNull();
            assertThat(enrollment.getCompletionType()).isNull();
            assertThat(enrollment.getCompletionYear()).isNull();
            assertThat(enrollment.getSemester()).isNull();
            assertThat(enrollment.getUserNumber()).isNull();
            assertThat(enrollment.getLecture()).isNull();
            assertThat(enrollment.getProfessor()).isNull();
        }
        
        @Test
        @DisplayName("1학기 수강 정보를 생성한다")
        void createFirstSemesterEnrollment() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            // when
            Enrollment enrollment = new Enrollment(1L, "이수", "2023", "1학기", user, lecture, "김교수");
            
            // then
            assertThat(enrollment.getSemester()).isEqualTo("1학기");
            assertThat(enrollment.getCompletionYear()).isEqualTo("2023");
            assertThat(enrollment.getCompletionType()).isEqualTo("이수");
        }
        
        @Test
        @DisplayName("2학기 수강 정보를 생성한다")
        void createSecondSemesterEnrollment() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "데이터베이스", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "이교수", LectureType.전공선택);
            
            // when
            Enrollment enrollment = new Enrollment(2L, "이수", "2023", "2학기", user, lecture, "이교수");
            
            // then
            assertThat(enrollment.getSemester()).isEqualTo("2학기");
            assertThat(enrollment.getLecture().getLectureName()).isEqualTo("데이터베이스");
            assertThat(enrollment.getProfessor()).isEqualTo("이교수");
        }
        
        @Test
        @DisplayName("여름학기 수강 정보를 생성한다")
        void createSummerSemesterEnrollment() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "영어회화", StarRating.createRatingBuilder(),
                                        "외국어학과", "한국대학교", "존스미스", LectureType.교양선택);
            
            // when
            Enrollment enrollment = new Enrollment(3L, "이수", "2023", "여름학기", user, lecture, "존스미스");
            
            // then
            assertThat(enrollment.getSemester()).isEqualTo("여름학기");
            assertThat(enrollment.getLecture().getLectureType()).isEqualTo(LectureType.교양선택);
        }
        
        @Test
        @DisplayName("다양한 이수 구분으로 수강 정보를 생성한다")
        void createEnrollmentWithVariousCompletionTypes() {
            // given
            String[] completionTypes = {"이수", "미이수", "재수강", "취소"};
            
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            for (int i = 0; i < completionTypes.length; i++) {
                // when
                Enrollment enrollment = new Enrollment((long) (i + 1), completionTypes[i], 
                                                     "2023", "1학기", user, lecture, "김교수");
                
                // then
                assertThat(enrollment.getCompletionType()).isEqualTo(completionTypes[i]);
                assertThat(enrollment.getCompletionNumber()).isEqualTo((long) (i + 1));
            }
        }
        
        @Test
        @DisplayName("여러 연도의 수강 정보를 생성한다")
        void createEnrollmentForMultipleYears() {
            // given
            String[] years = {"2020", "2021", "2022", "2023", "2024"};
            
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            for (int i = 0; i < years.length; i++) {
                // when
                Enrollment enrollment = new Enrollment((long) (i + 1), "이수", years[i], 
                                                     "1학기", user, lecture, "김교수");
                
                // then
                assertThat(enrollment.getCompletionYear()).isEqualTo(years[i]);
            }
        }
        
        @Test
        @DisplayName("null 값들로 수강 정보를 생성한다")
        void createEnrollmentWithNullValues() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            // when
            Enrollment enrollment = new Enrollment(1L, null, null, null, user, lecture, null);
            
            // then
            assertThat(enrollment.getCompletionType()).isNull();
            assertThat(enrollment.getCompletionYear()).isNull();
            assertThat(enrollment.getSemester()).isNull();
            assertThat(enrollment.getProfessor()).isNull();
            assertThat(enrollment.getUserNumber()).isNotNull();
            assertThat(enrollment.getLecture()).isNotNull();
        }
        
        @Test
        @DisplayName("동일한 학생이 여러 강의를 수강한다")
        void createMultipleEnrollmentsForSameStudent() {
            // given
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture1 = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            Lecture lecture2 = new Lecture(2L, "데이터베이스", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", "한국대학교", "이교수", LectureType.전공선택);
            
            // when
            Enrollment enrollment1 = new Enrollment(1L, "이수", "2023", "1학기", user, lecture1, "김교수");
            Enrollment enrollment2 = new Enrollment(2L, "이수", "2023", "1학기", user, lecture2, "이교수");
            
            // then
            assertThat(enrollment1.getUserNumber()).isEqualTo(enrollment2.getUserNumber());
            assertThat(enrollment1.getLecture()).isNotEqualTo(enrollment2.getLecture());
            assertThat(enrollment1.getProfessor()).isNotEqualTo(enrollment2.getProfessor());
        }
    }
}