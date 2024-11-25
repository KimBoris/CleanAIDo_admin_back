package org.zerock.cleanaido_admin_back.login.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.zerock.cleanaido_admin_back.login.properties.JWTProperties;

import java.util.Date;

@Component
public class JWTUtil {

    private final JWTProperties jwtProperties;

    public JWTUtil(JWTProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createToken(String email, boolean isAdmin, int expirationMinutes) {
        return Jwts.builder()
                .setSubject(email)
                .claim("adminRole", isAdmin)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
