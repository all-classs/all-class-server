package org.classreviewsite.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Student? 로 변경 고민중.
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "Users")
public class User {

    @Id
    @Column(nullable = false, length = 8, unique = true)
    private int userNumber;

    @Column(nullable = false, length = 255, unique = false)
    private String password;

    @Column(nullable = false, length = 45, unique = false)
    private String userName;

    @Column(nullable = false, length = 45, unique = false)
    private String department;

    @Column(nullable = false, length = 200, unique = true)
    private String nickname;

    @ManyToMany
    @JoinTable(
            name = "userAuthority",
            joinColumns = {@JoinColumn(name = "userNumber", referencedColumnName = "userNumber")},
            inverseJoinColumns = {@JoinColumn(name = "authority", referencedColumnName = "authority")}
    )
    private Set<Authority> authorities;
}








