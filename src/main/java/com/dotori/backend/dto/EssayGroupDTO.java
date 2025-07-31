package com.dotori.backend.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EssayGroupDTO {
    private Integer groupId;
    private Integer choiceCount;
    private Boolean required;
    private String note;
    private List<EssayDTO> essays;
}
