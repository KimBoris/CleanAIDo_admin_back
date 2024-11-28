package org.zerock.cleanaido_admin_back.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;

@RestController
@RequestMapping("/api/v1/admin/customer")

public class CustomerController {

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<Customer>> list(PageRequestDTO pageRequestDTO) {


        return null;
    }
}
