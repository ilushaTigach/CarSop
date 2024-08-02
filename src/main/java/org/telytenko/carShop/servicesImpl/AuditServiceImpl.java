package org.telytenko.carShop.servicesImpl;

import org.telytenko.carShop.services.AuditService;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuditServiceImpl implements AuditService {
    private List<String> logs = new ArrayList<>();

    @Override
    public void logAction(String username, String action) {
        String logEntry = "User: " + username + " - Action: " + action + " - Timestamp: " + System.currentTimeMillis();
        logs.add(logEntry);
    }

    @Override
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    @Override
    public void exportLogs(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String log : logs) {
                writer.write(log);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось экспортировать журналы в файл: " + filePath, e);
        }
    }
}
