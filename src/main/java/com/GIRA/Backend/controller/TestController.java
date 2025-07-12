package com.GIRA.Backend.controller;

import com.GIRA.Backend.DTO.common.ApiResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Test controller for basic functionality verification.
 * 
 * @author Mohamed yahya jabrane
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("service", "GIRA Backend");
        healthData.put("version", "1.0.0");
        
        return ApiResponse.success("Service is healthy", healthData);
    }

    @GetMapping("/public")
    public ApiResponse<String> publicEndpoint() {
        return ApiResponse.success("This is a public endpoint", "Hello from GIRA!");
    }
} 