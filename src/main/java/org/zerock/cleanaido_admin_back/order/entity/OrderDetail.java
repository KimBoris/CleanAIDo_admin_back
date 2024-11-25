package org.zerock.cleanaido_admin_back.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.zerock.cleanaido_admin_back.product.entity.Product;

@Entity
@Table(name = "order_detail")
@ToString(exclude = "Order, Product")
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
    @JoinColumn(name = "order_number", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_number", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private int price;
}
