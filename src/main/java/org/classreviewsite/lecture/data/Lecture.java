package org.classreviewsite.lecture.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.classreviewsite.review.infrastructure.StarRating;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "Lecture")
public class Lecture {

    @Id
    @Column(name = "lectureId" , nullable = false)
    private Long lectureId;

    @Column(nullable = false, length = 45 , unique = false)
    private String lectureName;

    @Column(nullable = false, unique = false)
    private StarRating starRating;

    @Column(nullable = false, length = 45, unique = false)
    private String department;

    @Column(nullable = false, length = 45, unique = false)
    private String university;

    @Enumerated(EnumType.STRING)
    private LectureType lectureType;

    public void addReview(Double starRating) {
        this.starRating.addRating(starRating);
    }

    public void removeReview(Double starRating) {
        this.starRating.removeRating(starRating);
    }

}