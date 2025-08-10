package classreviewsite.batchstudent.writer;

import classreviewsite.batchstudent.config.data.Student;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.user.infrastructure.Authority;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomItemWriter implements ItemWriter<Student> {

    private final UserDataRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomItemWriter(UserDataRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    int i = 0;

    @Override
    public void write(Chunk<? extends Student> chunk) {
        log.info("===== writer 작동 ====");
        chunk.getItems().forEach(item -> {
            Authority authority = Authority.toEntity("STUDENT");
            userRepository.save(
                    CsvToEntityConverter.toUser(item, passwordEncoder.encode("1234"), "testUser"+i, authority)
            );
            log.info(item.getName());
            i++;
        });

    }
}
