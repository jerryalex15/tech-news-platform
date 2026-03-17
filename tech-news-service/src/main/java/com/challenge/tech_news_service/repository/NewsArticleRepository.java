package com.challenge.tech_news_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.challenge.tech_news_service.entity.NewsArticle;

import java.util.UUID;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, UUID> {

    boolean existsByExternalId(String externalId);

    Page<NewsArticle> findByTagsContainingIgnoreCaseOrderByPublishedAtDesc(String tag, Pageable pageable);

    Page<NewsArticle> findAllByOrderByPublishedAtDesc(Pageable pageable);
}