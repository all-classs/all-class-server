package org.classreviewsite.lecture.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.handler.exception.*;
import org.classreviewsite.lecture.controller.data.response.EnrollmentResponse;
import org.classreviewsite.lecture.infrastructure.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentDataService {

        private final EnrollmentDataRepository enrollmentDataRepository;

        @Transactional(readOnly = true)
        public List<EnrollmentResponse> findClassForSemester(int userNumber){
            List<Enrollment> list = enrollmentDataRepository.findByUserNumber_UserNumber(userNumber).orElseThrow(() -> new EnrollmentNotFoundException("해당 학생의 수강 정보가 없습니다."));
            if(list.isEmpty()){
                throw new UserNotFoundException("해당 학생이 수강한 강의는 없습니다.");
            }

            return list.stream().map(EnrollmentResponse::from).toList();

        }

        @Transactional(readOnly = true)
        public Enrollment findByUserNumber(int userNumber, String lectureName){
            Enrollment list = enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName).orElseThrow(() -> new NoPermissionReviewException(""));
            log.info("{}", list.getUserNumber());
            return list;
        }

}
