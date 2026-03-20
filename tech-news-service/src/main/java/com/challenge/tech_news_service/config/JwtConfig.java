package com.challenge.tech_news_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@Slf4j
public class JwtConfig {

    @Value("${jwt.public-key-path:}")
    private String publicKeyPath;

    @Value("classpath:keys/public_key.pem")
    private Resource publicKeyResource;

    @Bean
    public PublicKey publicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String keyContent;
        if (publicKeyPath != null && !publicKeyPath.isBlank()
                && Files.exists(Paths.get(publicKeyPath))) {
            keyContent = Files.readString(Paths.get(publicKeyPath));
        } else {
            try (InputStream is = publicKeyResource.getInputStream()) {
                keyContent = new String(is.readAllBytes());
            }
        }
        keyContent = keyContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(keyContent);
        return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
    }
}


