package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.domain.lecture.ImageUrl;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Response.ClassListResponse;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see @현재 모호한 부분들
 * 현재 db 중심적으로 구성되어있음. detail 이라는 용어는 db 중심적인 것에서 나온 것이라고 할수있음.
 * 그렇다면, db 중심적이지 않고 어떠하다고 해야하나? 현재 강의 정보를 자세하게 보여주는 데이터들을 주기 위해서는 어떤 상황에서 주어야하나?
 * 강의 하나만의 데이터를 원할때, 전달하는 데이터들이다. 그렇다면 해당 비즈니스 로직이 사용되는 api는 강의 데이터 하나를 원한다는 표현이 적절해보인다.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassListAndDetailService {

    private final LectureDataService lectureDataService;
    private final ImageUrlService imageUrlService;

    @Transactional(readOnly = true)
    public List<ClassListResponse> get(String university){
            List<Lecture> lectures = lectureDataService.findByUniversity(university);
            validateNoLectureUniversity(lectures);

            return lectures.stream().map(ClassListResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail detail(Long lectureId){
        Lecture lecture = lectureDataService.findByLectureId(lectureId);
        ImageUrl image = imageUrlService.findById(1L);
        return ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail.from(lecture, image.getImageUrl());
    }

    private void validateNoLectureUniversity(List<Lecture> lectures) {
        if(lectures.isEmpty()){
            throw new NoSuchElementException("해당 학교의 강의가 존재하지 않습니다.");
        }
    }
}
