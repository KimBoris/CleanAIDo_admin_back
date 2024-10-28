package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionReadDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.Optional;


public interface QuestionSearch {
    Page<Question> list(Pageable pageable);

    Page<Question> searchByTitleAndContents(String keyword, Pageable pageable);

    Page<Question> searchByWriter(String writer, Pageable pageable);
}