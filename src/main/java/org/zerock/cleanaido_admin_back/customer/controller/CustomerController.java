package org.zerock.cleanaido_admin_back.customer.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;
import org.zerock.cleanaido_admin_back.customer.service.CustomerService;

@RestController
@RequestMapping("/api/v1/admin/customer")
@RequiredArgsConstructor
@Log4j2
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<CustomerListDTO>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                 @RequestParam(value = "type", required = false) String type,
                                                                 @RequestParam(value = "keyword", required = false) String keyword) {

        SearchDTO searchDTO = SearchDTO.builder()
                .searchType(type)
                .keyword(keyword)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        if(searchDTO.getKeyword() != null || searchDTO.getSearchType() != null) {
            return ResponseEntity.ok(customerService.search(pageRequestDTO));
        }
            return ResponseEntity.ok(customerService.listCustomers(pageRequestDTO));
    }
}
