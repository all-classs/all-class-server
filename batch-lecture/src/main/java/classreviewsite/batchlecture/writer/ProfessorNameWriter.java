package classreviewsite.batchlecture.writer;

import classreviewsite.batchlecture.config.data.ClassCsv;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureDataRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfessorNameWriter implements ItemWriter<ClassCsv> {

    private final LectureDataRepository lectureDataRepository;

    public ProfessorNameWriter(LectureDataRepository lectureDataRepository) {
        this.lectureDataRepository = lectureDataRepository;
    }

    int i = 0;

    @Override
    public void write(Chunk<? extends ClassCsv> chunk) throws Exception {
        log.info("==== writer2 작동 (건수: {}) ====", chunk.size());
        chunk.getItems().forEach(item -> {
            try {
                Lecture lecture = lectureDataRepository.findByLectureId(item.getClassNumber()).orElse(null);
                if (lecture != null) {
                    log.info("교수명 업데이트: {} -> {}", lecture.getProfessor(), item.getProfessor());
                    lecture.updateProfessorName(item.getProfessor());
                    lectureDataRepository.save(lecture);  // 저장 추가
                    i++;
                } else {
                    log.warn("강의 ID {}를 찾을 수 없습니다.", item.getClassNumber());
                }
            } catch (Exception e) {
                log.error("교수명 업데이트 실패: {}", item, e);
            }
        });
        log.info("총 {}건 처리 완료", i);
    }
}
