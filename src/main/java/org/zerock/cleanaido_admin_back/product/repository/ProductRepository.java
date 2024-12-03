//package org.zerock.cleanaido_admin_back.product.repository;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.zerock.cleanaido_admin_back.category.dto.CategoryDTO;
//import org.zerock.cleanaido_admin_back.product.dto.ProductReadDTO;
//import org.zerock.cleanaido_admin_back.product.entity.Product;
//import org.zerock.cleanaido_admin_back.product.repository.search.ProductSearch;
//
//import java.util.List;
//
//public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
//
//    @Override
//    Page<Product> searchBy(String type, String keyword, Pageable pageable);
//
//    @Override
//    List<CategoryDTO> searchCategoryBy(String keyword);
//
//}
