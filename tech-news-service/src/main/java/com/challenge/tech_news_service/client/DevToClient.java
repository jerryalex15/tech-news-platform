package com.challenge.tech_news_service.client;

import com.challenge.tech_news_service.config.FeignConfig;
import com.challenge.tech_news_service.dto.DevToArticleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "devto-client", url = "${devto.api.url}", configuration = FeignConfig.class)
public interface DevToClient {

    /**
     * Récupère les articles par tag
     *
     * @param tag      tag à filtrer (ex: "java")
     * @param perPage  nombre d'articles par page
     * @param page     numéro de page (commence à 1)
     * @param apiKey   token depuis le compte Dev.to
     */
    @GetMapping("/articles")
    List<DevToArticleDto> fetchArticles(
            @RequestParam("tag") String tag,
            @RequestParam("per_page") int perPage,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestHeader("api-key") String apiKey
    );
}
