package org.zerock.cleanaido_admin_back.support.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQRegisterDTO {

    private Long fno;
    private String question;
    private String answer;

    @Builder.Default
    private boolean delFlag = false;
}
