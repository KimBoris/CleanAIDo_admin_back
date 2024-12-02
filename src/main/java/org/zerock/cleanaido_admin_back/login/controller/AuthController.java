package org.zerock.cleanaido_admin_back.login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.cleanaido_admin_back.login.dto.LoginDTO;
import org.zerock.cleanaido_admin_back.login.util.JWTUtil;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            // User 인증
            User user = userService.authenticate(loginDTO.getUserId(), loginDTO.getPassword());

            // admin_role 값 확인
            Boolean isAdmin = user.isAdminRole();
            if (isAdmin == null) {
                throw new IllegalStateException("Admin role is null for user: " + user.getUserId());
            }

            String ownerName = user.getOwnerName();

            log.info("User authenticated: userId = {}, adminRole = {}, ownerName = {}", user.getUserId(), isAdmin, ownerName);

            String accessToken = jwtUtil.createAccessToken(user.getUserId(), isAdmin, ownerName, 60);
            String refreshToken = jwtUtil.createRefreshToken(user.getUserId(), 7);

            // 응답 반환
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "adminRole", isAdmin,
                    "userId", user.getUserId(),
                    "ownerName", ownerName // 응답에 owner_name 추가
            ));
        } catch (IllegalArgumentException e) {
            log.error("Invalid credentials: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

}
