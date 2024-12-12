//package org.zerock.cleanaido_admin_back.user;
//
//import org.junit.jupiter.api.Test;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.Mac;
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class KeyGeneratorExampleTest {
//
//    @Test
//    public void testGenerateHmacSha256Key() throws Exception {
//        // KeyGenerator를 사용하여 HMAC-SHA256 SecretKey 생성
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//        keyGenerator.init(256); // 키 길이를 256비트로 설정
//        SecretKey secretKey = keyGenerator.generateKey();
//
//        // SecretKey를 Base64로 인코딩
//        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        System.out.println("Base64 Encoded Key: " + base64Key);
//
//        // 생성된 키가 null이 아닌지 검증
//        assertNotNull(secretKey);
//        assertNotNull(base64Key);
//    }
//
//    @Test
//    public void testGenerateHmacForMessage() throws Exception {
//        // KeyGenerator를 사용하여 HMAC-SHA256 SecretKey 생성
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//        keyGenerator.init(256); // 키 길이를 256비트로 설정
//        SecretKey secretKey = keyGenerator.generateKey();
//
//        // Mac 객체 초기화
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(secretKey);
//
//        // 테스트 메시지
//        String message = "Test message";
//
//        // 메시지에 대한 HMAC 계산
//        byte[] hmacBytes = mac.doFinal(message.getBytes());
//
//        // HMAC 결과를 Base64로 인코딩
//        String base64Hmac = Base64.getEncoder().encodeToString(hmacBytes);
//        System.out.println("Base64 Encoded HMAC: " + base64Hmac);
//
//        // 생성된 HMAC이 null이 아닌지 검증
//        assertNotNull(hmacBytes);
//        assertNotNull(base64Hmac);
//    }
//}
