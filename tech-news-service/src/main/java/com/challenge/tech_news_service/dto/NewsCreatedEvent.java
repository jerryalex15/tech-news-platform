package com.challenge.tech_news_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event publié dans le topic Kafka "tech-news"
 * Consommé ensuite par le Notification Service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsCreatedEvent {

    private UUID articleId;
    private String externalId;
    private String title;
    private String sourceUrl;
    private String authorName;
    private String tags;
    private String coverImageUrl;
    private String publishedAt;
    private String fetchedAt;
}