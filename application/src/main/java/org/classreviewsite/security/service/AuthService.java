package org.classreviewsite.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    public String validateUserAuthority(Collection<? extends GrantedAuthority> authority){
        String match = String.valueOf(authority.stream().collect(Collectors.toCollection(ArrayList::new)).get(0));

        log.info("match: {}",
            authority.stream().collect(Collectors.toCollection(ArrayList::new)).get(0)
        );

        return match;
    }

}