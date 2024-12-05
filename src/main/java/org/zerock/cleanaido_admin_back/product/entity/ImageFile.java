package org.zerock.cleanaido_admin_back.product.entity;

import jakarta.persistence.Column;
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
public class ImageFile {

    @Column(name = "ord")
    private int ord;

    @Column(name="file_name")
    private String fileName;

    @Column(name="type")
    private boolean type;
}
