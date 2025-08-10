package org.classreviewsite.lecture.data;

import jakarta.persistence.*;
import lombok.*;
import org.classreviewsite.lecture.infrastructure.ImageUrl;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "Professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long professorId;

    @Column(nullable = false, length = 45, unique = false)
    private String professorName;

    @Column
    private String department;

}
