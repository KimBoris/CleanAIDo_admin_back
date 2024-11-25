package org.zerock.cleanaido_admin_back.order.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.entity.Order;
import org.zerock.cleanaido_admin_back.order.entity.QOrder;
import org.zerock.cleanaido_admin_back.order.entity.QOrderDetail;
import org.zerock.cleanaido_admin_back.product.entity.QProduct;

import java.util.List;
import java.util.stream.Collectors;

public class OrderSearchImpl extends QuerydslRepositorySupport implements OrderSearch {

    public OrderSearchImpl() {
        super(Order.class);
    }

    @Override
    public Page<OrderListDTO> list(List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(order.orderStatus.in(statuses));

        return getPagedResult(condition, pageable);
    }

    @Override
    public Page<OrderListDTO> searchByOrderNumber(String keyword, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        BooleanBuilder condition = new BooleanBuilder();

        try {
            long orderNumber = Long.parseLong(keyword);
            condition.and(order.orderNumber.eq(orderNumber));
        } catch (NumberFormatException e) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        condition.and(order.orderStatus.in(statuses));

        return getPagedResult(condition, pageable);
    }

    @Override
    public Page<OrderListDTO> searchByProductNumber(String keyword, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QProduct product = QProduct.product;

        BooleanBuilder condition = new BooleanBuilder();

        try {
            int productNumber = Integer.parseInt(keyword);
            condition.and(orderDetail.productNumber.eq(productNumber));
        } catch (NumberFormatException e) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        condition.and(order.orderStatus.in(statuses));

        JPQLQuery<Order> query = from(order)
                .join(order.orderDetails, orderDetail)
                .join(orderDetail.product, product) // Product와 조인
                .where(condition)
                .distinct()
                .orderBy(order.orderNumber.desc());
        getQuerydsl().applyPagination(pageable, query);

        List<OrderListDTO> results = query.fetch().stream()
                .map(OrderListDTO::new)
                .collect(Collectors.toList());

        long total = query.fetchCount();
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<OrderListDTO> searchByCustomerId(String keyword, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(order.customerId.containsIgnoreCase(keyword))
                .and(order.orderStatus.in(statuses));

        return getPagedResult(condition, pageable);
    }

    @Override
    public Page<OrderListDTO> searchByPhoneNumber(String keyword, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(order.phoneNumber.containsIgnoreCase(keyword))
                .and(order.orderStatus.in(statuses));

        return getPagedResult(condition, pageable);
    }

    @Override
    public Page<OrderListDTO> searchByTrackingNumber(String keyword, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(order.trackingNumber.containsIgnoreCase(keyword))
                .and(order.orderStatus.in(statuses));

        return getPagedResult(condition, pageable);
    }

    // 페이징된 결과를 반환하는 메서드
    private Page<OrderListDTO> getPagedResult(BooleanBuilder condition, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QProduct product = QProduct.product;

        JPQLQuery<Order> query = from(order)
                .leftJoin(order.orderDetails, orderDetail)
                .leftJoin(orderDetail.product, product) // Product와 조인
                .where(condition)
                .distinct()
                .orderBy(order.orderNumber.desc());

        getQuerydsl().applyPagination(pageable, query);

        List<OrderListDTO> results = query.fetch().stream()
                .map(OrderListDTO::new)
                .collect(Collectors.toList());

        long total = query.fetchCount();
        return new PageImpl<>(results, pageable, total);
    }
}
