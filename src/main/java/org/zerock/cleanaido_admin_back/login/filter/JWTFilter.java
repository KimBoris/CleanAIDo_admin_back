package org.zerock.cleanaido_admin_back.login.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.cleanaido_admin_back.login.util.JWTUtil;

import java.io.IOException;
import java.util.Collections;

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
            token = token.substring(7);
            try {
                var claims = jwtUtil.validateToken(token);
                String userId = claims.get("user_id", String.class);
                boolean isAdmin = claims.get("admin_role", Boolean.class);

                // 인증 정보 설정
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        isAdmin ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) :
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SELLER"))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
