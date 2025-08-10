package org.classreviewsite.lecture.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.classreviewsite.lecture.data.Lecture;
import org.classreviewsite.user.infrastructure.User;

/**
 * deprecated
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "UserClassList")
public class UserClassList {

    // 수강한 고유 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CompletionNumber;

    // 이수 구분
    @Column(nullable = true, length = 45, unique = false)
    private String CompletionType;

    // 이수연도
    @Column(nullable = true, length = 45, unique = false)
    private String CompletionYear;

    // 이수학년
    @Column(nullable = true, length = 45, unique = false)
    private String grade;

    // 이수학기
    @Column(nullable = true, length = 45, unique = false)
    private String semester;

    // 어떤 학생
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNumber")
    private User userNumber;

    // 어떤 교과목
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Lecture lecture;

    @Column(nullable = true, length = 45, unique = false)
    private String professor;


}
