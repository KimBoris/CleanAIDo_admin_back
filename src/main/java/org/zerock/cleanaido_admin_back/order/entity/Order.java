package org.zerock.cleanaido_admin_back.order.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private Integer orderNumber;

//    @Column(name = "product_number", nullable = false)
//    private Integer productNumber;

    @Column(name = "customer_id", length = 255, nullable = false)
    private String customerId;

    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @Column(name = "delivery_address", length = 255, nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_message", length = 255)
    private String deliveryMessage;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "tracking_number", length = 255)
    private String trackingNumber;

    @Column(name = "order_status", length = 255, nullable = false)
    private String orderStatus;

    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
    }
}
