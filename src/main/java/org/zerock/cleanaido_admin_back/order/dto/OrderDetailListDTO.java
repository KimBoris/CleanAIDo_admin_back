package org.zerock.cleanaido_admin_back.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailListDTO {

    private String pcode;
    private String pname;
    private int quantity; // 수량
    private int price; // 판매가
    private int totalPrice; // 수량 * 판매가
    
}
