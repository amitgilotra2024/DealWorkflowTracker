package com.app.dealworkflowtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaRetryConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> template) {
        // Initial delay: 1000ms, Multiplier: 2.0 (1s, 2s, 4s...)
        ExponentialBackOff backOff = new ExponentialBackOff(1000L, 2.0);
        backOff.setMaxInterval(10000L); // Max delay cap
        backOff.setMaxElapsedTime(15000L); // Stops retrying after 15 total seconds

        // Recoverer forwards failed messages to <topic>.DLT after max attempts
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);

        return new DefaultErrorHandler(recoverer, backOff);
    }
}