package com.ukhanov.TwPersonaAI.provider.repository;

import com.ukhanov.TwPersonaAI.provider.model.LlmTaskDone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LlmTaskDoneRepository extends JpaRepository<LlmTaskDone, Long> {

    // Получить задачи за указанный диапазон дат (например, за день)
    Page<LlmTaskDone> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<LlmTaskDone> findByTaskId(String taskId);

}
