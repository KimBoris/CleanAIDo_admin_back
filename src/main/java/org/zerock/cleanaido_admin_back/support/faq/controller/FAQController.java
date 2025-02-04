package org.zerock.cleanaido_admin_back.support.faq.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.UploadDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQListDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQReadDTO;
import org.zerock.cleanaido_admin_back.support.faq.dto.FAQRegisterDTO;
import org.zerock.cleanaido_admin_back.support.faq.service.FAQService;

    @RestController
    @RequestMapping("/api/v1/faq")
    @Log4j2
    @RequiredArgsConstructor
    public class FAQController {
        private final FAQService faqService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<FAQListDTO>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                            @RequestParam(value = "keyword", required = false) String keyword) {

        SearchDTO searchDTO = SearchDTO.builder()
                .keyword(keyword)
                .build();


        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();


        if (searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            return ResponseEntity.ok(faqService.listFAQ(pageRequestDTO));
        } else {
            return ResponseEntity.ok(faqService.search(pageRequestDTO));
        }
    }

        @PostMapping(value = "admin/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<Long> register(
                @ModelAttribute FAQRegisterDTO faqRegisterDTO,
                @RequestParam(value = "files", required = false) MultipartFile[] files) { // 첨부파일 필수 아님
            UploadDTO uploadDTO = new UploadDTO(files, null); // 파일이 없으면 null 전달
            Long fno = faqService.registerFAQ(faqRegisterDTO, uploadDTO);
            return ResponseEntity.ok(fno);
        }

    @GetMapping("read/{fno}")
    public ResponseEntity<FAQReadDTO> read(@PathVariable Long fno) {
        FAQReadDTO faqDTO = faqService.readFAQ(fno);
        return ResponseEntity.ok(faqDTO);
    }

    @DeleteMapping("admin/{fno}")
    public String delete(@PathVariable Long fno) {

        faqService.deleteFAQ(fno);
        return fno + "번이 삭제되었습니다.";
    }

    @PutMapping(value = "admin/{fno}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> update(
            @PathVariable Long fno,
            @ModelAttribute FAQRegisterDTO faqRegisterDTO,
            @RequestParam(value = "files", required = false) MultipartFile[] files) { // 첨부파일 필수 아님
        UploadDTO uploadDTO = new UploadDTO(files, null); // 파일이 없으면 null 전달
        Long updateFno = faqService.updateFAQ(fno, faqRegisterDTO, uploadDTO);
        return ResponseEntity.ok(updateFno + "번이 수정되었습니다.");
    }

}
