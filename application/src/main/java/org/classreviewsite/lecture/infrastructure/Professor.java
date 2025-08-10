package org.classreviewsite.lecture.infrastructure;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
//@Entity
@Table(name = "Professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long professorId;

    @Column(nullable = false, length = 45, unique = false)
    private String professorName;

    @Column
    private String department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileImage", nullable = true)
    private ImageUrl profileImage;

    @Column(nullable = true)
    private Long companyNumber;

    public void updateProfile(ImageUrl image){
        this.profileImage = image;
    }

}
