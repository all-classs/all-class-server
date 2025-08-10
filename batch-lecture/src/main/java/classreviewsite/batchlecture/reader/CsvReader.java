package classreviewsite.batchlecture.reader;

import classreviewsite.batchlecture.config.data.ClassCsv;
import classreviewsite.batchlecture.config.data.LectureCsv;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CsvReader {

    private String ENCODING = "UTF-8";

    @Bean
    public FlatFileItemReader<LectureCsv> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<LectureCsv>()
                .name("FlatFileItemReader")
                .resource(new ClassPathResource("lectures.csv"))
                .encoding(ENCODING)
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("grade", "term", "department", "course", "lectureNumber", "name")
                .targetType(LectureCsv.class)
                .build();
    }
}