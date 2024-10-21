package org.zerock.cleanaido_admin_back.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
