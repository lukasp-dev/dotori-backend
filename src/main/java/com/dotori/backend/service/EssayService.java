package com.dotori.backend.service;

import com.dotori.backend.dto.*;
import com.dotori.backend.model.Essay;
import com.dotori.backend.repository.EssayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EssayService {
    private final EssayRepository essayRepository;
    public EssayResponseDTO getEssayBySchoolId(Integer schoolId) {
        List<Essay> allEssays = essayRepository.findBySchoolId(schoolId);

        List<EssayDTO> individualEssays = allEssays.stream()
                .filter(e -> e.getGroupId() == null)
                .map(EssayDTO::fromEntity)
                .collect(Collectors.toList());
        Map<Integer, List<Essay>> grouped = allEssays.stream()
                .filter(e -> e.getGroupId() != null)
                .collect(Collectors.groupingBy(Essay::getGroupId));
        List<EssayGroupDTO> groupEssayDTOs = grouped.entrySet().stream()
                .map(entry -> {
                    List<Essay> groupEssays = entry.getValue();
                    Essay sample = groupEssays.get(0);
                    return new EssayGroupDTO(
                            entry.getKey(),
                            sample.getChoiceCount(),
                            sample.getRequired(),
                            null,
                            groupEssays.stream().map(EssayDTO::fromEntity).collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());
        return new EssayResponseDTO(individualEssays, groupEssayDTOs);
    }
}
