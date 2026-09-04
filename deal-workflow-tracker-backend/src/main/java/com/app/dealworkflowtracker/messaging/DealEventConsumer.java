package com.app.dealworkflowtracker.messaging;

import com.app.dealworkflowtracker.config.KafkaConfig;
import com.app.dealworkflowtracker.dto.DealEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DealEventConsumer {

    @KafkaListener(topics = KafkaConfig.DEAL_EVENTS_TOPIC, groupId = "deal-tracker-group")
    public void consumeDealEvent(DealEventDto event) {
        log.info("Received Kafka Event -> EventType: {}, Deal ID: {}, Status: {}, TriggeredBy: {}",
                event.getEventType(), event.getDealId(), event.getStatus(), event.getTriggeredBy());

        // Place downstream operations here (e.g., notification alerts, audit logging, analytics)
    }
}