package com.dotori.backend.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EssayResponseDTO {
    private List<EssayDTO> individualEssays;
    private List<EssayGroupDTO> groupEssays;
}
