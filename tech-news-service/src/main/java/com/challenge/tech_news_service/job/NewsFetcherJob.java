package com.challenge.tech_news_service.job;

import com.challenge.tech_news_service.client.DevToClient;
import com.challenge.tech_news_service.dto.DevToArticleDto;
import com.challenge.tech_news_service.entity.NewsArticle;
import com.challenge.tech_news_service.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "news.fetcher.enabled", havingValue = "true", matchIfMissing = true)
public class NewsFetcherJob {

    private final DevToClient devToClient;
    private final NewsService newsService;

    @Value("${devto.api.tag:java}")
    private String tag;

    @Value("${devto.api.per-page:20}")
    private int perPage;

    @Value("{devto.api.api.keys")
    private String apiKey;

    @Scheduled(fixedDelayString = "${news.fetcher.fixed-delay-ms:300000}")
    public void fetchAndPublish() {
        log.info("Démarrage collecte Dev.to — tag: {}", tag);

        try {
            List<DevToArticleDto> articles = devToClient.fetchArticles(tag, perPage, 1, apiKey);
            log.info("{} articles récupérés depuis Dev.to", articles.size());

            long nouveaux = articles.stream()
                    .filter(newsService::processIfNew)
                    .count();

            log.info("Collecte terminée — {} nouveaux articles publiés sur Kafka", nouveaux);

        } catch (Exception e) {
            // On logue l'erreur mais on ne crash pas le service
            // Le prochain @Scheduled réessaiera dans 5 minutes
            log.error("Erreur lors de la collecte Dev.to : {}", e.getMessage(), e);
        }
    }
}
