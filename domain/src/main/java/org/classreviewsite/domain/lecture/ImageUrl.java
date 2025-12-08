package org.classreviewsite.domain.lecture;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @see @삭제 예정
 * ImageUrl이 사용되지 않고 있다. 삭제 권장.
 */
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








