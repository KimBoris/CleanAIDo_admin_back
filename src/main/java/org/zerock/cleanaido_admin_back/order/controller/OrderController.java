package org.zerock.cleanaido_admin_back.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderDeliveryUpdateDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderDetailListDTO;
import org.zerock.cleanaido_admin_back.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    /**
     * 진행 중인 주문 조회 (권한별)
     *
     * @param page       현재 페이지 번호 (기본값: 1)
     * @param size       페이지 크기 (기본값: 10)
     * @param keyword    검색 키워드 (옵션)
     * @param searchType 검색 유형 (옵션)
     * @param userId     사용자 ID
     * @param role       사용자 역할 (예: 관리자, 판매자)
     * @return 진행 중인 주문의 페이지 응답
     */
    @GetMapping("/in-progress")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> getInProgressOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "role") String role
    ) {
        // 페이지 요청 DTO 생성
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(new SearchDTO(keyword, searchType))
                .build();

        // 진행 중인 상태 리스트 정의
        List<String> inProgressStatuses = List.of("배송전", "배송중", "배송완료", "주문완료");

        // 서비스 호출을 통해 주문 리스트 가져오기
        PageResponseDTO<OrderListDTO> response = orderService.listOrdersByRole(pageRequestDTO, inProgressStatuses, userId, role);

        return ResponseEntity.ok(response);
    }

    /**
     * 취소/교환/환불된 주문 조회 (권한별)
     *
     * @param page       현재 페이지 번호 (기본값: 1)
     * @param size       페이지 크기 (기본값: 10)
     * @param keyword    검색 키워드 (옵션)
     * @param searchType 검색 유형 (옵션)
     * @param status     특정 상태 필터 (옵션)
     * @param userId     사용자 ID
     * @param role       사용자 역할 (예: 관리자, 판매자)
     * @return 취소/교환/환불된 주문의 페이지 응답
     */
    @GetMapping("/canceled")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> getCanceledOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "role") String role
    ) {
        // 페이지 요청 DTO 생성
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(new SearchDTO(keyword, searchType))
                .build();

        // 취소/교환/환불 상태 리스트 정의
        List<String> canceledStatuses = (status != null && !status.isEmpty())
                ? List.of(status) // 특정 상태가 주어진 경우
                : List.of("취소", "교환", "환불");

        // 서비스 호출을 통해 주문 리스트 가져오기
        PageResponseDTO<OrderListDTO> response = orderService.listOrdersByRole(pageRequestDTO, canceledStatuses, userId, role);

        return ResponseEntity.ok(response);
    }

    /**
     * 주문 상세 조회
     *
     * @param page      현재 페이지 번호 (기본값: 1)
     * @param size      페이지 크기 (기본값: 10)
     * @param orderNum  주문 번호
     * @return 주문 상세 정보의 페이지 응답
     */
    @GetMapping("/detail/{orderNum}")
    public ResponseEntity<PageResponseDTO<OrderDetailListDTO>> getOrderDetail(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable Long orderNum) {

        // 인증된 사용자의 ID 가져오기 (판매자)
        String sellerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 페이지 요청 DTO 생성
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .build();

        // 서비스 호출을 통해 주문 상세 정보 가져오기
        PageResponseDTO<OrderDetailListDTO> response = orderService.listOrderDetail(sellerId, orderNum, pageRequestDTO);

        log.info("==============================");
        log.info(response.toString());
        log.info("==============================");

        return ResponseEntity.ok(response);
    }

    /**
     * 배송 상태 업데이트
     *
     * @param orderDeliveryUpdateList 배송 상태 업데이트를 위한 DTO 리스트
     * @return 처리 결과 메시지
     */
    @PutMapping("/delivery")
    public ResponseEntity<String> updataDeliveryStatus(
            @RequestBody List<OrderDeliveryUpdateDTO> orderDeliveryUpdateList) {

        try {
            // 서비스 호출을 통해 배송 상태 업데이트
            String response = orderService.updateOrderDeliveryStatus(orderDeliveryUpdateList);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 잘못된 요청 처리
            return ResponseEntity.badRequest().body("요청 실패");
        }
    }
}
