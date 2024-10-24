package org.zerock.cleanaido_admin_back.support.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQSearchDTO {

    private PageRequestDTO pageRequestDTO;
    private String keyword;
}

