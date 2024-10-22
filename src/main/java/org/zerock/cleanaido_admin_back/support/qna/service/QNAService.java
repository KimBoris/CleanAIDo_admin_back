package org.zerock.cleanaido_admin_back.support.qna.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.qna.Repository.QNARepository;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionDTO;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QNAService {

    private final QNARepository qnaRepository;

    private Pageable pageable;

    public PageResponseDTO<QuestionDTO> list(QuestionDTO pageRequestDTO) {

        return qnaRepository.list(pageable);
    }
}
