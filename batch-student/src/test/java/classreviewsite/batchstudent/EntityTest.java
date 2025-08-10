package classreviewsite.batchstudent;

import classreviewsite.batchstudent.config.data.Student;
import classreviewsite.batchstudent.writer.CsvToEntityConverter;
import org.classreviewsite.user.infrastructure.Authority;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityTest {

    @Test
    @DisplayName("chunk가 파라미터를 전달하면, User 객체를 반환한다.")
    void csvToUser() {
        Student student = new Student();
        String password = "1234";
        String nickName = "nickname";
        Authority authority = Authority.toEntity("STUDENT");

        User user = CsvToEntityConverter.toUser(student, password, nickName, authority);

        assertThat(user).isNotNull();
        assertThat(user.getClass()).isEqualTo(User.class);
    }
}
