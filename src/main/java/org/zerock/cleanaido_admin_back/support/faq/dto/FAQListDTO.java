package org.zerock.cleanaido_admin_back.support.faq.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

@NoArgsConstructor
public class FAQListDTO
{
    private Long fno;
    private String question;
    private boolean delFlag;
    private String fileName;

    public FAQListDTO(Long fno, String question, boolean delFlag, String fileName) {
        this.fno = fno;
        this.question = question;
        this.delFlag = delFlag;
        this.fileName = fileName;
    }
}
