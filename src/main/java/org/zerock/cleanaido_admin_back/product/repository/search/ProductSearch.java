package org.zerock.cleanaido_admin_back.product.repository.search;

import org.springframework.data.domain.PageRequest;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;

public interface ProductSearch {

    PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO);
}
