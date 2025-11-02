package com.example.securitydemo.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> status() {
        return Map.of("status", "ok", "message", "Admin endpoint accessible");
    }
}
