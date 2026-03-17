-- V1__create_news_article_table.sql

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE news_article (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    external_id VARCHAR(255) NOT NULL UNIQUE,

    title VARCHAR(255) NOT NULL,

    content TEXT,

    source_url VARCHAR(500) NOT NULL,

    author_name VARCHAR(255),

    tags VARCHAR(255),

    cover_image_url VARCHAR(500),

    reactions_count INTEGER,

    published_at TIMESTAMP,

    fetched_at TIMESTAMP DEFAULT NOW()
);

-- index pour la déduplication Dev.to
CREATE UNIQUE INDEX idx_external_id ON news_article(external_id);

-- index utile pour recherche par tags
CREATE INDEX idx_news_article_tags ON news_article(tags);