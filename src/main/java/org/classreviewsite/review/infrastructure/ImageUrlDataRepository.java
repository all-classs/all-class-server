package org.classreviewsite.review.infrastructure;

import org.classreviewsite.lecture.infrastructure.ImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ImageUrlDataRepository extends JpaRepository<ImageUrl, Long> {

    List<ImageUrl> findByImageNameOrImageName(String imageName, String imageName2);

}
