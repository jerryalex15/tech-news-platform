package com.challenge.tech_news_service.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor userAgentInterceptor() {
        return requestTemplate -> requestTemplate
                .header("User-Agent", "tech-news-service/1.0");
    }
}
