package classreviewsite.batchlecture.writer;

import classreviewsite.batchlecture.config.data.ClassCsv;
import classreviewsite.batchlecture.config.data.LectureCsv;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureDataRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomItemWriter implements ItemWriter<LectureCsv> {

    private final LectureDataRepository lectureRepository;

    public CustomItemWriter(LectureDataRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    int i = 0;

    @Override
    public void write(Chunk<? extends LectureCsv> chunk) {
        log.info("===== writer 작동 ====");
        chunk.getItems().forEach(item -> {
            lectureRepository.save(CsvToEntityConverter.toLecture(item, "동서대학교", ""));
            log.info(item.getName());
            i++;
        });
    }



}
