package org.zerock.cleanaido_admin_back.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;
import org.zerock.cleanaido_admin_back.customer.repository.search.CustomerSearch;

public interface CustomerRepository extends JpaRepository<Customer, String>, CustomerSearch {
}
