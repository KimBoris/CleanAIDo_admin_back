package org.zerock.cleanaido_admin_back;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;

public class PasswordEncoderGenerator {
    @Test
    public void generateEncodedPassword() {
        // 비밀번호 평문
        String rawPassword = "1111";

        // BCryptPasswordEncoder를 이용해 암호화
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);

        // 결과 출력
        System.out.println("Encoded Password: " + encodedPassword);

        // 암호화된 비밀번호가 유효한지 확인 (테스트 검증)
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Password matches: " + matches);
    }

//    @Test
//    public class KeyGenerator {
//        public static void main(String[] args) {
//            Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
//            System.out.println("Generated Key: " + java.util.Base64.getEncoder().encodeToString(key.getEncoded()));
//        }
//    }
}
