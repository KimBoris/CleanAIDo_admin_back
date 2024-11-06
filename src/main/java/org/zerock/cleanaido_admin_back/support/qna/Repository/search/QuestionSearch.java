package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.Optional;


public interface QuestionSearch {
    PageResponseDTO<QuestionListDTO> list(PageRequestDTO pageRequestDTO);

    Page<Question> searchByTitleAndContents(String keyword, Pageable pageable);

    Page<Question> searchByWriter(String writer, Pageable pageable);
}