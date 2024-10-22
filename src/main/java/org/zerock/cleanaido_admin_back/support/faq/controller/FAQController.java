package org.zerock.cleanaido_admin_back.support.faq.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQListDTO;
import org.zerock.cleanaido_admin_back.support.faq.service.FAQService;

@RestController
@RequestMapping("/api/v1/admin/faq")
@Log4j2
@RequiredArgsConstructor
public class FAQController {
    private final FAQService faqService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<FAQListDTO>> list(PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(faqService.listFAQ(pageRequestDTO));
    }
}
