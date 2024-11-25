package org.zerock.cleanaido_admin_back;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "1111"; // 테스트할 원래 비밀번호
        String encodedPassword = encoder.encode(rawPassword); // 비밀번호 암호화

        // 결과 출력
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
    }
}
