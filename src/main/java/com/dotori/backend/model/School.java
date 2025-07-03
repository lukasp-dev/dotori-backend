package com.dotori.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Schools")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String schoolName;

    @Column(nullable = false)
    private int ranking;

    @Column(name = "url_parameter", nullable = false)
    private String urlParameter;
}
