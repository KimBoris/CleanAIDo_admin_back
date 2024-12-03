package org.zerock.cleanaido_admin_back.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.UploadDTO;
import org.zerock.cleanaido_admin_back.common.util.CustomFileUtil;
import org.zerock.cleanaido_admin_back.category.dto.CategoryDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductReadDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductRegisterDTO;
import org.zerock.cleanaido_admin_back.category.entity.Category;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.category.entity.ProductCategory;
import org.zerock.cleanaido_admin_back.product.repository.ProductCategoryRepository;
import org.zerock.cleanaido_admin_back.product.repository.ProductRepository;
import org.zerock.cleanaido_admin_back.product.entity.ImageFiles;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CustomFileUtil customFileUtil;
    private final ProductCategoryRepository productCategoryRepository;
    private final UserRepository userRepository;

    public PageResponseDTO<ProductListDTO> listProductByRole(PageRequestDTO pageRequestDTO, String userId, String role) {
        if ("ROLE_ADMIN".equals(role)) {
            // 관리자는 storeName 포함 전체 리스트 조회
            return productRepository.list(pageRequestDTO);
        } else if ("ROLE_SELLER".equals(role)) {
            // 판매자는 storeName 없이 자신의 리스트만 조회
            return productRepository.listBySeller(pageRequestDTO, userId);
        } else {
            throw new IllegalArgumentException("잘못된 권한입니다.");
        }
    }



    public PageResponseDTO<ProductListDTO> search(PageRequestDTO pageRequestDTO) {
        // SearchDTO에서 type과 keyword를 가져옴
        String type = pageRequestDTO.getSearchDTO().getSearchType();
        String keyword = pageRequestDTO.getSearchDTO().getKeyword();
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        // type, keyword를 기반으로 검색 수행
        Page<Product> resultPage = productRepository.searchBy(type, keyword, pageable);

        List<ProductListDTO> dtoList = resultPage.getContent().stream()
                .map(product -> ProductListDTO.builder()
                        .pno(product.getPno())
                        .pname(product.getPname())
                        .pcode(product.getPcode())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .pstatus(product.getPstatus())
                        .updatedAt(product.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return new PageResponseDTO<>(dtoList, pageRequestDTO, resultPage.getTotalElements());
    }

    public List<CategoryDTO> searchCategory(String keyword) {

        List<CategoryDTO> resultList = productRepository.searchCategoryBy(keyword);

        return resultList;
    }

    public Long registerProduct(ProductRegisterDTO dto,
                                List<Long> categoryList, UploadDTO imageUploadDTO,
                                UploadDTO detailImageUploadDTO, UploadDTO usageImageUploadDTO) {

        // seller 유효성 확인
        if (dto.getSeller() == null) {
            throw new IllegalArgumentException("판매자 정보가 누락되었습니다.");
        }

        log.info("Registering product for seller: {}", dto.getSeller());

        User seller = userRepository.findById(dto.getSeller())
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다: " + dto.getSeller()));

        log.info("Found seller: {}", seller.getUserId());

        // Product 생성
        Product product = Product.builder()
                .pcode(dto.getPcode())
                .pname(dto.getPname())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .releasedAt(dto.getReleasedAt())
                .pstatus(dto.getPstatus())
                .ptags(dto.getPtags())
                .seller(seller)
                .build();

        log.info("Product to register: {}", product);

        // 카테고리 연결
        if (categoryList != null && !categoryList.isEmpty()) {
            log.info("Registering categories: {}", categoryList);
            for (Long cno : categoryList) {
                Category category = Category.builder().cno(cno).build();

                ProductCategory productCategory = ProductCategory.builder()
                        .product(product)
                        .category(category)
                        .build();

                productCategoryRepository.save(productCategory);
            }
        } else {
            log.warn("Category list is null or empty for product: {}", product.getPname());
        }

        // 이미지 파일 처리
        processImages(product, imageUploadDTO, true, false);
        processImages(product, detailImageUploadDTO, true, true);
        processImages(product, usageImageUploadDTO, false,true);

        // 상품 저장
        productRepository.save(product);
        log.info("Product registered with ID: {}", product.getPno());

        return product.getPno();
    }

    private void processImages(Product product, UploadDTO uploadDTO, boolean isMainImage, boolean isDetailImage) {
        List<String> fileNames = Optional.ofNullable(uploadDTO.getFiles())
                .map(files -> Arrays.stream(files)
                        .filter(file -> !file.isEmpty())
                        .collect(Collectors.toList()))
                .filter(validFiles -> !validFiles.isEmpty())
                .map(customFileUtil::saveFiles)
                .orElse(Collections.emptyList());

        log.info("Uploaded {} image files: {}", isMainImage ? "main" : "detail/usage", fileNames);

        fileNames.forEach(filename -> {
            if (isMainImage) {
                if (isDetailImage) {
                    product.addImageFile(filename, true);
                }
                else {
                    product.addImageFile(filename, false);
                }
            } else {
                product.addUsingImageFile(filename);
            }
        });
    }


    public ProductReadDTO getProduct(Long pno) {

        ProductReadDTO productReadDTO = productRepository.getProduct(pno);

        if (productReadDTO == null) {
            log.info("no product--------------------------");
            throw new EntityNotFoundException("상품을 찾을 수 없습니다. pno: " + pno);
        }

        return productReadDTO;
    }

    public Long deleteProduct(Long id){

        productRepository.deleteById(id);

        return id;
    }

    @Transactional
    public Long updateProduct(
            Long pno,
            ProductRegisterDTO productRegisterDTO,
            List<Long> categoryList,
            UploadDTO imageUploadDTO,
            UploadDTO detailImageUploadDTO,
            UploadDTO usageImageUploadDTO) {

        // 기존 Product 조회
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID: " + pno));

        // Product 기본 정보 업데이트
        product.setPname(productRegisterDTO.getPname());
        product.setPrice(productRegisterDTO.getPrice());
        product.setQuantity(productRegisterDTO.getQuantity());
        product.setPtags(productRegisterDTO.getPtags());
        product.setPstatus(productRegisterDTO.getPstatus());

        // 카테고리 업데이트
        productCategoryRepository.deleteById(pno); // 기존 카테고리 삭제
        if (categoryList != null) {
            for (Long cno : categoryList) {
                Category category = Category.builder()
                        .cno(cno)
                        .build();

                ProductCategory productCategory = ProductCategory.builder()
                        .product(product) // Product 객체 참조
                        .category(category)
                        .build();

                productCategoryRepository.save(productCategory); // ProductCategory 저장
            }
        }

        List<String> oldFileNames = product.getImageFiles().stream()
                .filter(ImageFiles::isType) // type이 true인 경우만 필터링
                .map(ImageFiles::getFileName) // 파일 이름만 추출
                .collect(Collectors.toList());

        // 새로운 파일 리스트
        List<String> newFileNames = Optional.ofNullable(imageUploadDTO.getFiles())
                .map(files -> Arrays.stream(files)
                        .filter(file -> !file.isEmpty()) // 실제 파일이 있는 경우만 필터링
                        .collect(Collectors.toList()))
                .filter(validFiles -> !validFiles.isEmpty()) // 빈 리스트는 제외
                .map(customFileUtil::saveFiles) // 파일이 있으면 저장
                .orElse(Collections.emptyList()); // 파일이 없으면 빈 리스트

        // 삭제할 파일 리스트
        List<String> filesToDelete = oldFileNames.stream()
                .filter(oldFile -> !newFileNames.contains(oldFile))
                .collect(Collectors.toList());

        // 삭제할 파일이 있다면 실제 파일까지 삭제
        if (!filesToDelete.isEmpty()) {
            customFileUtil.deleteFiles(filesToDelete);
        }

        productRepository.save(product);

        return product.getPno();
    }

}
