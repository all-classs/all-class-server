package org.classreviewsite.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.user.infrastructure.Authority;
import org.classreviewsite.handler.exception.UserNotFoundException;
import org.classreviewsite.security.service.AuthService;
import org.classreviewsite.user.controller.data.request.CreateUserRequest;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.handler.exception.UserExistException;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public User findById(Long userNumber){
        return userDataRepository.findById(userNumber).orElseThrow(() -> new UserNotFoundException("해당 학생이 존재하지 않습니다."));
    }

    @Transactional
    public int signUp(CreateUserRequest user){

        validateDuplicateUser(user);
        String password = passwordEncoder.encode(user.getPassword());

        Authority authority = Authority.toEntity(user.getUserType());
        int userNumber =  userDataRepository.save(User.toEntity(user, password, authority)).getUserNumber();

        return userNumber;


    }

    private void validateDuplicateUser(CreateUserRequest user){
       userDataRepository.findByUserNumber(user.getUserNumber())
               .ifPresent(m -> {
            throw new UserExistException("이미 존재하는 학생입니다.");
        });
    }
}
