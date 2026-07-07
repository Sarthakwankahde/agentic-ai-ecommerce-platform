    package com.sarthak.agenticai.security;

    import io.jsonwebtoken.Claims;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;

    import javax.crypto.SecretKey;
    import java.nio.charset.StandardCharsets;
    import java.util.Date;

    @Service
    public class JwtService {

        @Value("${jwt.secret}")
        private String secretKey;

        @Value("${jwt.expiration}")
        private long jwtExpiration;
        public String generateToken(String email) {

            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            return Jwts.builder()
                    .subject(email)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSigningKey())
                    .compact();
        }
        private SecretKey getSigningKey() {
            return Keys.hmacShaKeyFor(
                    secretKey.getBytes(StandardCharsets.UTF_8)
            );
        }
        private Claims extractAllClaims(String token) {

            return Jwts
                    .parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        public String extractUsername(String token) {

            Claims claims = extractAllClaims(token);

            return claims.getSubject();
        }
        public Date extractExpiration(String token) {

            Claims claims = extractAllClaims(token);

            return claims.getExpiration();
        }
        private boolean isTokenExpired(String token) {

            return extractExpiration(token).before(new Date());

        }
        public boolean isTokenValid(String token, UserDetails userDetails) {

            String username = extractUsername(token);

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);

        }

    }