package org.zerock.cleanaido_admin_back.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Page<OrderListDTO> getOrderList(Pageable pageable) {
        return orderRepository.searchOrders(pageable);
    }
}
