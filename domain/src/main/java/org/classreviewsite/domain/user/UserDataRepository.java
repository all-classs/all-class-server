package org.classreviewsite.domain.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserNumber(@Param("userNumber") int userNumber);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUserNumber(int UserNumber);

}








