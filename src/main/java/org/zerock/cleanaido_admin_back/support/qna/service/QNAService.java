package org.zerock.cleanaido_admin_back.support.qna.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.qna.Repository.QNARepository;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QNAService {

    private final QNARepository qnaRepository;

    public PageResponseDTO<QuestionListDTO> listQuestion(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        Page<Question> questionPage = qnaRepository.list(pageable);

        List<QuestionListDTO> dtoList = questionPage.getContent().stream()
                .map(question -> QuestionListDTO.builder()
                        .title(question.getTitle())
                        .build()).collect(Collectors.toList());


        return new PageResponseDTO<>(dtoList, pageRequestDTO, questionPage.getTotalElements());
    }
}
