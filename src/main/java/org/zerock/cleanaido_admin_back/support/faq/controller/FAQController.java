package org.zerock.cleanaido_admin_back.support.faq.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQListDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQReadDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQRegisterDTO;
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

    @GetMapping("{fno}")
    public ResponseEntity<FAQReadDTO> read(@PathVariable  Long fno) {
        FAQReadDTO faqDTO = faqService.readFAQ(fno);
        return ResponseEntity.ok(faqDTO);
    }


    @DeleteMapping("{fno}")
    public String delete(@PathVariable Long fno) {

        faqService.deleteFAQ(fno);
        return fno + "번이 삭제되었습니다.";
    }


}
