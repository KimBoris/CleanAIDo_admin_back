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
import org.zerock.cleanaido_admin_back.product.service.ProductService;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserReadDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserRegisterDTO;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;
import org.zerock.cleanaido_admin_back.user.dto.UserReadDTO;
import org.zerock.cleanaido_admin_back.user.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<UserListDTO>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                                             @RequestParam(value = "type", required = false) String type,
                                                             @RequestParam(value = "keyword", required = false) String keyword) {

        SearchDTO searchDTO = SearchDTO.builder()
                .searchType(type)
                .keyword(keyword)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        //키워드가 있거나 type이 있으면
        if (searchDTO.getKeyword() != null || searchDTO.getSearchType() != null) {
            return ResponseEntity.ok(userService.search(pageRequestDTO));
        }
        return ResponseEntity.ok(userService.listUsers(pageRequestDTO));

        }
    @GetMapping("{userId}")
    public ResponseEntity<UserReadDTO> read(@PathVariable String userId)
    {
        UserReadDTO readDTO = userService.getUser(userId);
        return ResponseEntity.ok(readDTO);
    }

    @PostMapping(value = "register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(UserRegisterDTO userRegisterDTO) {

        UploadOneDTO uploadOneDTO = new UploadOneDTO(userRegisterDTO.getImageFile(), null);
        String userId = userService.registUser(userRegisterDTO, uploadOneDTO);

        return ResponseEntity.ok(userId);
    }

    @PostMapping("checkid")
    public ResponseEntity<Boolean> check(@RequestBody Map<String, String> requestId) {
        try {

            String userId = requestId.get("userId");
            boolean isUserId = userService.checkUserId(userId);

            return ResponseEntity.ok(isUserId);

        } catch (IllegalArgumentException e) {

            // 잘못된 요청에 대한 응답 처리
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PutMapping("delete/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId)
    {
        userService.softDeleteUser(userId);

        return ResponseEntity.ok(userId+" is deleted successfully");
    }

    @PutMapping("{userId}")
    public ResponseEntity<String> update(@PathVariable String userId
                                         ,@ModelAttribute UserRegisterDTO userRegisterDTO){

        String updatedUserId = userService.updateUser(userId, userRegisterDTO);

        return ResponseEntity.ok(updatedUserId);
    }

}
