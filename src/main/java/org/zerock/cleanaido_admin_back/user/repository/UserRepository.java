package org.zerock.cleanaido_admin_back.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.cleanaido_admin_back.user.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}