package org.zerock.cleanaido_admin_back.support.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionListDTO {

    private Long qno;

    private String title;

    private String writer;

    private boolean answered;
}
