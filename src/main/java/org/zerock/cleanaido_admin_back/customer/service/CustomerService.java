package org.zerock.cleanaido_admin_back.customer.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerReadDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerRegisterDTO;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;
import org.zerock.cleanaido_admin_back.customer.repository.CustomerRepository;

@Service
@RequiredArgsConstructor // 생성자 주입을 자동으로 생성해주는 Lombok 애노테이션
@Log4j2 // Log4j2 로깅 사용
@Transactional // 트랜잭션 관리를 제공하는 Spring 애노테이션
public class CustomerService {
    private final CustomerRepository customerRepository; // CustomerRepository 의존성
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 PasswordEncoder 의존성

    // 고객 인증 메서드: 아이디와 비밀번호를 확인
    public Customer authenticate(String customerId, String rawPassword) {
        Customer customer = customerRepository.findById(customerId).orElse(null); // 고객 ID로 검색

        // 고객이 존재하고 입력된 비밀번호가 일치하면 해당 고객 반환
        if (customer != null && passwordEncoder.matches(rawPassword, customer.getCustomerPw())) {
            return customer;
        }
        return null; // 인증 실패 시 null 반환
    }

    // 고객 리스트 조회 메서드: 페이징 처리를 지원
    public PageResponseDTO<CustomerListDTO> listCustomers(PageRequestDTO pageRequestDTO) {
        // 페이지 번호가 1 미만이면 예외를 발생
        if (pageRequestDTO.getPage() < 1) {
            throw new IllegalArgumentException("페이지 번호는 1이상 이어야 합니다.");
        }

        return customerRepository.list(pageRequestDTO); // 레포지토리에서 리스트 조회
    }

    // 검색 기능 메서드: 검색 타입과 키워드를 기반으로 고객 목록 조회
    public PageResponseDTO<CustomerListDTO> search(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getSearchDTO().getSearchType(); // 검색 타입
        String keyword = pageRequestDTO.getSearchDTO().getKeyword(); // 검색 키워드

        return customerRepository.searchBy(type, keyword, pageRequestDTO); // 검색 결과 반환
    }

    // 고객 소프트 삭제 및 복구 메서드
    public String softDeleteCustomer(String customerId) {
        // 고객 ID로 검색, 없을 경우 예외 발생
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(customerId + "를 찾을 수 없습니다."));

        log.info(customer); // 고객 정보 로그 출력

        customer.toggleDelflag(); // 삭제 상태 토글 (삭제 또는 복구)

        customerRepository.save(customer); // 변경 사항 저장

        // 삭제 상태에 따라 적절한 메시지 반환
        if (customer.isDelFlag()) {
            return customerId + "가 삭제되었습니다.";
        }
        return customerId + "가 복구되었습니다.";
    }

    // 특정 고객 정보 조회 메서드
    public CustomerReadDTO getCustomer(String customerId) {
        CustomerReadDTO customerReadDTO = customerRepository.getCustomerById(customerId); // 고객 DTO 조회

        log.info("CustomerReadDTO = " + customerReadDTO); // 조회된 고객 정보 로그 출력

        // 고객이 없을 경우 예외 발생
        if (customerReadDTO == null) {
            throw new EntityNotFoundException("고객을 찾을 수 없습니다.");
        }
        return customerReadDTO; // 조회된 고객 정보 반환
    }

    // 고객 비밀번호 업데이트 메서드
    public String updateCustomer(String customerId, CustomerRegisterDTO customerRegisterDTO) {
        // 고객 ID로 검색, 없을 경우 예외 발생
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(customerId + "를 찾을 수 없습니다."));

        customer.setCustomerPw(customerRegisterDTO.getCustomerPw()); // 비밀번호 변경
        customerRepository.save(customer); // 변경 사항 저장

        return customer.getCustomerId() + "의 비밀번호가 변경되었습니다."; // 결과 메시지 반환
    }
}
