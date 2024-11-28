package org.zerock.cleanaido_admin_back.customer.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;
import org.zerock.cleanaido_admin_back.customer.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService
{
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;


    public PageResponseDTO<CustomerListDTO> listCustomers(PageRequestDTO pageRequestDTO)
    {
        if(pageRequestDTO.getPage()<1){
            throw new IllegalArgumentException("페이지 번호는 1이상 이어야 합니다.");
        }
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        PageResponseDTO<CustomerListDTO> response = customerRepository.list(pageRequestDTO);

        return response;
    }
}
