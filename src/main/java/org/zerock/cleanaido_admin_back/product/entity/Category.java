package org.zerock.cleanaido_admin_back.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_number", unique = true, nullable = false)
    private Long cno;

    @Column(name = "category_name", nullable = false, length = 100)
    private String cname;

    @Column(name = "depth", nullable = false, length = 100)
    private int depth;

    @Column(name = "parent")
    private Long parent;

}
