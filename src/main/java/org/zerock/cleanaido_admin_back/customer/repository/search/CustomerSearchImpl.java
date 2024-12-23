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
import org.zerock.cleanaido_admin_back.order.entity.QOrder;

import java.util.List;

public class CustomerSearchImpl extends QuerydslRepositorySupport implements CustomerSearch {

    public CustomerSearchImpl() {
        super(Customer.class);
    }

    // 고객 ID를 기준으로 고객 정보 조회
    @Override
    public CustomerReadDTO getCustomerById(String customerId) {
        QCustomer customer = QCustomer.customer;
        QOrder order = QOrder.order;

        // 고객 정보를 조회하는 쿼리 작성
        JPQLQuery<Customer> customerQuery = from(customer)
                .where(customer.customerId.eq(customerId));

        Customer customerResult = customerQuery.fetchOne();

        if (customerResult == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        // 주문 개수를 조회하는 쿼리 작성
        JPQLQuery<Long> orderCountQuery = from(order)
                .where(order.customerId.eq(customerId))
                .select(order.count());

        Long orderCount = orderCountQuery.fetchOne();

        // 조회한 데이터를 기반으로 DTO 생성
        return CustomerReadDTO.builder()
                .customerId(customerResult.getCustomerId())
                .customerPw(customerResult.getCustomerPw())
                .customerName(customerResult.getCustomerName())
                .birthDate(customerResult.getBirthDate())
                .createDate(customerResult.getCreateDate())
                .phoneNumber(customerResult.getPhoneNumber())
                .address(customerResult.getAddress())
                .profileImageUrl(customerResult.getProfileImageUrl())
                .orderCount(orderCount)
                .build();
    }

    // 모든 고객 리스트를 조회
    @Override
    public PageResponseDTO<CustomerListDTO> list(PageRequestDTO pageRequestDTO) {
        QCustomer customer = QCustomer.customer;

        // 기본 쿼리 설정
        JPQLQuery<Customer> query = from(customer).orderBy(customer.customerId.desc());

        // 페이지 정보 설정
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        getQuerydsl().applyPagination(pageable, query);

        // 조회 결과를 DTO로 변환
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

        // 페이지 응답 데이터 생성
        return PageResponseDTO.<CustomerListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    // 검색 조건을 기반으로 고객 리스트 조회
    @Override
    public PageResponseDTO<CustomerListDTO> searchBy(String type, String keyword, PageRequestDTO pageRequestDTO) {
        QCustomer customer = QCustomer.customer;

        // 기본 쿼리 설정 (삭제되지 않은 고객만 조회)
        JPQLQuery<Customer> query = from(customer).where(customer.delFlag.isFalse());

        // 검색 조건에 따른 필터링
        if (type == null || type.isEmpty()) {
            // 검색 조건이 없을 경우, 이름 또는 ID를 기준으로 검색
            BooleanBuilder builder = new BooleanBuilder();
            builder.or(customer.customerName.like("%" + keyword + "%"))
                    .or(customer.customerId.like("%" + keyword + "%"));
            query.where(builder).distinct();
        } else if (type.equals("customerName")) {
            // 이름으로 검색
            query.where(customer.customerName.like("%" + keyword + "%"));
        } else if (type.equals("customerId")) {
            // ID로 검색
            query.where(customer.customerId.like("%" + keyword + "%"));
        }

        // 정렬 조건 설정
        query.orderBy(customer.customerId.desc());

        // 페이지 정보 설정
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        getQuerydsl().applyPagination(pageable, query);

        // 조회 결과를 DTO로 변환
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

        // 페이지 응답 데이터 생성
        return PageResponseDTO.<CustomerListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
