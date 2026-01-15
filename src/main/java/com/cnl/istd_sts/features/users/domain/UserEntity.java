package com.cnl.istd_sts.features.users.domain;

import com.cnl.istd_sts.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Table(name = "users") // 1. Змінили ім'я таблиці, щоб не конфліктувало з SQL
@Getter
@Setter
@Builder // 2. Дуже зручно для створення об'єктів
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @UuidGenerator
    @Column(name = "uuid", unique = true, nullable = false, updatable = false, length = 36)
    private String uuid;

    @Column(nullable = false, length = 128, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber; // 4. Змінили на camelCase

    @Column(nullable = false, length = 528)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @ToString.Exclude
    private UserPersonalDataEntity personalData;


    public void setPersonalData(UserPersonalDataEntity data) {
        if (data == null) {
            if (this.personalData != null) {
                this.personalData.setUser(null);
            }
        } else {
            data.setUser(this);
        }
        this.personalData = data;
    }
}