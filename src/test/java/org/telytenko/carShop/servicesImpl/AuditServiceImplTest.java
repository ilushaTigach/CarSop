package test.java.org.telytenko.carShop.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AuditServiceImplTest {
    private AuditServiceImpl auditService;

    @BeforeEach
    public void setUp() {
        auditService = new AuditServiceImpl();
    }

    @Test
    public void testLogAction() {
        auditService.logAction("user1", "login");
        List<String> logs = auditService.getLogs();
        assertFalse(logs.isEmpty());
        assertTrue(logs.get(0).contains("User: user1 - Action: login"));
    }

    @Test
    public void testGetLogs() {
        auditService.logAction("user1", "login");
        auditService.logAction("user2", "logout");
        List<String> logs = auditService.getLogs();
        assertEquals(2, logs.size());
        assertTrue(logs.get(0).contains("User: user1 - Action: login"));
        assertTrue(logs.get(1).contains("User: user2 - Action: logout"));
    }

    @Test
    public void testExportLogs() throws IOException {
        auditService.logAction("user1", "login");
        auditService.logAction("user2", "logout");
        String filePath = "test_logs.txt";
        auditService.exportLogs(filePath);

        List<String> fileContent = Files.readAllLines(Paths.get(filePath));
        assertEquals(2, fileContent.size());
        assertTrue(fileContent.get(0).contains("User: user1 - Action: login"));
        assertTrue(fileContent.get(1).contains("User: user2 - Action: logout"));

        new File(filePath).delete();
    }
}