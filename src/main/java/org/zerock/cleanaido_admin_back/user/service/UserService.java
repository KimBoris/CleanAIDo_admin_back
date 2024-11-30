package org.zerock.cleanaido_admin_back.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.UploadOneDTO;
import org.zerock.cleanaido_admin_back.common.util.CustomFileUtil;
import org.zerock.cleanaido_admin_back.user.dto.UserListDTO;
import org.zerock.cleanaido_admin_back.user.dto.UserRegisterDTO;
import org.zerock.cleanaido_admin_back.user.entity.User;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;

import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomFileUtil customFileUtil;

    public PageResponseDTO<UserListDTO> listUsers(PageRequestDTO pageRequestDTO) {

        if (pageRequestDTO.getPage() < 1) {
            throw new IllegalArgumentException("페이지 번호는 1이상 이어야 합니다.");
        }

        PageResponseDTO<UserListDTO> response = userRepository.list(pageRequestDTO);

        log.info("response " + response);

        return response;
    }

    // 이메일과 비밀번호로 사용자 인증
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

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        PageResponseDTO<UserListDTO> resultPage = userRepository.searchBy(type, keyword, pageRequestDTO);

        List<UserListDTO> dtoList = resultPage.getDtoList().stream()
                .map(user ->
                        UserListDTO.builder().
                                userId(user.getUserId()).
                                password(user.getPassword()).
                                businessName(user.getBusinessName()).
                                businessType(user.getBusinessType()).
                                ownerName(user.getOwnerName()).
                                businessAddress(user.getBusinessAddress()).
                                businessStatus(user.getBusinessStatus()).
                                businessCategory(user.getBusinessCategory()).
                                storeName(user.getStoreName()).
                                commerceLicenseNum(user.getCommerceLicenseNum()).
                                businessLicenseFile(user.getBusinessLicenseFile()).
                                originAddress(user.getOriginAddress()).
                                contactNumber(user.getContactNumber()).
                                accountNumber(user.getAccountNumber()).
                                userStatus(user.getUserStatus()).
                                delFlag(user.isDelFlag()).
                                adminRole(user.isAdminRole()).
                                createDate(user.getCreateDate())
                                .build()).collect(Collectors.toList());

            return userRepository.searchBy(type, keyword, pageRequestDTO);
    }


    public String registUser(UserRegisterDTO userRegisterDTO, UploadOneDTO uploadOneDTO) {

        User user = User.builder()
                .userId(userRegisterDTO.getUserId())
                .password(userRegisterDTO.getPassword())
                .businessNumber(userRegisterDTO.getBusinessNumber())
                .businessName(userRegisterDTO.getBusinessName())
                .businessType(userRegisterDTO.getBusinessType())
                .ownerName(userRegisterDTO.getOwnerName())
                .businessAddress(userRegisterDTO.getBusinessAddress())
                .businessStatus(userRegisterDTO.getBusinessStatus())
                .businessCategory(userRegisterDTO.getBusinessCategory())
                .storeName(userRegisterDTO.getStoreName())
                .commerceLicenseNum(userRegisterDTO.getCommerceLicenseNum())
                .originAddress(userRegisterDTO.getOriginAddress())
                .contactNumber(userRegisterDTO.getContactNumber())
                .accountNumber(userRegisterDTO.getAccountNumber())
                .userStatus("입점요청")
                .delFlag(false)
                .adminRole(false)
                .businessLicenseFile(Optional.ofNullable(uploadOneDTO.getFile())
                        .filter(file -> !file.isEmpty())
                        .map(customFileUtil::saveFile)
                        .orElse(null)) // 파일명 저장
                .build();

        userRepository.save(user);

        return user.getUserId();
    }

}
