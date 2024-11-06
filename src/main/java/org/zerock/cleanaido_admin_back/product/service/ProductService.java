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
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.product.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

//    public PageResponseDTO<ProductListDTO> listProduct(PageRequestDTO pageRequestDTO) {
//        try {
//            Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
//            Page<Product> productPage = productRepository.list(pageable);
//
//            List<ProductListDTO> dtoList = productPage.getContent().stream()
//                    .map(product -> ProductListDTO.builder()
//
//                            .build()).collect(Collectors.toList());
//
//            return new PageResponseDTO<>(dtoList, pageRequestDTO, productPage.getTotalElements());
//        } catch (IllegalArgumentException ex) {
//            log.error("페이지 번호는 1 이상이어야 합니다: {}", ex.getMessage());
//            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
//        } catch (Exception ex) {
//            log.error("질문 목록을 불러오는 중 오류가 발생했습니다: {}", ex.getMessage());
//            throw new RuntimeException("질문 목록을 불러오는 중 오류가 발생했습니다.");
//        }
//    }
}
