package org.classreviewsite.lecture.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureDataRepository;
import org.classreviewsite.handler.exception.LectureNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureDataService {

    private final LectureDataRepository lectureDataRepository;

    @Transactional(readOnly = true)
    public Lecture findByLectureName(String lectureName){
        return lectureDataRepository.findByLectureName(lectureName).orElseThrow(() -> new LectureNotFoundException("존재하지 않는 강의입니다."));
    }

    @Transactional(readOnly = true)
    public Lecture findById(Long id){
        return lectureDataRepository.findById(id).orElseThrow(() -> new LectureNotFoundException("존재하지 않는 강의입니다."));
    }

    @Transactional(readOnly = true)
    public List<Lecture> findByUniversity(String university){
        List<Lecture> list = lectureDataRepository.findByUniversity(university);
        if(list.isEmpty()){
            throw new LectureNotFoundException("해당 대학의 강의가 존재하지 않습니다.");
        }
        return list;
    }

    @Transactional(readOnly = true)
    public Lecture findByLectureId(Long lectureId) {
        return lectureDataRepository.findByLectureId(lectureId).orElseThrow(() -> new LectureNotFoundException("존재하지 않는 강의입니다."));
    }
}
