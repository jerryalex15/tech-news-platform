package com.challenge.tech_news_service.kafka;

import com.challenge.tech_news_service.dto.NewsCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsEventProducer {

    private final KafkaTemplate<String, NewsCreatedEvent> kafkaTemplate;

    @Value("${kafka.topics.tech-news}")
    private String topicTechNews;

    public void publier(NewsCreatedEvent event) {
        // La clé = articleId pour garantir l'ordre par article dans une partition
        String key = event.getArticleId().toString();

        CompletableFuture<SendResult<String, NewsCreatedEvent>> future =
                kafkaTemplate.send(topicTechNews, key, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Echec envoi event Kafka pour article [{}] : {}",
                        event.getExternalId(), ex.getMessage());
            } else {
                log.debug("Event publié — topic: {} | partition: {} | offset: {} | article: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        event.getExternalId());
            }
        });
    }
}