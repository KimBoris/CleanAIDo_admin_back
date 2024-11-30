package org.zerock.cleanaido_admin_back.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserReadDTO;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResponseDTO<UserListDTO> listUsers(PageRequestDTO pageRequestDTO) {

        if (pageRequestDTO.getPage() < 1) {
            throw new IllegalArgumentException("페이지 번호는 1이상 이어야 합니다.");
        }

        PageResponseDTO<UserListDTO> response = userRepository.list(pageRequestDTO);

        log.info("response " + response);

        return response;
    }

    // 이메일과 비밀번호로 사용자 인증
    // 로그인 할 떄 사용.
    public User authenticate(String userId, String rawPassword) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    public PageResponseDTO<UserListDTO> search(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getSearchDTO().getSearchType();
        String keyword = pageRequestDTO.getSearchDTO().getKeyword();

        return userRepository.searchBy(type, keyword, pageRequestDTO);
    }



    public UserReadDTO getUser(String userId) {
        UserReadDTO userReadDTO = userRepository.getUserById(userId);

        if (userReadDTO == null) {
            log.info("No User");
            throw new EntityNotFoundException("유저를 찾을 수 없습니다.");
        }
        return userReadDTO;
    }

    public String softDeleteUser(String userId)
    {
        User user =userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId + "를 찾을 수 없습니다."));

        user.setDelFlag(true);
        userRepository.save(user);

        return userId  +"가 삭제되었습니다.";
    }
}
