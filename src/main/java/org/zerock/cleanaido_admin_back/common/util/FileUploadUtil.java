package org.zerock.cleanaido_admin_back.common.util;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.File;
import java.io.IOException;

@Component
public class FileUploadUtil {

    public String uploadImages(String filePath) throws IOException {
        // 업로드할 파일을 File 객체로 생성
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        // FastAPI 서버의 이미지 업로드 URL
        String url = "http://43.203.169.10:8000/registImg";

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청의 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 파일을 바디에 추가하기 위한 MultiValueMap
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // MultipartFile을 File에서 변환하여 추가
        body.add("file", new org.springframework.core.io.FileSystemResource(file));

        // HttpEntity에 바디와 헤더를 추가
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // FastAPI 서버로 POST 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 응답 반환
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            // 서버에서 에러 응답이 있을 경우
            return "Error: " + e.getResponseBodyAsString();
        }
    }
}
