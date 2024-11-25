package org.zerock.cleanaido_admin_back.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.cleanaido_admin_back.login.filter.JWTFilter;
import org.zerock.cleanaido_admin_back.login.util.JWTUtil;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JWTUtil jwtUtil) throws Exception {
        http
                // CSRF 비활성화
                .csrf((csrf) -> csrf.disable())

                // 세션 사용 비활성화
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 요청 허용 정책
                .authorizeHttpRequests((auth) ->
                        auth
                                .requestMatchers("/api/auth/**").permitAll() // 로그인, 회원가입은 인증 없이 접근 가능
                                .requestMatchers("/api/admin/**").hasRole("ADMIN") // 관리자 전용
                                .requestMatchers("/api/seller/**").hasRole("SELLER") // 판매자 전용
                                .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )

                // JWT 필터 추가
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 Encoder
    }
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "1111";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }

}
