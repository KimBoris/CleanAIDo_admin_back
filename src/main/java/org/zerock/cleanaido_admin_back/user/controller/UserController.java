package org.zerock.cleanaido_admin_back.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.common.dto.UploadOneDTO;
import org.zerock.cleanaido_admin_back.product.service.ProductService;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserRegisterDTO;
import org.zerock.cleanaido_admin_back.user.service.UserService;

import javax.annotation.security.PermitAll;

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

        //키워드가 없으면
        if (searchDTO.getKeyword() == null) {
            log.info("All List");
            return ResponseEntity.ok(userService.listUsers(pageRequestDTO));
        }
        else {
            log.info("Search");
            return ResponseEntity.ok(userService.search(pageRequestDTO));
        }
    }

    @PostMapping(value = "register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(
            UserRegisterDTO userRegisterDTO,
            @RequestParam("imageFile") MultipartFile imageFile) {

        UploadOneDTO uploadOneDTO = new UploadOneDTO(imageFile, null);
        String userId = userService.registUser(userRegisterDTO, uploadOneDTO);

        return ResponseEntity.ok(userId);
    }
}
