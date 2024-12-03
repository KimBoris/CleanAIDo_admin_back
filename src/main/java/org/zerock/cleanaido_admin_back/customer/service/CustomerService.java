package org.zerock.cleanaido_admin_back.customer.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerLoginDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerReadDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerRegisterDTO;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;
import org.zerock.cleanaido_admin_back.customer.repository.CustomerRepository;
import org.zerock.cleanaido_admin_back.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public Customer authenticate(String customerId, String rawPassword) {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer != null && passwordEncoder.matches(rawPassword, customer.getCustomerPw())) {
            return customer;

        }
        return null;
    }


    public PageResponseDTO<CustomerListDTO> listCustomers(PageRequestDTO pageRequestDTO) {
        if (pageRequestDTO.getPage() < 1) {
            throw new IllegalArgumentException("페이지 번호는 1이상 이어야 합니다.");
        }

        return customerRepository.list(pageRequestDTO);
    }


    public PageResponseDTO<CustomerListDTO> search(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getSearchDTO().getSearchType();
        String keyword = pageRequestDTO.getSearchDTO().getKeyword();

        return customerRepository.searchBy(type, keyword, pageRequestDTO);
    }

    public String softDeleteCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException(customerId + "를 찾을 수 없습니다."));

        log.info(customer);

        customer.toggleDelflag();

        customerRepository.save(customer);

        if (customer.isDelFlag()) {
            return customerId + "가 삭제되었습니다.";
        }
        return customerId+ "가 복구되었습니다." ;
    }

    public CustomerReadDTO getCustomer(String customerId) {
        CustomerReadDTO customerReadDTO = customerRepository.getCustomerById(customerId);

        log.info("CustomerReadDTO = "+customerReadDTO);

        if (customerReadDTO == null) {
            throw new EntityNotFoundException("고객을 찾을 수 없습니다.");
        }
        return customerReadDTO;
    }

    public String updateCustomer(String customerId, CustomerRegisterDTO customerRegisterDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(customerId + "를 찾을 수 없습니다."));

        customer.setCustomerPw(customerRegisterDTO.getCustomerPw());
        customerRepository.save(customer);

        return customer.getCustomerId() + "의 비밀번호가 변경되었습니다.";  // 비즈니스 로직 처리 후 메시지 반환
    }
}
