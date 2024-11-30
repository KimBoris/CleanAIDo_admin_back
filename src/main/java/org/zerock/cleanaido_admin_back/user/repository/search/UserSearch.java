package org.zerock.cleanaido_admin_back.user.repository.search;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.product.dto.ProductListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserReadDTO;
import org.zerock.cleanaido_admin_back.user.entity.User;

public interface UserSearch {
    PageResponseDTO<UserListDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<UserListDTO> searchBy(String type, String keyword, PageRequestDTO pageRequestDTO);

    UserReadDTO getUserById(String userId);

}
