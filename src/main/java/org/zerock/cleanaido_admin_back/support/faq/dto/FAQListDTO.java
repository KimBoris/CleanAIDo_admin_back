package org.zerock.cleanaido_admin_back.support.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FAQListDTO {

    private Long id;
    private String title;
    private String description;
}
