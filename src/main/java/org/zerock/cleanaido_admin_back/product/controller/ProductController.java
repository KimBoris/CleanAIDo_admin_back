package org.zerock.cleanaido_admin_back.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.common.dto.UploadDTO;
import org.zerock.cleanaido_admin_back.category.dto.CategoryDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductReadDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductRegisterDTO;
import org.zerock.cleanaido_admin_back.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    //해당 판매자의 판매물품 리스트 가져오기
    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        log.info("UserId: " + userId);
        log.info("Role: " + role);

        SearchDTO searchDTO = SearchDTO.builder()
                .searchType(type)
                .keyword(keyword)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        return ResponseEntity.ok(productService.listProductByRole(pageRequestDTO, userId, role));
    }

    //상품 등록시 필요한 카테고리의 리스트 가져오기
    @GetMapping("seller/category")
    public ResponseEntity<List<CategoryDTO>> searchCategory(
            @RequestParam(value = "keyword", required = false) String keyword) {

        return ResponseEntity.ok(productService.searchCategory(keyword));
    }


    //물품 등록
    @PostMapping(value = "seller/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> registerProduct(
            @ModelAttribute ProductRegisterDTO productRegisterDTO,
            @RequestParam("imageFiles") MultipartFile[] imageFiles,
            @RequestParam("detailImageFiles") MultipartFile[] detailImageFiles,
            @RequestParam("usageImageFiles") MultipartFile[] usageImageFiles) {
        log.info("=============================");
        log.info("check111`=======");
        // 인증 정보 가져오기
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("=============================");
        log.info("check222`=======");
        // DTO에 seller 설정
        productRegisterDTO.setSeller(userId);

        log.info("ProductRegisterDTO: " + productRegisterDTO);

        // 파일 업로드 DTO 생성
        UploadDTO imageUploadDTO = new UploadDTO(imageFiles, null);
        UploadDTO detailImageUploadDTO = new UploadDTO(detailImageFiles, null);
        UploadDTO usageImageUploadDTO = new UploadDTO(usageImageFiles, null);

        // 서비스 호출로 상품 등록
        Long productId = productService.registerProduct(
                productRegisterDTO,
                imageUploadDTO,
                detailImageUploadDTO,
                usageImageUploadDTO
        );

        return ResponseEntity.ok(productId);
    }

    //상품 상세정보 가져오기
    @GetMapping("/read/{pno}")
    public ResponseEntity<ProductReadDTO> read(@PathVariable Long pno) {
        ProductReadDTO readDTO = productService.getProduct(pno);
        return ResponseEntity.ok(readDTO);
    }

    //상품 삭제
    @DeleteMapping("seller/delete")
    public Long delete(
            @RequestParam(value = "pno", required = false) Long pno
    ){
        return productService.deleteProduct(pno);
    }

    //상품 정보 수정
    @PutMapping(value = "seller/{pno}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> update(
            @PathVariable Long pno, // 수정 대상 Product 번호
            @ModelAttribute ProductRegisterDTO productRegisterDTO, // 수정할 기본 정보
            @RequestParam(value = "oldImageFiles", required = false) List<String> oldImageFiles,
            @RequestParam(value = "oldDetailFiles", required = false) List<String> oldDetailFiles,
            @RequestParam(value = "oldUsageFiles", required = false) List<String> oldUsageFiles,
            @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles, // 새 이미지 파일
            @RequestParam(value = "detailImageFiles", required = false) MultipartFile[] detailImageFiles, // 새 상세 이미지
            @RequestParam(value = "usageImageFiles", required = false) MultipartFile[] usageImageFiles) { // 새 사용법 이미지

        // 파일이 존재하면 UploadDTO 생성
        UploadDTO imageUploadDTO = (imageFiles != null) ? new UploadDTO(imageFiles, null) : null;
        UploadDTO detailImageUploadDTO = (detailImageFiles != null) ? new UploadDTO(detailImageFiles, null) : null;
        UploadDTO usageImageUploadDTO = (usageImageFiles != null) ? new UploadDTO(usageImageFiles, null) : null;

        // 서비스 호출을 통해 데이터 수정
        Long updatedPno = productService.updateProduct(
                pno, productRegisterDTO,
                oldImageFiles, oldDetailFiles, oldUsageFiles,
                imageUploadDTO, detailImageUploadDTO, usageImageUploadDTO);

        return ResponseEntity.ok(updatedPno); // 수정된 Product 번호 반환
    }
}
