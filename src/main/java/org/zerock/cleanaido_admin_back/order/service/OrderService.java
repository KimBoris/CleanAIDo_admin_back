package org.zerock.cleanaido_admin_back.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.repository.OrderRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // In-progress 상태 주문 목록 조회 및 검색
    public PageResponseDTO<OrderListDTO> listInProgressOrders(PageRequestDTO pageRequestDTO) {
        List<String> inProgressStatuses = Arrays.asList("배송전", "배송중", "배송완료");
        return handleListOrSearch(pageRequestDTO, inProgressStatuses);
    }

    // 취소, 교환, 환불 상태의 주문 목록 조회 및 검색
    public PageResponseDTO<OrderListDTO> listCanceledOrders(PageRequestDTO pageRequestDTO, String status) {
        List<String> canceledStatuses = (status == null || status.isEmpty())
                ? Arrays.asList("취소", "교환", "환불")
                : List.of(status); // 특정 상태가 있을 때 그 상태만 필터링
        return handleListOrSearch(pageRequestDTO, canceledStatuses);
    }

    // 상태별 목록 또는 검색 결과를 반환하는 공통 메서드
    private PageResponseDTO<OrderListDTO> handleListOrSearch(PageRequestDTO pageRequestDTO, List<String> statuses) {
        var pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        var searchDTO = pageRequestDTO.getSearchDTO();

        Page<OrderListDTO> resultPage;
        if (searchDTO == null || searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            // 일반 목록 조회
            resultPage = orderRepository.list(statuses, pageable);
        } else {
            // 검색 조건에 따른 검색
            switch (searchDTO.getSearchType()) {
                case "orderNumber":
                    resultPage = orderRepository.searchByOrderNumber(searchDTO.getKeyword(), statuses, pageable);
                    break;
                case "productNumber":
                    resultPage = orderRepository.searchByProductNumber(searchDTO.getKeyword(), statuses, pageable);
                    break;
                case "customerId":
                    resultPage = orderRepository.searchByCustomerId(searchDTO.getKeyword(), statuses, pageable);
                    break;
                case "phoneNumber":
                    resultPage = orderRepository.searchByPhoneNumber(searchDTO.getKeyword(), statuses, pageable);
                    break;
                case "trackingNumber":
                    resultPage = orderRepository.searchByTrackingNumber(searchDTO.getKeyword(), statuses, pageable);
                    break;
                default:
                    return new PageResponseDTO<>(List.of(), pageRequestDTO, 0);
            }
        }

        // 결과 페이지와 총 개수를 포함하여 PageResponseDTO 반환
        return new PageResponseDTO<>(resultPage.getContent(), pageRequestDTO, resultPage.getTotalElements());
    }
}
