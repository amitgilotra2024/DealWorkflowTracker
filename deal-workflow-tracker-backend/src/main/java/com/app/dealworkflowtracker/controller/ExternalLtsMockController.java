package com.app.dealworkflowtracker.controller;

import com.app.dealworkflowtracker.dto.LtsCallbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/external/lts")
@RequiredArgsConstructor
public class ExternalLtsMockController {

    private final RestTemplate restTemplate;

    @PostMapping("/create-deal-async")
    public ResponseEntity<Map<String, Object>> createDealAsync(@RequestBody Map<String, Object> request) {
        Long dealCardId = Long.valueOf(request.get("dealCardId").toString());
        String trackingId = "LTS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Simulate background processing and asynchronous callback execution
        CompletableFuture.runAsync(() -> triggerMockCallback(dealCardId, trackingId));

        // Return 202 ACCEPTED immediately (< 100ms) to beat the 29s API Gateway timeout
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "status", "ACKNOWLEDGED",
                "message", "Request received. Callback will be sent upon completion.",
                "trackingId", trackingId
        ));
    }

    private void triggerMockCallback(Long dealCardId, String ltsDealId) {
        try {
            // Simulate 3-second delay for external processing
            Thread.sleep(3000);

            // Construct callback payload matching LtsCallbackRequest DTO
            LtsCallbackRequest callbackPayload = new LtsCallbackRequest();
            callbackPayload.setDealCardId(dealCardId);
            callbackPayload.setLtsDealId(ltsDealId);
            callbackPayload.setStatus("SUCCESS");

            // Post callback back to your application's webhook listener
            restTemplate.postForEntity(
                    "http://localhost:8080/api/external/lts/callback",
                    callbackPayload,
                    Void.class
            );

            log.info("Successfully triggered asynchronous LTS callback for DealCard ID: {}", dealCardId);
        } catch (Exception e) {
            log.error("Failed to execute mock LTS callback for DealCard ID: {}", dealCardId, e);
        }
    }
}