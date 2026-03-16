# tech-news-platform
Ce projet consiste à construire une plateforme de news techniques automatisée, alimentée par une API tierce (Dev.to), et découpée en microservices communicants via Apache Kafka. Les articles ne sont pas saisis manuellement : un job schedulé interroge régulièrement l'API Dev.to, déduplique les résultats, et publie les nouveaux articles dans Kafka.
