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
        // SearchDTO와 PageRequestDTO 생성
        SearchDTO searchDTO = SearchDTO.builder()
                .keyword(keyword)
                .searchType(searchType)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        // 검색어가 없는 경우 목록 반환, 있는 경우 검색 결과 반환
        if (searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            return ResponseEntity.ok(orderService.listInProgressOrders(pageRequestDTO));
        } else {
            return ResponseEntity.ok(orderService.searchInProgressOrders(pageRequestDTO));
        }
    }

    // 취소/교환/환불 상태의 주문 내역 조회
    @GetMapping("/canceled")
    public ResponseEntity<PageResponseDTO<OrderListDTO>> getCanceledOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false) String searchType
    ) {
        // SearchDTO와 PageRequestDTO 생성
        SearchDTO searchDTO = SearchDTO.builder()
                .keyword(keyword)
                .searchType(searchType)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        // 검색어가 없는 경우 목록 반환, 있는 경우 검색 결과 반환
        if (searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            return ResponseEntity.ok(orderService.listCanceledOrders(pageRequestDTO));
        } else {
            return ResponseEntity.ok(orderService.searchCanceledOrders(pageRequestDTO));
        }
    }
}
