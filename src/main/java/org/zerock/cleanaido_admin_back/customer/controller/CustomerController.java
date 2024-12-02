package org.zerock.cleanaido_admin_back.customer.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerListDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerReadDTO;
import org.zerock.cleanaido_admin_back.customer.dto.CustomerRegisterDTO;
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


        if (searchDTO.getKeyword() != null || searchDTO.getSearchType() != null) {
            return ResponseEntity.ok(customerService.search(pageRequestDTO));
        }
        return ResponseEntity.ok(customerService.listCustomers(pageRequestDTO));
    }
    @GetMapping("{customerId}")
    public ResponseEntity<CustomerReadDTO> read(@PathVariable String customerId)
    {
        CustomerReadDTO readDTO = customerService.getCustomer(customerId);

        log.info("REadDTO = "+ readDTO.toString());

        return ResponseEntity.ok(readDTO);
    }

    @PutMapping("delete/{customerId}")
    public ResponseEntity<String> delete(@PathVariable("customerId") String customerId) {
        customerService.softDeleteCustomer(customerId);

        return ResponseEntity.ok(customerId + "is delete successfully");
    }

    @PutMapping("{customerId}")
    public ResponseEntity<String> update(@PathVariable String customerId,
                                         @ModelAttribute CustomerRegisterDTO customerRegisterDTO) {
        String resultMessage = customerService.updateCustomer(customerId, customerRegisterDTO);

        return ResponseEntity.ok(resultMessage);
    }


}
