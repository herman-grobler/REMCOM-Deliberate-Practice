package com.example.demo.controller;

import com.example.demo.model.EnforcementAction;
import com.example.demo.service.EnforcementService;
import com.example.demo.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class EnforcementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnforcementService enforcementService;

    @Autowired
    private JwtService jwtService;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        // Generate tokens for different roles
        adminToken = jwtService.generateToken("admin@example.com", com.example.demo.model.Role.ADMIN);
        userToken = jwtService.generateToken("user@example.com", com.example.demo.model.Role.USER);
    }

    @Test
    public void shouldEnforceWarrantWithAdminRole() throws Exception {
        String identifier = "A1234567";
        EnforcementAction mockAction = new EnforcementAction(
                "action1",
                "WARRANT",
                identifier,
                "admin@example.com",
                java.time.LocalDateTime.now(),
                "ENFORCED",
                "Test notes");

        when(enforcementService.enforceWarrant(anyString(), anyString(), anyString()))
                .thenReturn(mockAction);

        mockMvc.perform(post("/api/enforce/warrant")
                .header("Authorization", "Bearer " + adminToken)
                .param("identifier", identifier)
                .param("notes", "Test notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("WARRANT"))
                .andExpect(jsonPath("$.status").value("ENFORCED"));
    }

    @Test
    public void shouldEnforceFineWithAdminRole() throws Exception {
        String identifier = "AB12 CDE";
        EnforcementAction mockAction = new EnforcementAction(
                "action2",
                "FINE",
                identifier,
                "admin@example.com",
                java.time.LocalDateTime.now(),
                "ENFORCED",
                "Test notes");

        when(enforcementService.enforceFine(anyString(), anyString(), anyString()))
                .thenReturn(mockAction);

        mockMvc.perform(post("/api/enforce/fine")
                .header("Authorization", "Bearer " + adminToken)
                .param("identifier", identifier)
                .param("notes", "Test notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("FINE"))
                .andExpect(jsonPath("$.status").value("ENFORCED"));
    }

    @Test
    public void shouldRejectEnforcementWithUserRole() throws Exception {
        mockMvc.perform(post("/api/enforce/warrant")
                .header("Authorization", "Bearer " + userToken)
                .param("identifier", "A1234567"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/enforce/fine")
                .header("Authorization", "Bearer " + userToken)
                .param("identifier", "AB12 CDE"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldGetEnforcementHistory() throws Exception {
        String identifier = "A1234567";
        List<EnforcementAction> mockHistory = Arrays.asList(
                new EnforcementAction(
                        "action1",
                        "WARRANT",
                        identifier,
                        "admin@example.com",
                        java.time.LocalDateTime.now(),
                        "ENFORCED",
                        "Test notes"));

        when(enforcementService.getEnforcementHistory(identifier))
                .thenReturn(mockHistory);

        mockMvc.perform(get("/api/enforce/history/" + identifier)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("WARRANT"))
                .andExpect(jsonPath("$[0].status").value("ENFORCED"));
    }

    @Test
    public void shouldRejectHistoryAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/api/enforce/history/A1234567")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
}