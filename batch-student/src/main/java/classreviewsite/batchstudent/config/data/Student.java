package classreviewsite.batchstudent.config.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Student {
    private String department;
    private int studentNumber;
    private String name;
    private String grade;
}
