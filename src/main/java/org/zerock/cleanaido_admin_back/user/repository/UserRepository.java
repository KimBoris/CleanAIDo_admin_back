package org.zerock.cleanaido_admin_back.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.repository.search.UserSearch;

public interface UserRepository extends JpaRepository<User, String> , UserSearch {
    boolean existsByUserId(String userId);
}