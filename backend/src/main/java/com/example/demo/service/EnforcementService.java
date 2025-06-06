package com.example.demo.service;

import com.example.demo.model.EnforcementAction;
import com.example.demo.model.LookupResult;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EnforcementService {
    private static final Logger logger = LoggerFactory.getLogger(EnforcementService.class);
    private final LookupService lookupService;
    private final ConcurrentHashMap<String, List<EnforcementAction>> enforcementHistory = new ConcurrentHashMap<>();

    public EnforcementService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public EnforcementAction enforceWarrant(String identifier, String userId, String notes) {
        logger.info("Enforcing warrant for identifier: {} by user: {}", identifier, userId);

        LookupResult lookupResult = lookupService.lookup(identifier);
        EnforcementAction action = new EnforcementAction(
                UUID.randomUUID().toString(),
                "WARRANT",
                identifier,
                userId,
                LocalDateTime.now(),
                "ENFORCED",
                notes);

        enforcementHistory.computeIfAbsent(identifier, k -> new ArrayList<>()).add(action);
        return action;
    }

    public EnforcementAction enforceFine(String identifier, String userId, String notes) {
        logger.info("Enforcing fine for identifier: {} by user: {}", identifier, userId);

        LookupResult lookupResult = lookupService.lookup(identifier);
        EnforcementAction action = new EnforcementAction(
                UUID.randomUUID().toString(),
                "FINE",
                identifier,
                userId,
                LocalDateTime.now(),
                "ENFORCED",
                notes);

        enforcementHistory.computeIfAbsent(identifier, k -> new ArrayList<>()).add(action);
        return action;
    }

    public List<EnforcementAction> getEnforcementHistory(String identifier) {
        return enforcementHistory.getOrDefault(identifier, new ArrayList<>());
    }
}