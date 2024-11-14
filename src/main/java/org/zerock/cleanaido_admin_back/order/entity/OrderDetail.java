package org.zerock.cleanaido_admin_back.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_number")
    private Order order;

    @Column(name = "product_number", nullable = false)
    private Integer productNumber;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private int price;
}
