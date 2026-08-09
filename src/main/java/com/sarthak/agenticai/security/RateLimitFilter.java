package com.sarthak.agenticai.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets =
            new ConcurrentHashMap<>();

    private Bucket createBucket() {

        Bandwidth limit =
                Bandwidth.builder()
                        .capacity(5)
                        .refillGreedy(
                                5,
                                Duration.ofMinutes(1)
                        )
                        .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private Bucket resolveBucket(String key) {

        return buckets.computeIfAbsent(
                key,
                k -> createBucket()
        );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        /*
         * Apply rate limiting only to
         * password reset endpoints.
         */
        if (!path.equals(
                "/api/v1/auth/forgot-password")
                &&
                !path.equals(
                        "/api/v1/auth/reset-password")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        /*
         * Identify the client.
         */
        String clientIp =
                request.getRemoteAddr();

        Bucket bucket =
                resolveBucket(clientIp);

        /*
         * Consume one token.
         */
        ConsumptionProbe probe =
                bucket.tryConsumeAndReturnRemaining(1);

        /*
         * Request allowed.
         */
        if (probe.isConsumed()) {

            response.setHeader(
                    "X-Rate-Limit-Remaining",
                    String.valueOf(
                            probe.getRemainingTokens()
                    )
            );

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        /*
         * Rate limit exceeded.
         */
        long retryAfterSeconds =
                Math.max(
                        1,
                        Duration.ofNanos(
                                probe.getNanosToWaitForRefill()
                        ).toSeconds()
                );

        response.setStatus(
                HttpServletResponse.SC_TOO_MANY_REQUESTS
        );

        response.setContentType(
                "application/json"
        );

        response.setHeader(
                "Retry-After",
                String.valueOf(
                        retryAfterSeconds
                )
        );

        response.getWriter().write("""
                {
                    "status": 429,
                    "error": "Too Many Requests",
                    "message": "Too many requests. Please try again later."
                }
                """);
    }
}