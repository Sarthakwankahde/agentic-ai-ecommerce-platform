package com.sarthak.agenticai.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket forgotPasswordBucket() {

        Bandwidth limit =
                Bandwidth.builder()
                        .capacity(3)
                        .refillGreedy(3, Duration.ofMinutes(15))
                        .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Bean
    public Bucket resetPasswordBucket() {

        Bandwidth limit =
                Bandwidth.builder()
                        .capacity(5)
                        .refillGreedy(5, Duration.ofMinutes(15))
                        .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}