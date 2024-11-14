package org.zerock.cleanaido_admin_back.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.common.dto.UploadDTO;
import org.zerock.cleanaido_admin_back.category.dto.CategoryDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductRegisterDTO;
import org.zerock.cleanaido_admin_back.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(@RequestParam(value = "page", defaultValue = "1") int page,
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

        if (searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            log.info("keyword is null or empty");
            log.info("---------------------");
            return ResponseEntity.ok(productService.listProduct(pageRequestDTO));
        } else {
            log.info("type is :" + searchDTO.getSearchType());
            log.info("keyword is " + searchDTO.getKeyword());
            return ResponseEntity.ok(productService.search(pageRequestDTO));
        }

    }

    @GetMapping("register")
    public ResponseEntity<List<CategoryDTO>> searchCategory(
            @RequestParam(value = "keyword", required = false) String keyword) {

        return ResponseEntity.ok(productService.searchCategory(keyword));
    }



    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> register(
            @ModelAttribute ProductRegisterDTO productRegisterDTO,
            @RequestParam List<Long> categoryList ,
            @RequestParam("imageFiles") MultipartFile[] imageFiles,
            @RequestParam("detailImageFiles") MultipartFile[] detailImageFiles,
            @RequestParam("usageImageFiles") MultipartFile[] usageImageFiles) {
        UploadDTO imageUploadDTO = new UploadDTO(imageFiles, null); // 또는 적절한 초기화 코드
        UploadDTO detailImageUploadDTO = new UploadDTO(detailImageFiles, null); // 또는 적절한 초기화 코드
        UploadDTO usageImageUploadDTO = new UploadDTO(usageImageFiles, null); // 또는 적절한 초기화 코드
        Long fno = productService.registerProduct(
                productRegisterDTO, categoryList, imageUploadDTO, detailImageUploadDTO, usageImageUploadDTO);
        return ResponseEntity.ok(fno);
    }
}
