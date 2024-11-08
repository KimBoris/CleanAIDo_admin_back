package org.zerock.cleanaido_admin_back.product.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ImageFiles {

    private int ord;
    private String fileName;
    private boolean type;
}
