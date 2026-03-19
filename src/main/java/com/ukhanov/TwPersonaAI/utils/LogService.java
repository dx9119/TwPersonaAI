package com.ukhanov.TwPersonaAI.utils;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LogService {

    private final Path logFile = Path.of("twpersona.log");
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogService() {
        try {
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать лог-файл", e);
        }
    }

    public synchronized void write(String tag, String result) {
        String timestamp = LocalDateTime.now().format(formatter);
        String line = String.format("[%s] [%s] %s%n", timestamp, tag, result);

        try (FileWriter writer = new FileWriter(logFile.toFile(), true)) {
            writer.write(line);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в лог-файл", e);
        }
    }
}

