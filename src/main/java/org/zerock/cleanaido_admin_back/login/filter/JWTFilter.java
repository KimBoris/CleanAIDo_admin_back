package org.zerock.cleanaido_admin_back.login.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.cleanaido_admin_back.login.util.JWTUtil;

import java.io.IOException;
import java.util.Collections;
@Log4j2
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
            try {
                var claims = jwtUtil.validateToken(token);

                String userId = claims.get("user_id", String.class);
                Boolean isAdmin = claims.get("admin_role", Boolean.class);

                if (isAdmin == null) {
                    log.error("Missing admin_role in JWT token for user: {}", userId);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: Missing admin_role");
                    return;
                }

                log.info("JWT user_id: {}, admin_role: {}", userId, isAdmin);

                String role = isAdmin ? "ROLE_ADMIN" : "ROLE_SELLER";

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(role))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception e) {
                log.error("JWT validation failed: ", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }





}
