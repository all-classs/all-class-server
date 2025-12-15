package org.classreviewsite.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureDataRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByLectureName(@Param("lectureName") String lectureName);
    List<Lecture> findByUniversity(@Param("university") String university);
    Optional<Lecture> findByLectureId(Long lectureId);
    
    /**
     * 별점 추가 - Atomic Update
     * 단일 쿼리로 reviewCount 증가, totalRating 증가, averageRating 재계산을 원자적으로 수행
     * 
     * @param lectureId 강의 ID
     * @param rating 추가할 별점
     * @return 업데이트된 행 수
     */
    @Modifying
    @Query("UPDATE Lecture l SET " +
           "l.starRating.reviewCount = l.starRating.reviewCount + 1, " +
           "l.starRating.totalRating = l.starRating.totalRating + :rating, " +
           "l.starRating.averageRating = (l.starRating.totalRating + :rating) / (l.starRating.reviewCount + 1) " +
           "WHERE l.lectureId = :lectureId")
    int addStarRatingAtomic(@Param("lectureId") Long lectureId, 
                            @Param("rating") Double rating);
    
    /**
     * 별점 제거 - Atomic Update
     * 단일 쿼리로 reviewCount 감소, totalRating 감소, averageRating 재계산을 원자적으로 수행
     * 
     * @param lectureId 강의 ID
     * @param rating 제거할 별점
     * @return 업데이트된 행 수
     */
    @Modifying
    @Query("UPDATE Lecture l SET " +
           "l.starRating.reviewCount = CASE WHEN l.starRating.reviewCount > 0 THEN l.starRating.reviewCount - 1 ELSE 0 END, " +
           "l.starRating.totalRating = CASE WHEN l.starRating.totalRating >= :rating THEN l.starRating.totalRating - :rating ELSE 0.0 END, " +
           "l.starRating.averageRating = CASE WHEN (l.starRating.reviewCount - 1) > 0 THEN (l.starRating.totalRating - :rating) / (l.starRating.reviewCount - 1) ELSE 0.0 END " +
           "WHERE l.lectureId = :lectureId AND l.starRating.reviewCount > 0")
    int removeStarRatingAtomic(@Param("lectureId") Long lectureId, 
                               @Param("rating") Double rating);
    
    /**
     * 별점 수정 - Atomic Update
     * 기존 별점 제거 후 새 별점 추가를 단일 쿼리로 수행
     * 
     * @param lectureId 강의 ID
     * @param oldRating 기존 별점
     * @param newRating 새 별점
     * @return 업데이트된 행 수
     */
    @Modifying
    @Query("UPDATE Lecture l SET " +
           "l.starRating.totalRating = l.starRating.totalRating - :oldRating + :newRating, " +
           "l.starRating.averageRating = (l.starRating.totalRating - :oldRating + :newRating) / l.starRating.reviewCount " +
           "WHERE l.lectureId = :lectureId AND l.starRating.reviewCount > 0")
    int updateStarRatingAtomic(@Param("lectureId") Long lectureId,
                               @Param("oldRating") Double oldRating,
                               @Param("newRating") Double newRating);
}









