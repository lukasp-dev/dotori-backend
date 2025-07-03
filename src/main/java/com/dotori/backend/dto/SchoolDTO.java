package com.dotori.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolDTO {
    private Long id;
    private String schoolName;
    private int ranking;
    private String urlParameter;
}
