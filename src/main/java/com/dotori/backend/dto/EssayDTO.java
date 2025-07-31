package com.dotori.backend.dto;

import com.dotori.backend.model.Essay;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EssayDTO {
    private Integer id;
    private Integer schoolId;
    private String schoolName;
    private String topic;
    private Integer year;
    private Boolean required;
    private Integer groupId;
    private Integer choiceCount;
    private String limitType;
    private Integer limitCountMin;
    private Integer limitCountMax;
    private String details;

    public static EssayDTO fromEntity(Essay e) {
        return new EssayDTO(
                e.getId(),
                e.getSchoolId(),
                e.getSchoolName(),
                e.getTopic(),
                e.getYear(),
                e.getRequired(),
                e.getGroupId(),
                e.getChoiceCount(),
                e.getLimitType(),
                e.getLimitCountMin(),
                e.getLimitCountMax(),
                e.getDetails()
        );
    }
}
