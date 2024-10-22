package org.zerock.cleanaido_admin_back.support.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    private Long Qno;

    private String title;

    private String contents;

    private String writer;
}
