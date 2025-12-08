package org.classreviewsite.repository;

import org.classreviewsite.domain.user.User;
import org.classreviewsite.domain.user.UserDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class UserDataRepositoryTest {

    @Autowired private UserDataRepository userDataRepository;

    @Test
    @DisplayName("userNumber를 전달하면, User 객체를 반환한다.")
    void findByUserNumber() {
        int userNumber = 20191434;
        User savedUser = User.builder().userNumber(userNumber).build();
        userDataRepository.save(savedUser);

        User user = userDataRepository.findById(userNumber).get();

        assertThat(user).isNotNull();

    }

}
