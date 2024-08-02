package org.telytenko.carShop.services;

import java.util.List;

public interface AuditService {
    void logAction(String username, String action);
    List<String> getLogs();
    void exportLogs(String filePath);
}
