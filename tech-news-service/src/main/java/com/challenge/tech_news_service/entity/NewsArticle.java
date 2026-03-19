package com.challenge.tech_news_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "news_article", indexes = {
        @Index(name = "idx_external_id", columnList = "externalId", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String externalId;         // ID Dev.to — clé de déduplication

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String sourceUrl;

    private String authorName;

    private String tags;               // ex: "java,spring,kafka"

    private String coverImageUrl;

    private Integer reactionsCount;

    private LocalDateTime publishedAt; // date de publication sur Dev.to

    @CreationTimestamp
    private LocalDateTime fetchedAt;   // date où on l'a collecté
}
