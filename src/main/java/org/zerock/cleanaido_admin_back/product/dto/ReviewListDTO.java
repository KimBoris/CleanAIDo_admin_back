package org.zerock.cleanaido_admin_back.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.cleanaido_admin_back.customer.entity.Customer;
import org.zerock.cleanaido_admin_back.product.entity.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListDTO {

    private Long reviewNumber;
    private String reviewContent;
    private LocalDateTime createDate;
    private int score;
    private String customerName;
    private String productName;
    private Set<String> reviewImages;


}
