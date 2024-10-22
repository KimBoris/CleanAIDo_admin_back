package org.zerock.cleanaido_admin_back.support.qna.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
import org.zerock.cleanaido_admin_back.support.qna.service.QNAService;

@RestController
@RequestMapping("/api/v1/admin/qna")
@Log4j2
@RequiredArgsConstructor
public class QNAController {

    private final QNAService qnaService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<QuestionListDTO>> list(PageRequestDTO pageRequestDTO) {

        return ResponseEntity.ok(qnaService.listQuestion(pageRequestDTO));

    }
}
