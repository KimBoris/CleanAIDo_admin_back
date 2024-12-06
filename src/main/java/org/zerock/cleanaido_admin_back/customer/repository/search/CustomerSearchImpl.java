package org.zerock.cleanaido_admin_back.customer.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerReadDTO;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;
import org.zerock.cleanaido_admin_back.customer.entity.QCustomer;
import org.zerock.cleanaido_admin_back.order.dto.OrderListDTO;
import org.zerock.cleanaido_admin_back.order.entity.Order;
import org.zerock.cleanaido_admin_back.order.entity.QOrder;

import java.nio.channels.IllegalChannelGroupException;
import java.util.List;

public class CustomerSearchImpl extends QuerydslRepositorySupport implements CustomerSearch {
    public CustomerSearchImpl() {
        super(Customer.class);
    }

    @Override
    public CustomerReadDTO getCustomerById(String customerId) {
        QCustomer customer = QCustomer.customer;
        QOrder order = QOrder.order;

        // 고객 정보 조회
        JPQLQuery<Customer> customerQuery = from(customer)
                .where(customer.customerId.eq(customerId));

        Customer customerResult = customerQuery.fetchOne();

        if (customerResult == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        // 주문 개수 조회
        JPQLQuery<Long> orderCountQuery = from(order)
                .where(order.customerId.eq(customerId))
                .select(order.count());

        Long orderCount = orderCountQuery.fetchOne();

        // 결과 DTO 빌드
        return CustomerReadDTO.builder()
                .customerId(customerResult.getCustomerId())
                .customerPw(customerResult.getCustomerPw())
                .customerName(customerResult.getCustomerName())
                .birthDate(customerResult.getBirthDate())
                .createDate(customerResult.getCreateDate())
                .phoneNumber(customerResult.getPhoneNumber())
                .address(customerResult.getAddress())
                .profileImageUrl(customerResult.getProfileImageUrl())
                .orderCount(orderCount).build();

    }

    @Override
    public PageResponseDTO<CustomerListDTO> list(PageRequestDTO pageRequestDTO) {
        QCustomer customer = QCustomer.customer;

        JPQLQuery<Customer> query = from(customer).orderBy(customer.customerId.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<CustomerListDTO> results =
                query.select(
                        Projections.bean(
                                CustomerListDTO.class,
                                customer.customerId,
                                customer.customerPw,
                                customer.customerName,
                                customer.birthDate,
                                customer.createDate,
                                customer.updatedDate,
                                customer.phoneNumber,
                                customer.delFlag,
                                customer.address,
                                customer.profileImageUrl
                        )
                );

        List<CustomerListDTO> dtoList = results.fetch();
        long total = query.fetchCount();
        ;

        return PageResponseDTO.<CustomerListDTO>withAll().
                dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public PageResponseDTO<CustomerListDTO> searchBy(String type, String keyword, PageRequestDTO pageRequestDTO) {
        keyword = pageRequestDTO.getSearchDTO().getKeyword();
        type = pageRequestDTO.getSearchDTO().getSearchType();

        QCustomer customer = QCustomer.customer;

        JPQLQuery<Customer> query = from(customer).where(customer.delFlag.isFalse());

        if (type == null || type.isEmpty()) {
            BooleanBuilder builder = new BooleanBuilder();
            builder.or(customer.customerName.like("%" + keyword + "%"))
                    .or(customer.customerId.like("%" + keyword + "%"));
            query.where(builder).distinct();
        } else if (type.equals("customerName")) {
            query.where(customer.customerName.like("%" + keyword + "%"));
        } else if (type.equals("customerId")) {
            query.where(customer.customerId.like("%" + keyword + "%"));
        }

        query.orderBy(customer.customerId.desc());

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<CustomerListDTO> results =
                query.select(
                        Projections.bean(
                                CustomerListDTO.class
                                , customer.customerId
                                , customer.customerPw
                                , customer.customerName
                                , customer.birthDate
                                , customer.createDate
                                , customer.updatedDate
                                , customer.phoneNumber
                                , customer.delFlag
                                , customer.address
                                , customer.profileImageUrl
                        )
                );

        List<CustomerListDTO> dtoList = results.fetch();

        long total = query.fetchCount();

        return PageResponseDTO.<CustomerListDTO>withAll().
                dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
