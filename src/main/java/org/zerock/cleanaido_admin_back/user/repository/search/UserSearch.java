package org.zerock.cleanaido_admin_back.user.repository.search;


import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;

public interface UserSearch {
    PageResponseDTO<UserListDTO> list(PageRequestDTO pageRequestDTO);

}
