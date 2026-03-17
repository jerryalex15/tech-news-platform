package com.challenge.tech_news_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO qui mappe la réponse de l'API Dev.to
 * GET https://dev.to/api/articles
 */
@Data
public class DevToArticleDto {

    private Long id;

    private String title;

    private String description;

    private String url;

    @JsonProperty("cover_image")
    private String coverImage;

    @JsonProperty("published_at")
    private LocalDateTime publishedAt;

    private String tags;

    @JsonProperty("positive_reactions_count")
    private Integer positiveReactionsCount;

    private UserDto user;

    @Data
    public static class UserDto {
        private String name;
        private String username;
    }

}