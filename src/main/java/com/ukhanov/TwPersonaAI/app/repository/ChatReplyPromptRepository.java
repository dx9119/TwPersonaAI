package com.ukhanov.TwPersonaAI.app.repository;

import com.ukhanov.TwPersonaAI.provider.model.ChatReplyPromptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatReplyPromptRepository extends JpaRepository<ChatReplyPromptEntity, Long> {
}
