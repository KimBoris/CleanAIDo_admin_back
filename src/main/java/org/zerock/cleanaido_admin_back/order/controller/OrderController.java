package org.zerock.cleanaido_admin_back.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 진행 중인 주문 조회 (권한별)
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
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(new SearchDTO(keyword, searchType))
                .build();

        List<String> inProgressStatuses = List.of("배송전", "배송중", "배송완료", "주문 완료");
        PageResponseDTO<OrderListDTO> response = orderService.listOrdersByRole(pageRequestDTO, inProgressStatuses, userId, role);

        return ResponseEntity.ok(response);
    }

    /**
     * 취소/교환/환불된 주문 조회 (권한별)
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
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(new SearchDTO(keyword, searchType))
                .build();

        List<String> canceledStatuses;
        if (status != null && !status.isEmpty()) {
            canceledStatuses = List.of(status);
        } else {
            canceledStatuses = List.of("취소", "교환", "환불");
        }

        PageResponseDTO<OrderListDTO> response = orderService.listOrdersByRole(pageRequestDTO, canceledStatuses, userId, role);

        return ResponseEntity.ok(response);
    }
}
