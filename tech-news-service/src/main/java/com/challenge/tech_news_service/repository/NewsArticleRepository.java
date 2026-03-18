package com.challenge.tech_news_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.challenge.tech_news_service.entity.NewsArticle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, UUID> {

    boolean existsByExternalId(String externalId);

    Page<NewsArticle> findByTagsContainingIgnoreCaseOrderByPublishedAtDesc(String tag, Pageable pageable);

    Page<NewsArticle> findAllByOrderByPublishedAtDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = """
        WITH to_keep AS (
            SELECT id
            FROM news_article
            ORDER BY fetched_at DESC
            LIMIT :threshold
        )
        DELETE FROM news_article
        WHERE id NOT IN (SELECT id FROM to_keep)
        """, nativeQuery = true)
    void deleteOlderThan(@Param("threshold") int threshold);
}