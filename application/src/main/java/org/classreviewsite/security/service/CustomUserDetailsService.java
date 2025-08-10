package org.classreviewsite.security.service;

import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDataRepository userRepository;


    public CustomUserDetailsService(UserDataRepository userRepository) {
        this.userRepository = userRepository;
    }

    // username으로 검사 변경 메소드
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username){
        return userRepository.findOneWithAuthoritiesByUserNumber(Integer.parseInt(username))
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    // user 객체 생성 메소드
    private org.springframework.security.core.userdetails.User createUser(String username, User user){

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(String.valueOf(user.getUserNumber()),
                user.getPassword(),
                grantedAuthorities);
    }
}
