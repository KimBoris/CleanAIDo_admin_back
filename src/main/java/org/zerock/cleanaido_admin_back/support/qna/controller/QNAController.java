package org.zerock.cleanaido_admin_back.support.qna.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.common.dto.SearchDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
import org.zerock.cleanaido_admin_back.support.qna.service.QNAService;


@RestController
@RequestMapping("/api/v1/admin/qna")
@Log4j2
@RequiredArgsConstructor
public class QNAController {

    private final QNAService qnaService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<QuestionListDTO>> list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "searchType", required = false) String searchType
    ) {
        SearchDTO searchDTO = SearchDTO.builder()
                .keyword(keyword)
                .searchType(searchType)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .searchDTO(searchDTO)
                .build();

        if (searchDTO.getKeyword() == null || searchDTO.getKeyword().isEmpty()) {
            return ResponseEntity.ok(qnaService.listQuestion(pageRequestDTO));
        } else {
            if ("titleContents".equalsIgnoreCase(searchDTO.getSearchType())) {
                return ResponseEntity.ok(qnaService.searchByTitleAndContents(pageRequestDTO));
            } else if ("writer".equalsIgnoreCase(searchDTO.getSearchType())) {
                return ResponseEntity.ok(qnaService.searchByWriter(pageRequestDTO));
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
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
        if(answerText.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        qnaService.saveAnswer(answerText, qno); // 답변 저장
        return ResponseEntity.status(201).build(); // 201 Created 응답
    }

    @PutMapping("{qno}")
    public ResponseEntity<Void> updateAnswer(@PathVariable("qno") Long qno, @RequestParam String answerText) {
        if(answerText.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        qnaService.updateAnswer(answerText, qno);
        return ResponseEntity.status(201).build();
    }
}