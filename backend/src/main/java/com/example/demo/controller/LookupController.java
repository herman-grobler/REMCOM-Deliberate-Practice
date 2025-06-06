package com.example.demo.controller;

import com.example.demo.model.LookupResult;
import com.example.demo.service.LookupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lookup")
public class LookupController {
    private final LookupService lookupService;

    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping
    public ResponseEntity<LookupResult> lookup(@RequestParam String identifier) {
        try {
            LookupResult result = lookupService.lookup(identifier);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}