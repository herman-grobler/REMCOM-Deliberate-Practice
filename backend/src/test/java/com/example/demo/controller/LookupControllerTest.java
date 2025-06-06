package com.example.demo.controller;

import com.example.demo.service.LookupService;
import com.example.demo.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class LookupControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private LookupService lookupService;

        @Autowired
        private JwtService jwtService;

        private String validToken;

        @BeforeEach
        void setUp() {
                // Generate a valid JWT token for testing
                validToken = jwtService.generateToken("test@example.com", com.example.demo.model.Role.USER);
        }

        @Test
        public void shouldReturnLookupResultForValidDriverId() throws Exception {
                String driverId = "A1234567";
                when(lookupService.lookup(driverId)).thenReturn(
                                new com.example.demo.model.LookupResult(
                                                driverId,
                                                "DRIVER",
                                                java.util.Collections.emptyList(),
                                                java.util.Collections.emptyList()));

                mockMvc.perform(get("/api/lookup")
                                .header("Authorization", "Bearer " + validToken)
                                .param("identifier", driverId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.identifier").value(driverId))
                                .andExpect(jsonPath("$.type").value("DRIVER"));
        }

        @Test
        public void shouldReturnLookupResultForValidVehicleRegistration() throws Exception {
                String registration = "AB12 CDE";
                when(lookupService.lookup(registration)).thenReturn(
                                new com.example.demo.model.LookupResult(
                                                registration,
                                                "VEHICLE",
                                                java.util.Collections.emptyList(),
                                                java.util.Collections.emptyList()));

                mockMvc.perform(get("/api/lookup")
                                .header("Authorization", "Bearer " + validToken)
                                .param("identifier", registration))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.identifier").value(registration))
                                .andExpect(jsonPath("$.type").value("VEHICLE"));
        }

        @Test
        public void shouldReturnBadRequestForInvalidIdentifier() throws Exception {
                String invalidId = "invalid";
                when(lookupService.lookup(invalidId))
                                .thenThrow(new IllegalArgumentException("Invalid identifier format"));

                mockMvc.perform(get("/api/lookup")
                                .header("Authorization", "Bearer " + validToken)
                                .param("identifier", invalidId))
                                .andExpect(status().isBadRequest());
        }

        @Test
        public void shouldReturnBadRequestForEmptyIdentifier() throws Exception {
                when(lookupService.lookup(""))
                                .thenThrow(new IllegalArgumentException("Identifier cannot be empty"));

                mockMvc.perform(get("/api/lookup")
                                .header("Authorization", "Bearer " + validToken)
                                .param("identifier", ""))
                                .andExpect(status().isBadRequest());
        }

}