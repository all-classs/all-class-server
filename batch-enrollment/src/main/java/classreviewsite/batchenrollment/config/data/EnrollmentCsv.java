package classreviewsite.batchenrollment.config.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class EnrollmentCsv {
    private Long completionYear; // 년도
    private Long semester; // 학기
    private String completionType; // 이수구분
    private Long lectureId; // 교과목번호
    private String courseName; // 교과목명
    private Long section; // 분반
    private Long credits; // 학점수
    private String professorName; // 담당교수명
    private int studentNumber; // 학번
    private String studentName; // 이름
}
