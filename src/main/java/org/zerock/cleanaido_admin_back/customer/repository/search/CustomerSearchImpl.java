package org.zerock.cleanaido_admin_back.customer.repository.search;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;

public class CustomerSearchImpl extends QuerydslRepositorySupport implements CustomerSearch {
    public CustomerSearchImpl() {
        super(Customer.class);
    }


}
