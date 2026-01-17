package com.cnl.istd_sts.features.teachers.dto;

import com.cnl.istd_sts.features.users.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherEntity {

    @Id
    @Column(name = "user_id", length = 36)
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonBackReference
    private UserEntity user;

    @Column(name = "subject_discipline", length = 68, nullable = false)
    private String subjectDiscipline;

    @Column(name = "bio_description", length=1000)
    private String bioDescription;

    @Column(name = "price_per_hour", nullable = false)
    private Double pricePerHour;

    @Column(name = "lesson_duration_minutes")
    private Integer lessonDurationMinutes;

    @Column(name = "is_visible_in_search")
    private Boolean isVisibleInSearch = false;
}
