package com.cnl.istd_sts.features.users.domain;

import com.cnl.istd_sts.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {
    @Id
    @UuidGenerator
    @Column(unique = true, nullable = false, updatable = false, length = 36)
    private String UUID;

    @Column(nullable = false, length = 128, unique = true)
    private String email;

    @Column(nullable = false, length = 528, unique = true)
    private String phone_number;

    @Column(nullable = false, length = 528)
    private String password; /// NOTE: Encrypted password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date createdAt;
}
