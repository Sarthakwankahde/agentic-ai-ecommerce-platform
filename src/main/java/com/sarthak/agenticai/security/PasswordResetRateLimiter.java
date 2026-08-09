package com.sarthak.agenticai.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PasswordResetRateLimiter {

    private final Map<String, Bucket> buckets =
            new ConcurrentHashMap<>();

    public boolean isAllowed(String email) {

        String key = email.toLowerCase().trim();

        Bucket bucket = buckets.computeIfAbsent(
                key,
                k -> createBucket()
        );

        return bucket.tryConsume(1);
    }

    private Bucket createBucket() {

        Bandwidth limit =
                Bandwidth.builder()
                        .capacity(3)
                        .refillGreedy(3, Duration.ofHours(1))
                        .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
