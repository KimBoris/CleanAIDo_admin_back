package org.zerock.cleanaido_admin_back.customer.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        //키워드가 없으면
//        if (searchDTO.getKeyword() == null) {
//            log.info("All List");
//        }
        return ResponseEntity.ok(customerService.listCustomers(pageRequestDTO));
//        else {
//            log.info("Search");
//            return ResponseEntity.ok(customerService.search(pageRequestDTO));
//        }
    }

    @PutMapping("delete/{customerId}")
    public ResponseEntity<String> delete(@PathVariable("customerId") String customerId) {
        customerService.softDeleteCustomer(customerId);

        return ResponseEntity.ok(customerId + "is delete successfully");
    }
}
