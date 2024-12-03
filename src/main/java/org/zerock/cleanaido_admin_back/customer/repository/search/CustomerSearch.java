package org.zerock.cleanaido_admin_back.customer.repository.search;

import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerReadDTO;

public interface CustomerSearch {
    PageResponseDTO<CustomerListDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<CustomerListDTO> searchBy(String type, String key, PageRequestDTO pageRequestDTO);

    CustomerReadDTO getCustomerById(String customerId);
}
