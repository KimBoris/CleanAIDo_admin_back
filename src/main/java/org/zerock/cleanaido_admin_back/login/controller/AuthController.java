package org.zerock.cleanaido_admin_back.login.controller;

import lombok.RequiredArgsConstructor;
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
public class AuthController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.authenticate(loginDTO.getUserId(), loginDTO.getPassword());

        if (user != null) {
            // AccessToken: 60분, RefreshToken: 7일
            String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.isAdminRole(), 60);
            String refreshToken = jwtUtil.createRefreshToken(user.getUserId(), 7);

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "adminRole", user.isAdminRole()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
