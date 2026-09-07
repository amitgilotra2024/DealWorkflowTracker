package com.app.dealworkflowtracker.controller;

import com.app.dealworkflowtracker.dto.LtsCallbackRequest;
import com.app.dealworkflowtracker.service.LtsCallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/lts") // Leading slash required
@RequiredArgsConstructor
public class LtsCallbackController {

    private final LtsCallbackService ltsCallbackService;

    @PostMapping("/callback") // Leading slash required
    public ResponseEntity<Void> handleLtsCallback(@RequestBody LtsCallbackRequest request) {
        ltsCallbackService.processCallback(request);
        return ResponseEntity.ok().build();
    }
}