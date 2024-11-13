package org.zerock.cleanaido_admin_back.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.order.entity.Order;
import org.zerock.cleanaido_admin_back.order.repository.search.OrderSearch;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderSearch {
}
