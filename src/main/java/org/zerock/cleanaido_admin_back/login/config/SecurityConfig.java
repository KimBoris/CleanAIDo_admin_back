package org.zerock.cleanaido_admin_back.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.zerock.cleanaido_admin_back.login.filter.JWTFilter;
import org.zerock.cleanaido_admin_back.login.util.JWTUtil;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    public SecurityConfig(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 로그인/회원가입은 인증 없이 접근 가능
                        .requestMatchers("/api/v1/faq/list/**", "/api/v1/faq/read/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SELLER")
                        .requestMatchers("/api/v1/faq/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/v1/product/list/**", "/api/v1/product/read/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SELLER")
                        .requestMatchers("/api/v1/product/seller/**").hasAuthority("ROLE_SELLER")
                        .requestMatchers("/api/v1/orders/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SELLER")
                        .requestMatchers("/api/v1/qna/list/**", "/api/v1/qna/read/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SELLER")
                        .requestMatchers("/api/v1/qna/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/v1/admin/user/register").permitAll()
                        .requestMatchers("/api/v1/admin/user/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/v1/admin/customer/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JWTFilter(jwtUtil), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
