package org.zerock.cleanaido_admin_back.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.product.repository.search.ProductSearch;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

    @Override
    Page<Product> searchBy(String type, String keyword, Pageable pageable);
}
