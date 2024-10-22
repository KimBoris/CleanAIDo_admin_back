package org.zerock.cleanaido_admin_back.support.qna.Repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.support.qna.dto.QuestionListDTO;
import org.zerock.cleanaido_admin_back.support.qna.entity.Question;

public interface QNASearch {
    Page<Question> list(Pageable pageable);

}
