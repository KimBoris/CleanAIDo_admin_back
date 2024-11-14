package org.zerock.cleanaido_admin_back.order.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_number")
    private Long orderNumber;

    @Column(name = "customer_id", length = 255, nullable = false)
    private String customerId;

    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @Column(name = "delivery_address", length = 255, nullable = false)
    private String deliveryAddress;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "tracking_number", length = 255)
    private String trackingNumber;

    @Column(name = "order_status", length = 255, nullable = false)
    private String orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;
}
