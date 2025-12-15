package org.classreviewsite.domain.lecture;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.classreviewsite.domain.util.NumberFormat;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "Lectures")
public class Lecture {

    @Id
    @Column(name = "lectureId" , nullable = false)
    private Long lectureId;

    @Column(nullable = false, length = 45 , unique = false)
    private String lectureName;

    @Embedded
    @Column(nullable = false, unique = false)
    private StarRating starRating;

    @Column(nullable = false, length = 45, unique = false)
    private String department;

    @Column(nullable = false, length = 45, unique = false)
    private String university;

    @Column(nullable = false)
    private String professor;

    @Enumerated(EnumType.STRING)
    private LectureType lectureType;

    public void addStarRating(Double starRating) {
        this.starRating.addRating(starRating);
    }

    public void removeStarRating(Double starRating) {
        this.starRating.removeRating(starRating);
    }

    public void updateStarRating(Double postStar, Double requestStar) {
        removeStarRating(postStar);
        addStarRating(requestStar);
    }

    public void updateProfessorName(String professorName) {
        this.professor = professorName;
    }

    public Double getAverageRating() {
        return NumberFormat.format(this.starRating.getAverageRating());
    }

}








