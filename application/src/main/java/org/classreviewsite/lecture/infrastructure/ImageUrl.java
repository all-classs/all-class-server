package org.classreviewsite.lecture.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "ImageUrl")
public class ImageUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageNumber;

    @Column(nullable = true, length = 255, unique = false)
    private String imageName;

    @Column(nullable = true, length = 255, unique = false)
    private String imageUrl;

}
