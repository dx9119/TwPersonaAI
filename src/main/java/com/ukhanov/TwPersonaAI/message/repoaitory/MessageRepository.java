package com.ukhanov.TwPersonaAI.message.repoaitory;

import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.model.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    // 10 последних НЕ отвеченных сообщений
    List<MessageEntity> findTop10ByAnsweredFalseOrderByTimestampDesc();

    // 10 последних НЕ отвеченных сообщений по источнику
    List<MessageEntity> findTop10ByAnsweredFalseAndSourceOrderByTimestampDesc(MessageSource source);

    // Универсальный метод с лимитом
    List<MessageEntity> findTopByAnsweredFalseOrderByTimestampDesc(); // если нужен лимит — лучше через Pageable

    @Query(value = """
        SELECT * FROM messages
        WHERE answered = 0
        ORDER BY timestamp DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<MessageEntity> findLastUnchecked(int limit);

    @Query(value = """
    SELECT * FROM messages
    WHERE name = :name
    ORDER BY timestamp DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<MessageEntity> findLastMessagesByUser(String name, int limit);

    @Query("""
    SELECT m FROM MessageEntity m
    WHERE m.timestamp BETWEEN :from AND :to
    ORDER BY m.timestamp DESC
    """)
    List<MessageEntity> findMessagesBetweenDates(LocalDateTime from, LocalDateTime to);


    List<MessageEntity> findTop5ByNameOrderByTimestampDesc(String name);

}
