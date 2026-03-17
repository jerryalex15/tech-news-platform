package com.challenge.tech_news_service.service;

import com.challenge.tech_news_service.dto.DevToArticleDto;
import com.challenge.tech_news_service.dto.NewsArticleResponse;
import com.challenge.tech_news_service.dto.NewsCreatedEvent;
import com.challenge.tech_news_service.entity.NewsArticle;
import com.challenge.tech_news_service.kafka.NewsEventProducer;
import com.challenge.tech_news_service.repository.NewsArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsArticleRepository repository;
    private final NewsEventProducer producer;

    /**
     * Traite un article Dev.to :
     * - Si déjà en base (externalId existe) → ignoré
     * - Sinon → sauvegarde + publication Kafka dans la même transaction
     */
    @Transactional
    public boolean processIfNew(DevToArticleDto dto) {
        String externalId = String.valueOf(dto.getId());

        if (repository.existsByExternalId(externalId)) {
            log.debug("Article déjà connu, ignoré : [{}] {}", externalId, dto.getTitle());
            return false;
        }

        NewsArticle article = mapToEntity(dto, externalId);
        NewsArticle saved = repository.save(article);

        log.info("Nouvel article sauvegardé : [{}] {}", externalId, saved.getTitle());

        NewsCreatedEvent event = mapToEvent(saved);
        producer.publier(event);

        return true;
    }

    public Page<NewsArticleResponse> getArticles(Pageable pageable) {
        return repository.findAllByOrderByPublishedAtDesc(pageable)
                .map(NewsArticleResponse::from);
    }

    public Page<NewsArticleResponse> getArticlesByTag(String tag, Pageable pageable) {
        return repository.findByTagsContainingIgnoreCaseOrderByPublishedAtDesc(tag, pageable)
                .map(NewsArticleResponse::from);
    }


    public NewsArticleResponse getArticleById(UUID id) {
        return repository.findById(id)
                .map(NewsArticleResponse::from)
                .orElseThrow(() -> new RuntimeException("Article introuvable : " + id));
    }

    // ── Mapping ──────────────────────────────────────────────

    private NewsArticle mapToEntity(DevToArticleDto dto, String externalId) {
        return NewsArticle.builder()
                .externalId(externalId)
                .title(dto.getTitle())
                .content(dto.getDescription())
                .sourceUrl(dto.getUrl())
                .authorName(dto.getUser() != null ? dto.getUser().getName() : null)
                .tags(dto.getTags())
                .coverImageUrl(dto.getCoverImage())
                .reactionsCount(dto.getPositiveReactionsCount())
                .publishedAt(dto.getPublishedAt())
                .build();
    }

    private NewsCreatedEvent mapToEvent(NewsArticle article) {
        return NewsCreatedEvent.builder()
                .articleId(article.getId())
                .externalId(article.getExternalId())
                .title(article.getTitle())
                .sourceUrl(article.getSourceUrl())
                .authorName(article.getAuthorName())
                .tags(article.getTags())
                .coverImageUrl(article.getCoverImageUrl())
                .publishedAt(article.getPublishedAt())
                .fetchedAt(article.getFetchedAt())
                .build();
    }
}
