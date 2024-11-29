package org.zerock.cleanaido_admin_back.customer.repository.search;

import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;

public interface CustomerSearch {
    PageResponseDTO<CustomerListDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<CustomerListDTO> searchByName(String type, String key, PageRequestDTO pageRequestDTO);
}
