package com.app.dealworkflowtracker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String DEAL_EVENTS_TOPIC = "deal-workflow-events";

    @Bean
    public NewTopic dealWorkflowTopic() {
        return TopicBuilder.name(DEAL_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}