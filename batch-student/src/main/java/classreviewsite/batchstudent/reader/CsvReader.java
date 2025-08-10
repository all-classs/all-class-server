package classreviewsite.batchstudent.reader;

import classreviewsite.batchstudent.config.data.Student;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CsvReader {
    private String ENCODING = "UTF-8";

    @Bean
    public FlatFileItemReader<Student> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<Student>()
                .name("FlatFileItemReader")
                .resource(new ClassPathResource("students.csv"))
                .encoding(ENCODING)
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("department", "studentNumber", "name", "grade")
                .targetType(Student.class)
                .build();
    }
}
