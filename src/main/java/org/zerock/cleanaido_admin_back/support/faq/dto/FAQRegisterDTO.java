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

    private String question;
    private String answer;
    private boolean delFlag;
}
