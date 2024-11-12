package org.zerock.cleanaido_admin_back.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.order.entity.Order;
import org.zerock.cleanaido_admin_back.order.repository.search.OrderSearch;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderSearch {
    Page<Order> findByOrderStatusIn(List<String> statuses, Pageable pageable);
}