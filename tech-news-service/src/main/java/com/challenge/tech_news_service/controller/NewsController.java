package com.challenge.tech_news_service.controller;

import com.challenge.tech_news_service.dto.NewsArticleResponse;
import com.challenge.tech_news_service.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * GET /api/news?page=0&size=10
     * GET /api/news?tag=kafka&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<NewsArticleResponse>> lister(
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<NewsArticleResponse> result = (tag != null && !tag.isBlank())
                ? newsService.getArticlesByTag(tag, pageable)
                : newsService.getArticles(pageable);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/news/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsArticleResponse> detail(@PathVariable UUID id) {
        return ResponseEntity.ok(newsService.getArticleById(id));
    }
}