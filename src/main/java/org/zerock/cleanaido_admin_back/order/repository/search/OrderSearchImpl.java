package org.zerock.cleanaido_admin_back.order.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderDetailListDTO;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.entity.*;
import org.zerock.cleanaido_admin_back.product.entity.QProduct;
import org.zerock.cleanaido_admin_back.user.entity.QUser;

import java.util.List;
import java.util.stream.Collectors;

public class OrderSearchImpl extends QuerydslRepositorySupport implements OrderSearch {

    public OrderSearchImpl() {
        super(Order.class);
    }

    //판매자별 주문 리스트 가져오기
    @Override
    public Page<OrderListDTO> listBySeller(String sellerId, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QProduct product = QProduct.product;

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(product.seller.userId.eq(sellerId)) // 판매자 ID 조건
                .and(order.orderStatus.in(statuses));     // 주문 상태 조건

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

    //주문리스트 가져오기
    @Override
    public Page<OrderListDTO> list(List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(order.orderStatus.in(statuses));

        return getPagedResult(condition, pageable);
    }

    //주문 번호로 검색
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

    //상품 번호로 검색
    @Override
    public Page<OrderListDTO> searchByProductNumber(String keyword, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QProduct product = QProduct.product;

        BooleanBuilder condition = new BooleanBuilder();

        try {
            long productNumber = Long.parseLong(keyword); // Product의 productNumber 조건 추가
            condition.and(product.pno.eq(productNumber)); // Product의 pno 기준 검색
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

    //고객 아이디로 검색
    @Override
    public Page<OrderListDTO> searchByCustomerId(String keyword, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(order.customerId.containsIgnoreCase(keyword))
                .and(order.orderStatus.in(statuses));

        return getPagedResult(condition, pageable);
    }

    //고객 전화번호로 검색
    @Override
    public Page<OrderListDTO> searchByPhoneNumber(String keyword, List<String> statuses, Pageable pageable) {
        QOrder order = QOrder.order;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(order.phoneNumber.containsIgnoreCase(keyword))
                .and(order.orderStatus.in(statuses));

        return getPagedResult(condition, pageable);
    }

    //트래킹 번호로 검색
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

    public PageResponseDTO<OrderDetailListDTO> getOrderDetailList
            (String sellerId, Long orderNum, PageRequestDTO pageRequestDTO) {
        QOrder order = QOrder.order;
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QProduct product = QProduct.product;
        QUser user = QUser.user;

        JPQLQuery<OrderDetail> query = from(orderDetail)
                .leftJoin(orderDetail.order, order)
                .leftJoin(orderDetail.product, product)
                .leftJoin(product.seller, user)
                .where(user.userId.eq(sellerId))
                .where(orderDetail.order.orderNumber.eq(orderNum));

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<OrderDetailListDTO> results = query.select(
                Projections.bean(
                        OrderDetailListDTO.class,
                        product.pcode,
                        product.pname,
                        orderDetail.quantity,
                        product.price,
                        Expressions.as(
                                product.price.multiply(orderDetail.quantity),
                                "totalPrice"
                        )
                )
        );

        List<OrderDetailListDTO> dtoList = results.fetch();
        long total = query.fetchCount();

        return PageResponseDTO.<OrderDetailListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
