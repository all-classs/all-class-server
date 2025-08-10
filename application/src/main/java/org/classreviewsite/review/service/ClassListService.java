package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.lecture.infrastructure.ImageUrl;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.service.EnrollmentService;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Response.ClassListResponse;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassListService {

    private final LectureDataService lectureDataService;
    private final ImageUrlService imageUrlService;
    private final EnrollmentService enrollmentService;

    @Transactional(readOnly = true)
    public List<ClassListResponse> get(String university){
            List<Lecture> list = lectureDataService.findByUniversity(university);
            if(list.isEmpty()){
                throw new NoSuchElementException("해당 학교의 강의가 존재하지 않습니다.");
            }
            return list.stream().map(ClassListResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail detail(Long lectureId){
        Lecture lecture = lectureDataService.findByLectureId(lectureId);
        ImageUrl image = imageUrlService.findById(1L);
        return ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail.from(lecture, image.getImageUrl());
    }

}
