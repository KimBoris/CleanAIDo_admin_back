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
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/in-progress")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> getInProgressOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false) String searchType
    ) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(new SearchDTO(keyword, searchType))
                .build();

        List<String> inProgressStatuses = List.of("배송전", "배송중", "배송완료", "주문 완료");
        return ResponseEntity.ok(orderService.listOrders(pageRequestDTO, inProgressStatuses));
    }

    @GetMapping("/canceled")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> getCanceledOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "status", required = false) String status
    ) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(new SearchDTO(keyword, searchType))
                .build();

        List<String> canceledStatuses;

        // 특정 상태가 지정되었을 때 그 상태만 포함, 그렇지 않으면 취소, 교환, 환불 모두 포함
        if (status != null && !status.isEmpty()) {
            canceledStatuses = List.of(status);
        } else {
            canceledStatuses = List.of("취소", "교환", "환불");
        }

        return ResponseEntity.ok(orderService.listOrders(pageRequestDTO, canceledStatuses));
    }

}
