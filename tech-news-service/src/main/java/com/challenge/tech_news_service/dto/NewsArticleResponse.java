package com.challenge.tech_news_service.dto;

import com.challenge.tech_news_service.entity.NewsArticle;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NewsArticleResponse {

    private UUID id;
    private String title;
    private String sourceUrl;
    private String authorName;
    private String tags;
    private String coverImageUrl;
    private Integer reactionsCount;
    private LocalDateTime publishedAt;
    private LocalDateTime fetchedAt;

    public static NewsArticleResponse from(NewsArticle article) {
        return NewsArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .sourceUrl(article.getSourceUrl())
                .authorName(article.getAuthorName())
                .tags(article.getTags())
                .coverImageUrl(article.getCoverImageUrl())
                .reactionsCount(article.getReactionsCount())
                .publishedAt(article.getPublishedAt())
                .fetchedAt(article.getFetchedAt())
                .build();
    }
}