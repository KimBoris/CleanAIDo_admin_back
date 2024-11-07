package org.zerock.cleanaido_admin_back.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.entity.Order;
import org.zerock.cleanaido_admin_back.order.repository.OrderRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Page<OrderListDTO> getInProgressOrders(Pageable pageable) {
        List<String> inProgressStatuses = Arrays.asList("배송전", "배송중", "배송완료");
        return orderRepository.findByOrderStatusIn(inProgressStatuses, pageable)
                .map(this::toOrderListDTO);
    }

    public Page<OrderListDTO> getCanceledOrders(Pageable pageable) {
        List<String> canceledStatuses = Arrays.asList("취소", "교환", "환불");
        return orderRepository.findByOrderStatusIn(canceledStatuses, pageable)
                .map(this::toOrderListDTO);
    }

    private OrderListDTO toOrderListDTO(Order order) {
        return OrderListDTO.builder()
                .orderNumber(order.getOrderNumber())
                .productNumber(order.getProductNumber())
                .customerId(order.getCustomerId())
                .phoneNumber(order.getPhoneNumber())
                .deliveryAddress(order.getDeliveryAddress())
                .totalPrice(order.getTotalPrice())
                .trackingNumber(order.getTrackingNumber())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .build();
    }

}