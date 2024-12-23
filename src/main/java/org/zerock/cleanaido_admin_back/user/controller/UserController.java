package org.zerock.cleanaido_admin_back.user.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.common.dto.UploadOneDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserReadDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserRegisterDTO;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;
import org.zerock.cleanaido_admin_back.user.dto.UserReadDTO;
import org.zerock.cleanaido_admin_back.user.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/user") // 사용자 관리 관련 API 컨트롤러
@RequiredArgsConstructor // 필요한 의존성을 자동으로 주입
@Log4j2
@CrossOrigin(origins = "*") // 모든 도메인에서 요청 허용
public class UserController {

    private final UserService userService;

    /**
     * 사용자 목록 조회 메서드
     * @param page 현재 페이지
     * @param size 페이지 당 데이터 개수
     * @param type 검색 타입
     * @param keyword 검색어
     * @return 사용자 목록과 페이지 정보
     */
    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<UserListDTO>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                                             @RequestParam(value = "type", required = false) String type,
                                                             @RequestParam(value = "keyword", required = false) String keyword) {

        // 검색 조건 생성
        SearchDTO searchDTO = SearchDTO.builder()
                .searchType(type)
                .keyword(keyword)
                .build();

        // 페이지 요청 데이터 생성
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        // 검색 조건에 따라 사용자 목록 반환
        if (searchDTO.getKeyword() != null || searchDTO.getSearchType() != null) {
            return ResponseEntity.ok(userService.search(pageRequestDTO));
        }
        return ResponseEntity.ok(userService.listUsers(pageRequestDTO));
    }

    /**
     * 특정 사용자 정보 조회 메서드
     * @param userId 사용자 ID
     * @return 사용자 상세 정보
     */
    @GetMapping("{userId}")
    public ResponseEntity<UserReadDTO> read(@PathVariable String userId) {
        UserReadDTO readDTO = userService.getUser(userId);
        return ResponseEntity.ok(readDTO);
    }

    /**
     * 사용자 등록 메서드
     * @param userRegisterDTO 사용자 등록 정보
     * @return 생성된 사용자 ID
     */
    @PostMapping(value = "register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(UserRegisterDTO userRegisterDTO) {
        // 파일 업로드 데이터 생성
        UploadOneDTO uploadOneDTO = new UploadOneDTO(userRegisterDTO.getImageFile(), null);
        String userId = userService.registUser(userRegisterDTO, uploadOneDTO);

        return ResponseEntity.ok(userId);
    }

    /**
     * 사용자 ID 중복 확인 메서드
     * @param requestId 확인할 사용자 ID
     * @return 중복 여부 (true/false)
     */
    @PostMapping("checkid")
    public ResponseEntity<Boolean> check(@RequestBody Map<String, String> requestId) {
        try {
            String userId = requestId.get("userId");
            boolean isUserId = userService.checkUserId(userId);
            return ResponseEntity.ok(isUserId);
        } catch (IllegalArgumentException e) {
            // 잘못된 요청 처리
            return ResponseEntity.badRequest().body(false);
        }
    }

    /**
     * 사용자 삭제 (소프트 삭제) 메서드
     * @param userId 삭제할 사용자 ID
     * @return 삭제 성공 메시지
     */
    @PutMapping("delete/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId) {
        userService.softDeleteUser(userId);
        return ResponseEntity.ok(userId + " is deleted successfully");
    }

    /**
     * 사용자 정보 수정 메서드
     * @param userId 수정할 사용자 ID
     * @param userRegisterDTO 수정할 정보
     * @return 수정된 사용자 ID
     */
    @PutMapping("{userId}")
    public ResponseEntity<String> update(@PathVariable String userId, @ModelAttribute UserRegisterDTO userRegisterDTO) {
        String updatedUserId = userService.updateUser(userId, userRegisterDTO);
        return ResponseEntity.ok(updatedUserId);
    }

    /**
     * 특정 상태의 사용자 목록 요청 메서드
     * @param page 현재 페이지
     * @param size 페이지 당 데이터 개수
     * @return 특정 상태의 사용자 목록
     */
    @GetMapping("request")
    public ResponseEntity<PageResponseDTO<UserListDTO>> request(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(userService.userListByStatus(pageRequestDTO));
    }

    /**
     * 사용자 상태 업데이트 메서드
     * @param request 사용자 ID와 새로운 상태
     * @return 상태 업데이트 성공 메시지
     */
    @PutMapping("status")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String status = request.get("status");
            String response = userService.updateUserStatus(userId, status);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 잘못된 요청 처리
            return ResponseEntity.badRequest().body("요청 실패");
        }
    }
}
