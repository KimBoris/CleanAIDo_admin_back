package org.zerock.cleanaido_admin_back.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String userId;   // 사용자의 이메일 (user_id)
    private String password; // 사용자의 비밀번호 (user_pw)
}