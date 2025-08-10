package classreviewsite.batchstudent.writer;

import classreviewsite.batchstudent.config.data.Student;
import org.classreviewsite.user.infrastructure.Authority;
import org.classreviewsite.user.infrastructure.User;

import java.util.Collections;

public class CsvToEntityConverter {
    public static User toUser(Student student, String password, String nickName, Authority authority) {
        return User.builder()
                .userName(student.getName())
                .userNumber(student.getStudentNumber())
                .department(student.getDepartment())
                .authorities(Collections.singleton(authority))
                .nickname(nickName)
                .password(password)
                .build();
    }
}
