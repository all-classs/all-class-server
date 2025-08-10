package classreviewsite.batchlecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "classreviewsite.batchlecture",
        "org.classreviewsite.lecture.infrastructure",
})
@EnableJpaRepositories(basePackages = {
        "org.classreviewsite.lecture.infrastructure"
})
@EntityScan(basePackages = {
        "org.classreviewsite.lecture.infrastructure",
        "org.classreviewsite.user.infrastructure",
})public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

}
