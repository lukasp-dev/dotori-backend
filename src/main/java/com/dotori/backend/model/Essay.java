package com.dotori.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "essays")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Essay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "school_id")
    private Integer schoolId;

    @Column(name = "school_name")
    private String schoolName;

    private String topic;

    private Integer year;

    private Boolean required;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "choice_count")
    private Integer choiceCount;

    @Column(name = "limit_type")
    private String limitType;

    @Column(name = "limit_count_min")
    private Integer limitCountMin;

    @Column(name = "limit_count_max")
    private Integer limitCountMax;

    private String details;
}
