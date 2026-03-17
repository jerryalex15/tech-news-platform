package com.challenge.tech_news_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.tech-news}")
    private String topicTechNews;

    @Bean
    public NewTopic topicTechNews() {
        return TopicBuilder.name(topicTechNews)
                .partitions(3)
                .replicas(1)
                .build();
    }
}