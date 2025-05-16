package com.dotori.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@ToString
public class UploadInfoRequestDTO {
    private String userId;
    private int high_school_completion;
    private Map<String, Integer> general_college_requirement;
    private int alumni;
    private int first;
    private List<String> alumni_school_names;
    private String residency;
    private String state;
    private String country;
    private int sat;
    private int act;
    private double gpa;
    private int volunteering_hours;
    private String english_test_type;
    private int english_test_score;
}
