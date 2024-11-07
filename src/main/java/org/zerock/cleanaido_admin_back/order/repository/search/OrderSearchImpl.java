package org.zerock.cleanaido_admin_back.order.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.entity.Order;
import org.zerock.cleanaido_admin_back.order.entity.QOrder;

import java.util.List;

public class OrderSearchImpl extends QuerydslRepositorySupport implements OrderSearch {

    public OrderSearchImpl() {
        super(Order.class);
    }

    @Override
    public Page<OrderListDTO> searchOrders(Pageable pageable) {
        QOrder order = QOrder.order;

        JPQLQuery<Order> query = from(order);
        query.orderBy(order.orderNumber.desc());

        JPQLQuery<OrderListDTO> dtoQuery = query.select(
                Projections.bean(OrderListDTO.class,
                        order.orderNumber,
                        order.productNumber,
                        order.customerId,
                        order.phoneNumber,
                        order.deliveryAddress,
                        order.deliveryMessage,
                        order.totalPrice,
                        order.orderDate,
                        order.trackingNumber,
                        order.orderStatus)
        );

        List<OrderListDTO> dtoList = getQuerydsl().applyPagination(pageable, dtoQuery).fetch();
        long total = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, total);
    }
}