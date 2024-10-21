package org.zerock.cleanaido_admin_back.support.faq.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQListDTO;
import org.zerock.cleanaido_admin_back.support.faq.entity.FAQ;
import org.zerock.cleanaido_admin_back.support.faq.service.FAQService;

@RestController
@RequestMapping("/api/v1/admin/faq")
@PreAuthorize("permitAll()")
@Log4j2
@RequiredArgsConstructor
public class FAQController {
    private final FAQService faqService;

    @PreAuthorize("permitAll()")
    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<FAQListDTO>> list(
            @Validated PageRequestDTO pageRequestDTO
    ) {
        log.info("-------------------------------FAQ Controller list");
        return ResponseEntity.ok(faqService.list(pageRequestDTO));
    }
}
