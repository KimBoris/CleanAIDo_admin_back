package org.zerock.cleanaido_admin_back.login.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;

@Component
@Log4j2
public class JWTUtil {

    private final byte[] secretKeyBytes;

    public JWTUtil(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKeyBytes = Base64.getDecoder().decode(secretKey);
    }

    public String createAccessToken(String userId, boolean isAdmin, String ownerName, int expirationMinutes) {
        log.info("Creating AccessToken: userId = {}, isAdmin = {}, ownerName = {}", userId, isAdmin, ownerName);
        return Jwts.builder()
                .setSubject(userId)
                .claim("user_id", userId)
                .claim("admin_role", isAdmin) // Boolean 값으로 설정
                .claim("owner_name", ownerName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKeyBytes), SignatureAlgorithm.HS256)
                .compact();
    }



    public String createRefreshToken(String userId, int expirationDays) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("user_id", userId) // user_id 필드 추가
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
