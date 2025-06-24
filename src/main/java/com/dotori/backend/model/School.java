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
}
