package org.zerock.cleanaido_admin_back.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.service.OrderService;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 배송 상태의 주문 내역 조회
    @GetMapping("/in-progress")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> getInProgressOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false) String searchType
    ) {
        SearchDTO searchDTO = SearchDTO.builder()
                .keyword(keyword)
                .searchType(searchType)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        return ResponseEntity.ok(orderService.listInProgressOrders(pageRequestDTO));
    }

    // 취소/교환/환불 상태의 주문 내역 조회
    @GetMapping("/canceled")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> getCanceledOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "status", required = false) String status // 추가된 파라미터
    ) {
        SearchDTO searchDTO = SearchDTO.builder()
                .keyword(keyword)
                .searchType(searchType)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        // 상태 필터가 없으면 전체 취소/교환/환불 목록 반환, 상태 필터가 있으면 해당 상태만 필터링
        if (status == null || status.isEmpty()) {
            return ResponseEntity.ok(orderService.listCanceledOrders(pageRequestDTO, null));
        } else {
            return ResponseEntity.ok(orderService.listCanceledOrders(pageRequestDTO, status));
        }
    }
}