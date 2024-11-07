package org.zerock.cleanaido_admin_back.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {

    private Long pno;

    private String pcode;

    private String pname;

    private int price;

    private int quantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String pstatus;
}
