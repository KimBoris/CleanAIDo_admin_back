package org.zerock.cleanaido_admin_back.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.product.service.ProductService;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserReadDTO;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;
import org.zerock.cleanaido_admin_back.user.service.UserService;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
@Log4j2
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

    @PutMapping("delete/{userId}")
    public String delete(@PathVariable String userId)
    {
        return userService.deleteUser(userId);
    }

}
