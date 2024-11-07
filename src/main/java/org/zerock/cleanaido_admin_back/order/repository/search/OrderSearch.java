package org.zerock.cleanaido_admin_back.order.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;

public interface OrderSearch {
    Page<OrderListDTO> searchOrders(Pageable pageable);
}
