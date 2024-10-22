package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

import java.util.Optional;


public interface QNASearch {
    Page<Question> list(Pageable pageable);

    // 새로운 메소드 추가
    Optional<QuestionDTO> getAQuestion(Long qno);

}
