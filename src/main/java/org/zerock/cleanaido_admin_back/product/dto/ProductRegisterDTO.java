package org.zerock.cleanaido_admin_back.product.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegisterDTO {

    private String pcode;

    private String pname;

    private int price;

    private int quantity;

    private LocalDateTime releasedAt;

    private String ptags;

    private String pstatus;

    private String sellerId;
}
