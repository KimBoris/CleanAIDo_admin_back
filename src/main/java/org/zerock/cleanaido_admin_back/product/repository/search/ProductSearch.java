package org.zerock.cleanaido_admin_back.product.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.product.entity.Product;

public interface ProductSearch {

    PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO);

    Page<Product> searchBy(String type, String keyword, Pageable pageable);
}
