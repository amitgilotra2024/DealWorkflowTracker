package com.app.dealworkflowtracker.messaging;

import com.app.dealworkflowtracker.config.KafkaConfig;
import com.app.dealworkflowtracker.dto.DealEventDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class DealEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @CircuitBreaker(name = "kafkaService", fallbackMethod = "publishDealEventFallback")
    @Retry(name = "kafkaService")
    public void publishDealEvent(DealEventDto event) {
        log.info("Publishing Kafka event for Deal ID: {}", event.getDealId());

        try {
            // .get() forces a synchronous wait so Resilience4j catches broker failures
            var result = kafkaTemplate.send(KafkaConfig.DEAL_EVENTS_TOPIC, String.valueOf(event.getDealId()), event).get();
            log.info("Successfully published event [Type: {}] for Deal ID: {} at offset: {}",
                    event.getEventType(), event.getDealId(), result.getRecordMetadata().offset());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while sending event to Kafka", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to deliver message to Kafka broker", e.getCause());
        }
    }

    /**
     * Fallback method triggered when retries are exhausted or circuit breaker is OPEN
     */
    public void publishDealEventFallback(DealEventDto event, Throwable ex) {
        log.error("KAFKA FALLBACK: Failed to publish event [Type: {}] for Deal ID: {}. Circuit status or error: {}",
                event.getEventType(), event.getDealId(), ex.getMessage());

        // Add offline handling logic here (e.g., save to DB audit log, outbox pattern table, or memory queue)
    }
}