package org.zerock.cleanaido_admin_back.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderDetailListDTO;
import org.zerock.cleanaido_admin_back.order.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;

    public PageResponseDTO<OrderListDTO> listOrdersByRole(PageRequestDTO pageRequestDTO, List<String> statuses, String userId, String role) {
        Page<OrderListDTO> resultPage;
        Pageable pageable = pageRequestDTO.toPageable();
        SearchDTO searchDTO = pageRequestDTO.getSearchDTO();

        if (searchDTO == null || searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            // 검색 조건이 없는 경우 권한별로 리스트 조회
            if ("ROLE_ADMIN".equals(role)) {
                // 관리자는 전체 주문 조회
                resultPage = orderRepository.list(statuses, pageable);
            } else if ("ROLE_SELLER".equals(role)) {
                // 판매자는 본인의 상품과 관련된 주문 조회
                resultPage = orderRepository.listBySeller(userId, statuses, pageable);
            } else {
                throw new IllegalArgumentException("잘못된 권한입니다. ROLE_ADMIN 또는 ROLE_SELLER만 허용됩니다.");
            }
        } else {
            // 검색 조건이 있는 경우 공통 검색 로직 호출
            resultPage = searchOrders(searchDTO, statuses, pageable);
        }

        return new PageResponseDTO<>(resultPage.getContent(), pageRequestDTO, resultPage.getTotalElements());
    }

    private Page<OrderListDTO> searchOrders(SearchDTO searchDTO, List<String> statuses, Pageable pageable) {
        switch (searchDTO.getSearchType()) {
            case "orderNumber":
                return orderRepository.searchByOrderNumber(searchDTO.getKeyword(), statuses, pageable);
            case "productNumber":
                return orderRepository.searchByProductNumber(searchDTO.getKeyword(), statuses, pageable);
            case "customerId":
                return orderRepository.searchByCustomerId(searchDTO.getKeyword(), statuses, pageable);
            case "phoneNumber":
                return orderRepository.searchByPhoneNumber(searchDTO.getKeyword(), statuses, pageable);
            case "trackingNumber":
                return orderRepository.searchByTrackingNumber(searchDTO.getKeyword(), statuses, pageable);
            default:
                return Page.empty(pageable);
        }
    }

    public PageResponseDTO<OrderDetailListDTO> listOrderDetail
            (String sellerId, Long orderNum, PageRequestDTO pageRequestDTO) {

        PageResponseDTO<OrderDetailListDTO> results
                = orderRepository.getOrderDetailList(sellerId, orderNum, pageRequestDTO);

        return results;

    }

}
