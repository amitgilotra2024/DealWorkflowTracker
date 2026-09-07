package com.app.dealworkflowtracker.service;

import com.app.dealworkflowtracker.dto.LtsCallbackRequest;

public interface LtsCallbackService {
    void processCallback(LtsCallbackRequest callbackRequest);
}