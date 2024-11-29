package org.zerock.cleanaido_admin_back.login.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {

    private final byte[] secretKeyBytes;

    public JWTUtil(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKeyBytes = Base64.getDecoder().decode(secretKey);
    }

    public String createAccessToken(String userId, boolean isAdmin, int expirationMinutes) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("user_id", userId) // user_id 포함
                .claim("admin_role", isAdmin) // admin_role 포함
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMinutes * 24 * 60* 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKeyBytes), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String userId, int expirationDays) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("user_id", userId) // user_id 포함
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDays * 24 * 60 * 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKeyBytes), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKeyBytes))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
