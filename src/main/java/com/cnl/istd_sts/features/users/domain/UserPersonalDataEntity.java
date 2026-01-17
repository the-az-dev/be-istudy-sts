package com.cnl.istd_sts.features.users.domain;

import com.cnl.istd_sts.common.managers.AttrEncryptorManager;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "user_personal_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserPersonalDataEntity {

    public UserPersonalDataEntity(String fullName, String city, String telegramChatId) {
        this.fullName = fullName;
        this.city = city;
        this.telegramChatId = telegramChatId;
    }

    @Id
    @Column(name = "user_id", length = 36)
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonBackReference
    private UserEntity user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "city")
    private String city;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;
}
