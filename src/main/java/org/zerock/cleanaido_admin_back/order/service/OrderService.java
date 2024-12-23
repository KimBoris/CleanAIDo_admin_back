package org.zerock.cleanaido_admin_back.order.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderDeliveryUpdateDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderDetailListDTO;
import org.zerock.cleanaido_admin_back.order.entity.Order;
import org.zerock.cleanaido_admin_back.order.repository.OrderRepository;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * 역할에 따른 주문 리스트 조회
     *
     * @param pageRequestDTO 페이지 요청 정보 (페이지 번호, 크기, 검색 조건 포함)
     * @param statuses       조회할 주문 상태 리스트
     * @param userId         사용자 ID
     * @param role           사용자 역할 (ROLE_ADMIN 또는 ROLE_SELLER)
     * @return 주문 리스트와 페이지 정보
     */
    public PageResponseDTO<OrderListDTO> listOrdersByRole(PageRequestDTO pageRequestDTO, List<String> statuses, String userId, String role) {
        Page<OrderListDTO> resultPage;
        Pageable pageable = pageRequestDTO.toPageable(); // 페이지 정보 변환
        SearchDTO searchDTO = pageRequestDTO.getSearchDTO(); // 검색 조건 추출

        if (searchDTO == null || searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            // 검색 조건이 없는 경우
            if ("ROLE_ADMIN".equals(role)) {
                // 관리자는 모든 주문 조회
                resultPage = orderRepository.list(statuses, pageable);
            } else if ("ROLE_SELLER".equals(role)) {
                // 판매자는 자신의 상품과 관련된 주문 조회
                resultPage = orderRepository.listBySeller(userId, statuses, pageable);
            } else {
                // 잘못된 역할 처리
                throw new IllegalArgumentException("잘못된 권한입니다. ROLE_ADMIN 또는 ROLE_SELLER만 허용됩니다.");
            }
        } else {
            // 검색 조건이 있는 경우 검색 로직 실행
            resultPage = searchOrders(searchDTO, statuses, pageable);
        }

        // 페이지 응답 DTO 생성 및 반환
        return new PageResponseDTO<>(resultPage.getContent(), pageRequestDTO, resultPage.getTotalElements());
    }

    /**
     * 검색 조건에 따른 주문 검색
     *
     * @param searchDTO 검색 조건 (검색 유형 및 키워드)
     * @param statuses  조회할 주문 상태 리스트
     * @param pageable  페이지 정보
     * @return 검색 결과 페이지
     */
    private Page<OrderListDTO> searchOrders(SearchDTO searchDTO, List<String> statuses, Pageable pageable) {
        switch (searchDTO.getSearchType()) {
            case "orderNumber":
                return orderRepository.searchByOrderNumber(searchDTO.getKeyword(), statuses, pageable); // 주문 번호 검색
            case "productNumber":
                return orderRepository.searchByProductNumber(searchDTO.getKeyword(), statuses, pageable); // 상품 번호 검색
            case "customerId":
                return orderRepository.searchByCustomerId(searchDTO.getKeyword(), statuses, pageable); // 고객 ID 검색
            case "phoneNumber":
                return orderRepository.searchByPhoneNumber(searchDTO.getKeyword(), statuses, pageable); // 전화번호 검색
            case "trackingNumber":
                return orderRepository.searchByTrackingNumber(searchDTO.getKeyword(), statuses, pageable); // 운송장 번호 검색
            default:
                return Page.empty(pageable); // 기본값: 빈 결과 반환
        }
    }

    /**
     * 특정 주문의 상세 정보 조회
     *
     * @param sellerId  판매자 ID
     * @param orderNum  주문 번호
     * @param pageRequestDTO 페이지 요청 정보
     * @return 주문 상세 정보와 페이지 정보
     */
    public PageResponseDTO<OrderDetailListDTO> listOrderDetail(String sellerId, Long orderNum, PageRequestDTO pageRequestDTO) {
        // 주문 상세 정보 조회
        PageResponseDTO<OrderDetailListDTO> results = orderRepository.getOrderDetailList(sellerId, orderNum, pageRequestDTO);
        return results;
    }

    /**
     * 주문 상태 변경 및 송장 번호 업데이트
     *
     * @param orderDeliveryUpdateList 배송 상태 업데이트 DTO 리스트
     * @return 처리 결과 메시지
     */
    public String updateOrderDeliveryStatus(List<OrderDeliveryUpdateDTO> orderDeliveryUpdateList) {
        for (OrderDeliveryUpdateDTO dto : orderDeliveryUpdateList) {
            Long orderNumber = Long.valueOf(dto.getOrderNumber()); // 주문 번호 가져오기
            String trackingNumber = dto.getTrackingNumber(); // 송장 번호 가져오기

            // 주문 엔티티 조회 (없을 경우 예외 발생)
            Order order = orderRepository.findById(orderNumber).orElseThrow(() ->
                    new EntityNotFoundException(orderNumber + "를 찾을 수 없습니다."));

            // 주문 상태 및 송장 번호 업데이트
            order.setTrackingNumber(trackingNumber);
            order.setOrderStatus("배송완료");
            orderRepository.save(order); // 변경 사항 저장
        }

        return "응답 성공";
    }
}
