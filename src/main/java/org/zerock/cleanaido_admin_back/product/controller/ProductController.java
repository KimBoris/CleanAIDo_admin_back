package org.zerock.cleanaido_admin_back.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.UploadDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductRegisterDTO;
import org.zerock.cleanaido_admin_back.product.service.ProductService;

@RestController
@RequestMapping("/api/v1/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        return ResponseEntity.ok(productService.listProduct(pageRequestDTO));

    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> register(
            @ModelAttribute ProductRegisterDTO productRegisterDTO,
            @RequestParam("files") MultipartFile[] files) {
        UploadDTO uploadDTO = new UploadDTO(files, null); // 또는 적절한 초기화 코드
        Long fno = productService.registerProduct(productRegisterDTO, uploadDTO);
        return ResponseEntity.ok(fno);
    }
}
