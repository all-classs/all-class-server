package org.classreviewsite.domain.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.classreviewsite.domain.util.BaseTimeEntity;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.user.User;

/**
 * @see @변경할것
 * 추후에 네이밍 Review 로 변경
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class ClassReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecId", nullable = false, unique = false)
    private Lecture lecId;

    @Column(nullable = false, length = 255, unique = false)
    private String postTitle;

    @Column(nullable = false, unique = false)
    private Double starLating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNumber" , nullable = false, unique = false)
    private User userNumber;

    @Column(nullable = false, length = 255, unique = false)
    private String postContent;

    @Column(nullable = false)
    private int likes;

    @Version
    private Long version;

    public int like(){
        this.likes += 1;
        return this.likes;
    }

    public int like(int like){
        this.likes = like;
        return this.likes;
    }

    public void update(String postTitle, String postContent, Double starLating){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.starLating = starLating;
    }




}








