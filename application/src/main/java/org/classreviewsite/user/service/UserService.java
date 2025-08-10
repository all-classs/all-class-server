package org.classreviewsite.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.handler.exception.UserNotFoundException;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDataRepository userDataRepository;

    @Transactional(readOnly = true)
    public User get(Long userNumber){
        return userDataRepository.findById(userNumber).orElseThrow(() -> new UserNotFoundException("해당 학생이 존재하지 않습니다."));
    }

}
