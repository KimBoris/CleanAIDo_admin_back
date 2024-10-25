package org.zerock.cleanaido_admin_back.support.qna.dto;

import lombok.Data;

@Data
public class AnswerDTO {
    private Long qno; // 답해야할 질문의 번호
    private String answerText; // 답변 내용
}

