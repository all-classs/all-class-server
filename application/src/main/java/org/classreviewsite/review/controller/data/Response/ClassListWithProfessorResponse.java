package org.classreviewsite.review.controller.data.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.classreviewsite.domain.util.NumberFormat;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureType;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class ClassListWithProfessorResponse {

    private Double AverageStarLating;
    private String lectureName;
    private String department;
    private LectureType LectureType;
    private Long ReviewCount;
    private String university;
    private Long lectureId;
    private Double TotalStarLating;
    private String professorName;


    public static List<ClassListWithProfessorResponse> from(List<Lecture> lectures){
        List<ClassListWithProfessorResponse> list = lectures.stream()
                .map(lecture ->
                        ClassListWithProfessorResponse.builder()
                                .professorName(lecture.getProfessor())
                                .lectureName(lecture.getLectureName())
                                .LectureType(lecture.getLectureType())
                                .AverageStarLating(lecture.getStarRating().getAverageRating())
                                .department(lecture.getDepartment())
                                .lectureId(lecture.getLectureId())
                                .ReviewCount(lecture.getStarRating().getReviewCount())
                                .TotalStarLating(lecture.getStarRating().getTotalRating())
                                .university(lecture.getUniversity())
                                .build()

                ).collect(Collectors.toList());

        return list;
    }


    @Data
    @AllArgsConstructor
    @Builder
    public static class ClassListWithProfessorNameInDetail{
        private Long lectureId;
        private String lectureName;
        private Double averageStarLating;
        private Double totalStarLating;
        private Long reviewCount;
        private String department;
        private String university;
        private LectureType lectureType;
        private String professor;
        private String introduction;
        private String profileImage;


        public static ClassListWithProfessorNameInDetail from(Lecture lecture, String imageUrl){

            return ClassListWithProfessorNameInDetail.builder()
                    .averageStarLating(NumberFormat.format(lecture.getStarRating().getAverageRating()))
                    .professor(lecture.getProfessor())
                    .lectureId(lecture.getLectureId())
                    .lectureName(lecture.getLectureName())
                    .lectureType(lecture.getLectureType())
                    .department(lecture.getDepartment())
                    .reviewCount(lecture.getStarRating().getReviewCount())
                    .totalStarLating(lecture.getStarRating().getTotalRating())
                    .university(lecture.getUniversity())
                    .introduction("강의 정보 입니다.")
                    .profileImage(imageUrl)
                    .build();
        }
    }

}
