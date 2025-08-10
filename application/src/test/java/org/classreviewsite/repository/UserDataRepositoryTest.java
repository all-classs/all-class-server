package org.classreviewsite.repository;

import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserDataRepositoryTest {

    @Autowired private UserDataRepository userDataRepository;

    @Test
    @DisplayName("userNumber를 전달하면, User 객체를 반환한다.")
    void findByUserNumber() {
        Long userNumber = 20191434L;

        User user = userDataRepository.findById(userNumber).get();

        assertThat(user).isNotNull();

    }

}
