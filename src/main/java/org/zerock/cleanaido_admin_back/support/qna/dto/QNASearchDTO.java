package org.zerock.cleanaido_admin_back.support.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QNASearchDTO {

    private PageRequestDTO pageRequestDTO;
    private String keyword;
}