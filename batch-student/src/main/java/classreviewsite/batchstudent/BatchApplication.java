package classreviewsite.batchstudent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "classreviewsite.batchstudent",
        "org.classreviewsite.user.infrastructure",
})
@EnableJpaRepositories(basePackages = {
        "org.classreviewsite.user.infrastructure"
})
@EntityScan(basePackages = {
        "org.classreviewsite.user.infrastructure"
})
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

}
