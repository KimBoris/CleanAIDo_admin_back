package org.zerock.cleanaido_admin_back.customer.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRegisterDTO {
    private String customerId;
    private String customerPw;

}
