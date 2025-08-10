package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.handler.exception.LectureNotFoundException;
import org.classreviewsite.review.controller.data.Response.ClassListResponse;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.classreviewsite.review.controller.data.Response.RecommendClassResponse;
import org.classreviewsite.review.infrastructure.ClassListDataRepository;
import org.classreviewsite.lecture.infrastructure.ClassList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassListService {

    private final ClassListDataRepository classListDataRepository;

    @Transactional(readOnly = true)
    public ClassList findById(Long id){
        return classListDataRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 클래스가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<ClassListResponse> getByUniversity(String university){
            List<ClassList> list = classListDataRepository.findClassListByUniversity(university);
            if(list.isEmpty()){
                throw new NoSuchElementException("해당 학교의 강의가 존재하지 않습니다.");
            }
            return list.stream().map(ClassListResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail detail(Long lectureId){
        ClassList classList = classListDataRepository.findByLectureIdWithProfessorName(lectureId).orElseThrow(() -> new LectureNotFoundException("해당 강의가 없습니다."));
        return ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail.from(classList);
    }

    @Transactional(readOnly = true)
    public List<RecommendClassResponse> getRecommend(String university){
        List<RecommendClassResponse> list = classListDataRepository.findClassListByUniversityOrderByRandAndLimitThree(university);
        return list;
    }

}
