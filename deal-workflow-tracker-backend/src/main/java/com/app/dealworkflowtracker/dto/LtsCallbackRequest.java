package com.app.dealworkflowtracker.dto;

import lombok.Data;

@Data
public class LtsCallbackRequest {
    private Long dealCardId;
    private String ltsDealId;
    private String status;        // "SUCCESS" or "FAILED"
    private String failureReason; // Optional details if status is FAILED
}