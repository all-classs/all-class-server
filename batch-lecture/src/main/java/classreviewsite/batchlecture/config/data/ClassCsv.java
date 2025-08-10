package classreviewsite.batchlecture.config.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ClassCsv {
    private Long year;
    private String term; // 학기
    private Long classNumber;
    private String course; // 이수 구분
    private String name;
    private String professor;
}
