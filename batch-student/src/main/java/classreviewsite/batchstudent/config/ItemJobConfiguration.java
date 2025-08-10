package classreviewsite.batchstudent.config;

import classreviewsite.batchstudent.config.data.Student;
import classreviewsite.batchstudent.writer.CustomItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ItemJobConfiguration {
    public static final int CHUNK_SIZE = 100;
    public static final String ENCODING = "UTF-8";
    public static final String JPA_ITEM_READER_JOB = "JPA_ITEM_READER_JOB";

    private final FlatFileItemReader flatFileItemReader;
    private final CustomItemWriter customItemWriter;


    public ItemJobConfiguration(FlatFileItemReader flatFileItemReader, CustomItemWriter customItemWriter) {
        this.flatFileItemReader = flatFileItemReader;
        this.customItemWriter = customItemWriter;
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, JpaContext jpaContext){
        log.info("========== 1학년 재학생 명부 읽기 step ===========");
        return new StepBuilder("step", jobRepository)
                .<Student, org.classreviewsite.user.infrastructure.User>chunk(CHUNK_SIZE, transactionManager)
                .reader(flatFileItemReader)
                .writer(customItemWriter)
                .build();
    }


    @Bean
    public Job job(Step step, JobRepository jobRepository) {
        log.info("======== 재학생 명부 읽기 job =========");
        return new JobBuilder(JPA_ITEM_READER_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }


}