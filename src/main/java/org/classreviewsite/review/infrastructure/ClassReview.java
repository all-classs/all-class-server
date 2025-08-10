package org.classreviewsite.review.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.classreviewsite.common.util.BaseTimeEntity;
import org.classreviewsite.lecture.data.Lecture;
import org.classreviewsite.user.infrastructure.User;

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

    public int like(){
        this.likes += 1;
        return this.likes;
    }

    public int like(int like){
        this.likes = like;
        return this.likes;
    }

    public void update(String postTitle, String postContent, Double starLating, Long important, Long difficulty, Long funny){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.starLating = starLating;
    }

    public static ClassReview create(Lecture lecture, User user, Double starLating, int likes, String postContent, String postTitle){
        return ClassReview.builder()
                .lecId(lecture)
                .userNumber(user)
                .starLating(starLating)
                .likes(likes)
                .postTitle(postTitle)
                .postContent(postContent)
                .build();
    }


}
