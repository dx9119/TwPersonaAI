package com.ukhanov.TwPersonaAI.provider.repository;

import com.ukhanov.TwPersonaAI.provider.model.AiAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiAnswerRepository extends JpaRepository<AiAnswerEntity, Long> {

    // последние 15 ответов от ИИ, начиная с самого нового
    List<AiAnswerEntity> findTop15ByOrderByTimestampDesc();
}

