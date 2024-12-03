package org.zerock.cleanaido_admin_back.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.cleanaido_admin_back.category.entity.Category;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadDTO {

    private Long pno;

    private String pcode;

    private String pname;

    private int price;

    private int quantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<String> imageFiles;

    private List<String> detailImageFiles;

    private List<String> usageImageFiles;

    private Category category;

    private String tags;

    private String pstatus;
}
