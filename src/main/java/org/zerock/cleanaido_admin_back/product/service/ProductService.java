package org.zerock.cleanaido_admin_back.product.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
import org.zerock.cleanaido_admin_back.common.util.S3Uploader;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductReadDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductRegisterDTO;
import org.zerock.cleanaido_admin_back.category.entity.Category;
import org.zerock.cleanaido_admin_back.product.entity.ImageFile;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.product.entity.UsageImageFile;
import org.zerock.cleanaido_admin_back.product.repository.ProductRepository;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CustomFileUtil customFileUtil;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    //판매자/관리자 권한 구분하여 물품 리스트 가져오기
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

    //검색한 물품 가져오기
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

    //카테고리 리스트 가져오기
    public List<CategoryDTO> searchCategory(String keyword) {

        List<CategoryDTO> resultList = productRepository.searchCategoryBy(keyword);

        return resultList;
    }

    //물품 등록하기
    public Long registerProduct(ProductRegisterDTO dto, UploadDTO imageUploadDTO,
                                UploadDTO detailImageUploadDTO, UploadDTO usageImageUploadDTO) {

        // 판매자 유효성 확인
        if (dto.getSeller() == null) {
            throw new IllegalArgumentException("판매자 정보가 누락되었습니다.");
        }

        log.info("Registering product for seller: {}", dto.getSeller());

        //dto에서 seller를 가져와 임시로 생성
        User seller = userRepository.findById(dto.getSeller())
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다: " + dto.getSeller()));

        log.info("Found seller: {}", seller.getUserId());

        //dto에서 카테고리 번호를 가져와 임시로 생성
        Category category = Category.builder()
                .cno(dto.getCategoryId())
                .build();

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
                .category(category)
                .puseCase(dto.getPuseCase())
                .pusedItem(dto.getPusedItem())
                .build();

        log.info("Product to register: {}", product);


        // 이미지 파일 처리
        processImages(product, imageUploadDTO, true, false);
        processImages(product, detailImageUploadDTO, true, true);
        processImages(product, usageImageUploadDTO, false, true);



        // 상품 저장
        productRepository.save(product);
        log.info("Product registered with ID: {}", product.getPno());

        return product.getPno();
    }

    private void processImages(Product product, UploadDTO uploadDTO, boolean isMainImage, boolean isDetailImage) {
        log.info("Processing image for product: {}", product.getPno());
        log.info("Upload image: {}", uploadDTO);
        List<String> fileNames;
        //썸네일&상세이미지인지 아니면 사용처이미지인지 구분
        if (isMainImage) {
            fileNames = Optional.ofNullable(uploadDTO.getFiles())
                    .map(files -> Arrays.stream(files)
                            .filter(file -> !file.isEmpty())
                            .collect(Collectors.toList()))
                    .filter(validFiles -> !validFiles.isEmpty())
                    .map(customFileUtil::saveFiles)
                    .orElse(Collections.emptyList());
        }else{
            //사용처 이미지면 파이썬 서버에도 사진을 전송해야하므로 다른 로직으로 분리
            fileNames = Optional.ofNullable(uploadDTO.getFiles())
                    .map(files -> Arrays.stream(files)
                            .filter(file -> !file.isEmpty())
                            .collect(Collectors.toList()))
                    .filter(validFiles -> !validFiles.isEmpty())
                    .map(customFileUtil::saveUsageFiles)
                    .orElse(Collections.emptyList());
        }
        //저장소에 들어간 이미지들의 파일명을 db에 저장
        fileNames.forEach(filename -> {
            if (isMainImage) {
                if (isDetailImage) {
                    product.addImageFile(filename, true); //true면 상세이미지
                } else {
                    product.addImageFile(filename, false); // false면 썸네일 이미지
                }
            } else {
                product.addUsingImageFile(filename);
            }
        });
    }

    //상품 상세정보 가져오기
    public ProductReadDTO getProduct(Long pno) {

        ProductReadDTO productReadDTO = productRepository.getProduct(pno);

        if (productReadDTO == null) {
            log.info("no product--------------------------");
            throw new EntityNotFoundException("상품을 찾을 수 없습니다. pno: " + pno);
        }

        return productReadDTO;
    }

    //상품 삭제하기
    public Long deleteProduct(Long id) {

        productRepository.deleteById(id);

        return id;
    }

    //상품 정보 수정하기
    @Transactional
    public Long updateProduct(
            Long pno,
            ProductRegisterDTO productRegisterDTO,
            List<String> oldImageFiles,
            List<String> oldDetailFiles,
            List<String> oldUsageFiles,
            UploadDTO imageUploadDTO,
            UploadDTO detailImageUploadDTO,
            UploadDTO usageImageUploadDTO) {

        // null 체크 후 빈 리스트로 초기화
        List<String> safeOldImageFiles = (oldImageFiles != null) ? oldImageFiles : new ArrayList<>();
        List<String> safeOldDetailFiles = (oldDetailFiles != null) ? oldDetailFiles : new ArrayList<>();
        List<String> safeOldUsageFiles = (oldUsageFiles != null) ? oldUsageFiles : new ArrayList<>();

        for (String file : safeOldImageFiles) {
            log.info("Safe Old Image File: {}", file);
        }

        Category category = Category.builder()
                .cno(productRegisterDTO.getCategoryId())
                .build();

        // 기존 Product 조회
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID: " + pno));

        // Product 기본 정보 업데이트
        product.setPname(productRegisterDTO.getPname());
        product.setPrice(productRegisterDTO.getPrice());
        product.setQuantity(productRegisterDTO.getQuantity());
        product.setPtags(productRegisterDTO.getPtags());
        product.setPstatus(productRegisterDTO.getPstatus());
        product.setPuseCase(productRegisterDTO.getPuseCase());
        product.setPusedItem(productRegisterDTO.getPusedItem());
        product.setCategory(category);

        // type이 true인 경우만 필터링
        List<String> oldFileNames = product.getImageFiles().stream()
                .filter(imageFile -> !imageFile.isType()) // isType == false
                .map(ImageFile::getFileName) // 파일 이름만 추출
                .collect(Collectors.toList());

        for (String file : oldFileNames) {
            log.info("saved files: {}", file);
        }

        // 삭제할 파일 리스트
        List<String> filesToDelete = oldFileNames.stream()
                .filter(oldFile -> !safeOldImageFiles.contains(oldFile)) // 안전하게 null 체크된 리스트 사용
                .collect(Collectors.toList());

        // 삭제할 파일이 있다면 실제 파일까지 삭제
        if (!filesToDelete.isEmpty()) {
            for (String file : filesToDelete) {
                s3Uploader.removeS3File(file);
            }
            // DB에서 파일 삭제
            product.getImageFiles().removeIf(imageFile -> filesToDelete.contains(imageFile.getFileName()));
        }

        // type이 false인 경우만 필터링 (상세 이미지 파일)
        List<String> oldDetailFileNames = product.getImageFiles().stream()
                .filter(ImageFile::isType) // isType == true
                .map(ImageFile::getFileName) // 파일 이름만 추출
                .collect(Collectors.toList());

        // 삭제할 파일 리스트
        List<String> detailFilesToDelete = oldDetailFileNames.stream()
                .filter(oldFile -> !safeOldDetailFiles.contains(oldFile)) // 안전하게 null 체크된 리스트 사용
                .collect(Collectors.toList());

        // 삭제할 파일이 있다면 실제 파일까지 삭제
        if (!detailFilesToDelete.isEmpty()) {
            for (String file : detailFilesToDelete) {
                s3Uploader.removeS3File(file);
            }
            // DB에서 파일 삭제
            product.getImageFiles().removeIf(imageFile -> detailFilesToDelete.contains(imageFile.getFileName()));
        }

        // 사용법 이미지 파일 처리
        List<String> oldUsageFileNames = product.getUsageImageFiles().stream()
                .map(UsageImageFile::getFileName) // 파일 이름만 추출
                .collect(Collectors.toList());

        // 삭제할 파일 리스트
        List<String> usageFilesToDelete = oldUsageFileNames.stream()
                .filter(oldFile -> !safeOldUsageFiles.contains(oldFile)) // 안전하게 null 체크된 리스트 사용
                .collect(Collectors.toList());

        // 삭제할 파일이 있다면 실제 파일까지 삭제
        if (!usageFilesToDelete.isEmpty()) {
            for (String file : usageFilesToDelete) {
                s3Uploader.removeS3File(file);
            }
            // DB에서 파일 삭제
            product.getUsageImageFiles().removeIf(usageImageFile -> usageFilesToDelete.contains(usageImageFile.getFileName()));
        }

        // 이미지 파일 처리 (새 파일 등록)
        processImages(product, imageUploadDTO, true, false);
        processImages(product, detailImageUploadDTO, true, true);  // 상세 이미지도 등록
        processImages(product, usageImageUploadDTO, false, true);  // 사용법 이미지도 등록

        // DB 저장
        productRepository.save(product);

        return product.getPno();
    }

}
