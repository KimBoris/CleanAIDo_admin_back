package org.zerock.cleanaido_admin_back.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.order.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 배송전, 배송중, 배송완료인 주문 조회
    Page<Order> findByOrderStatusIn(List<String> statuses, Pageable pageable);

}