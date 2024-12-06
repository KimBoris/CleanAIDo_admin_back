package org.zerock.cleanaido_admin_back.product.repository.search;

import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.product.dto.ReviewListDTO;

public interface ReviewSearch {

    PageResponseDTO<ReviewListDTO> list(PageRequestDTO pageRequestDTO);
}
