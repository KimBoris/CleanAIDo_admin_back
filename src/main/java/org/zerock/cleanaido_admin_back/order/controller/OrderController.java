package org.zerock.cleanaido_admin_back.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.service.OrderService;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/in-progress")
    public ResponseEntity<Page<OrderListDTO>> getInProgressOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getInProgressOrders(pageable));
    }

    @GetMapping("/canceled")
    public ResponseEntity<Page<OrderListDTO>> getCanceledOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getCanceledOrders(pageable));
    }
}
