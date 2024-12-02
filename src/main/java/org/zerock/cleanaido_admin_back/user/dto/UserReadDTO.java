package org.zerock.cleanaido_admin_back.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadDTO {
    private String userId;

    private boolean delFlag;
}
