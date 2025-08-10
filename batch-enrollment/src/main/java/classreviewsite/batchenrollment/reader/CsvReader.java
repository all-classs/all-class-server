package classreviewsite.batchenrollment.reader;

import classreviewsite.batchenrollment.config.data.EnrollmentCsv;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CsvReader {

    private String ENCODING = "UTF-8";

    @Bean
    public FlatFileItemReader<EnrollmentCsv> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<EnrollmentCsv>()
                .name("FlatFileItemReader")
                .resource(new ClassPathResource("enrollment.csv"))
                .encoding(ENCODING)
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("completionYear", "semester", "completionType", "lectureId", "courseName", "section", "credits", "professorName", "studentNumber", "studentName")
                .targetType(EnrollmentCsv.class)
                .build();
    }
}