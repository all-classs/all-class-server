package classreviewsite.batchdummy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import classreviewsite.batchdummy.service.DummyDataService;
import org.classreviewsite.security.jwt.JwtTokenProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication(exclude = {BatchAutoConfiguration.class})
@ComponentScan(basePackages = {
    "classreviewsite.batchdummy",
    "org.classreviewsite.lecture.service",
    "org.classreviewsite.user.service",
    "org.classreviewsite.common"
})
@EntityScan(basePackages = {
    "org.classreviewsite.lecture.infrastructure",
    "org.classreviewsite.review.infrastructure",
    "org.classreviewsite.user.infrastructure"
})
@EnableJpaRepositories(basePackages = {
    "org.classreviewsite.lecture.infrastructure",
    "org.classreviewsite.review.infrastructure",
    "org.classreviewsite.user.infrastructure"
})
@RequiredArgsConstructor
public class BatchApplication implements ApplicationRunner {

    private final DummyDataService dummyDataService;

    public static void main(String[] args) {
        // SpringApplication.run 대신 ConfigurableApplicationContext 사용하여 자동 종료 구현
        ConfigurableApplicationContext context = SpringApplication.run(BatchApplication.class, args);
    }

        @Override
    public void run(ApplicationArguments args) throws Exception {
        /* ==================== 더미데이터 주입 로직 (주석 해제하여 활성화) ==================== */
        
        log.info("======== 더미 수강후기 데이터 주입 시작 ========");
        
        try {
            // 데이터베이스 준비 상태 확인
            if (!dummyDataService.waitForDatabaseReady()) {
                log.warn("데이터베이스가 준비되지 않았습니다. 더미데이터 주입을 건너뜁니다.");
                exitApplication(1);
                return;
            }
            
            // 더미데이터가 이미 있는지 확인
            if (dummyDataService.isDummyDataAlreadyExists()) {
                log.info("더미데이터가 이미 존재합니다. 주입을 건너뜁니다.");
                exitApplication(0);
                return;
            }
            
            // 더미데이터 주입 실행
            dummyDataService.loadDummyReviews();
            log.info("======== 더미 수강후기 데이터 주입 완료 ========");
            
            // 성공적으로 완료되면 애플리케이션 종료
            exitApplication(0);
            
        } catch (Exception e) {
            log.error("더미데이터 주입 중 오류 발생: {}", e.getMessage(), e);
            log.info("첫 부팅 시에는 정상적인 현상일 수 있습니다. 다음 재시작 시 다시 시도됩니다.");
            // 오류 발생 시 애플리케이션 종료 (exit code 1)
            exitApplication(1);
        }
        
        /* ==================== 더미데이터 주입 로직 끝 ==================== */
    }

    /**
     * 애플리케이션 종료
     */
    private void exitApplication(int exitCode) {
        log.info("배치 작업 완료. 애플리케이션을 종료합니다. (exit code: {})", exitCode);
        System.exit(exitCode);
    }
}