package com.example.demo.service;

import com.example.demo.model.LookupResult;
import com.example.demo.model.Notice;
import com.example.demo.model.Warrant;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class LookupService {
    private static final Pattern DRIVER_ID_PATTERN = Pattern.compile("^[A-Z]\\d{7}$");
    private static final Pattern VEHICLE_REG_PATTERN = Pattern.compile("^[A-Z]{2}\\d{2}\\s[A-Z]{3}$");

    public LookupResult lookup(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new IllegalArgumentException("Identifier cannot be empty");
        }

        identifier = identifier.trim().toUpperCase();

        if (DRIVER_ID_PATTERN.matcher(identifier).matches()) {
            return lookupDriver(identifier);
        } else if (VEHICLE_REG_PATTERN.matcher(identifier).matches()) {
            return lookupVehicle(identifier);
        } else {
            throw new IllegalArgumentException("Invalid identifier format");
        }
    }

    private LookupResult lookupDriver(String driverId) {
        List<Notice> notices = new ArrayList<>();
        List<Warrant> warrants = new ArrayList<>();

        // Mock data for driver
        notices.add(new Notice(
                "N001",
                "SPEEDING",
                "Exceeding speed limit by 20mph",
                LocalDateTime.now().minusDays(5),
                "ACTIVE"));

        warrants.add(new Warrant(
                "W001",
                "ARREST",
                "Failure to appear in court",
                LocalDateTime.now().minusMonths(1),
                "ACTIVE",
                "Metropolitan Police"));

        return new LookupResult(driverId, "DRIVER", notices, warrants);
    }

    private LookupResult lookupVehicle(String registration) {
        List<Notice> notices = new ArrayList<>();
        List<Warrant> warrants = new ArrayList<>();

        // Mock data for vehicle
        notices.add(new Notice(
                "N002",
                "TAX",
                "Vehicle tax expired",
                LocalDateTime.now().minusDays(10),
                "ACTIVE"));

        notices.add(new Notice(
                "N003",
                "MOT",
                "MOT certificate expired",
                LocalDateTime.now().minusDays(15),
                "ACTIVE"));

        return new LookupResult(registration, "VEHICLE", notices, warrants);
    }
}