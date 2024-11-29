package org.zerock.cleanaido_admin_back.customer.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;
import org.zerock.cleanaido_admin_back.customer.entity.QCustomer;

import java.util.List;

public class CustomerSearchImpl extends QuerydslRepositorySupport implements CustomerSearch {
    public CustomerSearchImpl() {
        super(Customer.class);
    }

    @Override
    public PageResponseDTO<CustomerListDTO> searchBy(String type, String key, PageRequestDTO pageRequestDTO) {




        return null;
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


}
