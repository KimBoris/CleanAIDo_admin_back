package org.zerock.cleanaido_admin_back.support.faq.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQReadDTO {
    private Long fno;
    private String question;
    private String answer;
    private boolean delFlag;

    private List<String> fileNames;
}
