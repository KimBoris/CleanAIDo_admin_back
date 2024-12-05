package org.zerock.cleanaido_admin_back.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListDTO {

    private Long cno;

    private String cname;;

    private Long parent;
}
