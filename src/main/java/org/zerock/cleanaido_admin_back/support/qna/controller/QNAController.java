package org.zerock.cleanaido_admin_back.support.qna.controller;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.AnswerDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
import org.zerock.cleanaido_admin_back.support.qna.service.QNAService;

import java.util.Optional;

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

    @GetMapping("{qno}")
    public ResponseEntity<QuestionReadDTO> read(@PathVariable("qno") Long qno, Model model) {

        log.info("Reading question: " + qno);

        QuestionReadDTO questionReadDTO = qnaService.read(qno); // QNASearch 인터페이스 사용

        model.addAttribute("question", questionReadDTO);

        log.info("Read question: " + questionReadDTO.getTitle());
        log.info("answer : "+ questionReadDTO.getAnswertext());

        return ResponseEntity.ok(questionReadDTO); // qna 읽기 페이지로 이동
    }

    @PostMapping("{qno}")
    public ResponseEntity<Void> createAnswer(@PathVariable("qno") Long qno, @RequestParam String answerText) {
        qnaService.saveAnswer(answerText, qno); // 답변 저장
        return ResponseEntity.status(201).build(); // 201 Created 응답
    }

    @PutMapping("{qno}")
    public ResponseEntity<Void> updateAnswer(@PathVariable("qno") Long qno, @RequestParam String answerText) {
        qnaService.updateAnswer(answerText, qno);
        return ResponseEntity.status(201).build();
    }
}
