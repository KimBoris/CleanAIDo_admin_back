package org.zerock.cleanaido_admin_back.product.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.product.dto.ReviewListDTO;
import org.zerock.cleanaido_admin_back.product.repository.ReviewRepository;

@Transactional
@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public PageResponseDTO<ReviewListDTO> list(PageRequestDTO pageRequestDTO, String role) {
        if ("ROLE_ADMIN".equals(role)) {
            return reviewRepository.list(pageRequestDTO);
        } else if ("ROLE_SELLER".equals(role)) {
            return reviewRepository.list(pageRequestDTO);
        } else {
            throw new IllegalArgumentException("잘못된 권한 입니다.");
        }
    }


}
