package classreviewsite.batchenrollment.config;

import classreviewsite.batchenrollment.config.data.EnrollmentCsv;
import classreviewsite.batchenrollment.writer.CustomItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.lecture.infrastructure.Enrollment;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ItemJobConfiguration {
    public static final int CHUNK_SIZE = 100;
    public static final String ENCODING = "UTF-8";
    public static final String JPA_ITEM_WRITER_JOB = "JPA_ITEM_READER_JOB";

    private final FlatFileItemReader flatFileItemReader;
    private final CustomItemWriter customItemWriter;


    public ItemJobConfiguration(FlatFileItemReader flatFileItemReader, CustomItemWriter customItemWriter) {
        this.flatFileItemReader = flatFileItemReader;
        this.customItemWriter = customItemWriter;
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, JpaContext jpaContext){
        log.info("========== 수강한 강의 정보 읽기 step ===========");
        return new StepBuilder("step", jobRepository)
                .<EnrollmentCsv, Enrollment>chunk(CHUNK_SIZE, transactionManager)
                .reader(flatFileItemReader)
                .writer(customItemWriter)
                .build();
    }


    @Bean
    public Job job(Step step, JobRepository jobRepository) {
        log.info("======== 수강한 강의 정보 읽기 job =========");
        return new JobBuilder(JPA_ITEM_WRITER_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }


}