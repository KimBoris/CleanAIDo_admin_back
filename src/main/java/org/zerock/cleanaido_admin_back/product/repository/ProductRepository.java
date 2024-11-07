package org.zerock.cleanaido_admin_back.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.product.entity.Product;
import org.zerock.cleanaido_admin_back.product.repository.search.ProductSearch;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

}
