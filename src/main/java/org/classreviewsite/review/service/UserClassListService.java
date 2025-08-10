package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.handler.exception.ClassNotFoundException;
import org.classreviewsite.handler.exception.NoPermissionReviewException;
import org.classreviewsite.handler.exception.UserNotFoundException;
import org.classreviewsite.review.controller.data.Response.MyPageStudentResponse;
import org.classreviewsite.review.controller.data.Response.UserClassListResponse;
import org.classreviewsite.review.infrastructure.UserClassListDataRepository;
import org.classreviewsite.lecture.data.Lecture;
import org.classreviewsite.lecture.service.LectureService;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.lecture.infrastructure.UserClassList;
import org.classreviewsite.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserClassListService {


    private final UserClassListDataRepository userClassListDataRepository;
    private final UserService userService;
    private final LectureService lectureService;


    @Transactional
    public void save(UserClassList userClassList){
        userClassListDataRepository.save(userClassList);
    }

    @Transactional(readOnly = true)
    public List<UserClassListResponse> findClassForSemester(int userNumber){

        List<UserClassList> list = userClassListDataRepository.findByUserNumberWithClassAndUser(userNumber);
        if(list.isEmpty()){
            throw new UserNotFoundException("해당 학생이 수강한 강의는 없습니다.");
        }

        return list.stream().map(UserClassListResponse::from).toList();

    }

    @Transactional(readOnly = true)
    public List<MyPageStudentResponse> myPageWithStudent(int userNumber){
        User user = userService.findById(Long.valueOf(userNumber));

        List<UserClassList> list = userClassListDataRepository.findByUserNumber(user).orElseThrow(() -> new ClassNotFoundException("해당 학생이 수강한 강의가 없습니다."));

        return MyPageStudentResponse.from(list);
    }

    @Transactional(readOnly = true)
    public UserClassList findByUserNumber(int userNumber, String lectureName){
        User user = userService.findById(Long.valueOf(userNumber));
        Lecture lecture = lectureService.findByLectureName(lectureName);
        UserClassList list = userClassListDataRepository.findByUserNumberAndLecture(user, lecture).orElseThrow(() -> new NoPermissionReviewException(""));
        System.out.println(list.getUserNumber());
        return list;
    }

}