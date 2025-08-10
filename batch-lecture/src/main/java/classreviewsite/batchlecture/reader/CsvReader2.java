package classreviewsite.batchlecture.reader;

import classreviewsite.batchlecture.config.data.ClassCsv;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CsvReader2 {
    private String ENCODING = "UTF-8";

    @Bean
    public FlatFileItemReader<ClassCsv> flatFileItemReader2() {
        return new FlatFileItemReaderBuilder<ClassCsv>()
                .name("FlatFileItemReader2")
                .resource(new ClassPathResource("classes.csv"))
                .encoding(ENCODING)
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("year", "term", "classNumber", "course", "name", "professor")
                .targetType(ClassCsv.class)
                .build();
    }
}
