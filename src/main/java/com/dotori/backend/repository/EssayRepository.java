package com.dotori.backend.repository;

import com.dotori.backend.model.Essay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EssayRepository extends JpaRepository<Essay, Integer> {
    List<Essay> findBySchoolId(Integer schoolId);
}
