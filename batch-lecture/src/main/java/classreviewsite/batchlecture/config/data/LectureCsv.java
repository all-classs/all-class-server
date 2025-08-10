package classreviewsite.batchlecture.config.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class LectureCsv {
    private String grade;
    private String term; // 학기
    private String department;
    private String course; // 이수 구분
    private String lectureNumber;
    private String name; // 교과목명
}
