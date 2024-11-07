package org.zerock.cleanaido_admin_back.product.service;

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
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductRegisterDTO;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.product.repository.ProductRepository;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQListDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQRegisterDTO;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;

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

    public PageResponseDTO<ProductListDTO> listProduct(PageRequestDTO pageRequestDTO) {

        if (pageRequestDTO.getPage() < 1) {
            throw new IllegalArgumentException("페이지 번호는 1이상 이어야 합니다.");
        }
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        PageResponseDTO<ProductListDTO> response = productRepository.list(pageRequestDTO);

        log.info("---------------------------------------1");

        return response;

    }
    public Long registerProduct(ProductRegisterDTO dto, UploadDTO uploadDTO) {
        Product product = Product.builder()
                .pcode(dto.getPcode())
                .pname(dto.getPname())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .releasedAt(dto.getReleasedAt())
                .ptags(dto.getPtags())
                .sellerId(dto.getSellerId())
                .build();

//        List<String> fileNames = Optional.ofNullable(uploadDTO.getFiles())
//                .map(files -> Arrays.stream(files)
//                        .filter(file -> !file.isEmpty()) // 실제 파일이 있는 경우만 필터링
//                        .collect(Collectors.toList()))
//                .filter(validFiles -> !validFiles.isEmpty()) // 빈 리스트는 제외
//                .map(customFileUtil::saveFiles) // 유효한 파일이 있으면 저장
//                .orElse(Collections.emptyList()); // 유효한 파일이 없으면 빈 리스트
//
//        fileNames.forEach(Product::addFile);

        productRepository.save(product);

        return product.getPno();
    }
}
