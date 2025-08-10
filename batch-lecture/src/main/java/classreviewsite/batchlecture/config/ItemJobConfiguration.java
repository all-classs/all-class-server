package classreviewsite.batchlecture.config;

import classreviewsite.batchlecture.config.data.ClassCsv;
import classreviewsite.batchlecture.config.data.LectureCsv;
import classreviewsite.batchlecture.writer.CustomItemWriter;
import classreviewsite.batchlecture.writer.ProfessorNameWriter;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.lecture.infrastructure.Lecture;
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
    public static final String JPA_ITEM_READER_JOB = "JPA_ITEM_READER_JOB";

    private final FlatFileItemReader flatFileItemReader;
    private final FlatFileItemReader flatFileItemReader2;
    private final CustomItemWriter customItemWriter;
    private final ProfessorNameWriter professorNameWriter;


    public ItemJobConfiguration(FlatFileItemReader flatFileItemReader, FlatFileItemReader flatFileItemReader2, CustomItemWriter customItemWriter, ProfessorNameWriter professorNameWriter) {
        this.flatFileItemReader = flatFileItemReader;
        this.flatFileItemReader2 = flatFileItemReader2;
        this.customItemWriter = customItemWriter;
        this.professorNameWriter = professorNameWriter;
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, JpaContext jpaContext){
        log.info("========== 강의 정보 읽기 step ===========");
        return new StepBuilder("step", jobRepository)
                .<LectureCsv, Lecture>chunk(CHUNK_SIZE, transactionManager)
                .reader(flatFileItemReader)
                .writer(customItemWriter)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager, JpaContext jpaContext){
        log.info("========== 교수명 업데이트 ===========");
        return new StepBuilder("step2", jobRepository)
                .<ClassCsv, ClassCsv>chunk(CHUNK_SIZE, transactionManager)
                .reader(flatFileItemReader2)
                .writer(professorNameWriter)
                .build();
    }

    @Bean
    public Job job(Step step, Step step2, JobRepository jobRepository) {
        log.info("======== 강의 정보 읽기 job =========");
        return new JobBuilder(JPA_ITEM_READER_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .next(step2)
                .build();
    }


}