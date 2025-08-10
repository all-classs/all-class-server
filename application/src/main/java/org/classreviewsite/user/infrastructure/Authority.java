package org.classreviewsite.user.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "authority")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @Column(name = "authority" , length = 50)
    private String authority;

    public static Authority toEntity(String type){
        return Authority.builder()
                .authority(type)
                .build();
    }
}
