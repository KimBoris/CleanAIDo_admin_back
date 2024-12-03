package org.zerock.cleanaido_admin_back.user.dto;


import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.cleanaido_admin_back.user.entity.UserStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO
{
    private String userId;

    private UserStatus userStatus;

}
