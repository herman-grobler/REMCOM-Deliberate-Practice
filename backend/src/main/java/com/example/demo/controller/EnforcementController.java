package com.example.demo.controller;

import com.example.demo.model.EnforcementAction;
import com.example.demo.service.EnforcementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enforce")
public class EnforcementController {
    private final EnforcementService enforcementService;

    public EnforcementController(EnforcementService enforcementService) {
        this.enforcementService = enforcementService;
    }

    @PostMapping("/warrant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnforcementAction> enforceWarrant(
            @RequestParam String identifier,
            @RequestParam(required = false) String notes,
            @AuthenticationPrincipal String userId) {
        try {
            EnforcementAction action = enforcementService.enforceWarrant(identifier, userId, notes);
            return ResponseEntity.ok(action);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/fine")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnforcementAction> enforceFine(
            @RequestParam String identifier,
            @RequestParam(required = false) String notes,
            @AuthenticationPrincipal String userId) {
        try {
            EnforcementAction action = enforcementService.enforceFine(identifier, userId, notes);
            return ResponseEntity.ok(action);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/history/{identifier}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EnforcementAction>> getEnforcementHistory(@PathVariable String identifier) {
        List<EnforcementAction> history = enforcementService.getEnforcementHistory(identifier);
        return ResponseEntity.ok(history);
    }
}