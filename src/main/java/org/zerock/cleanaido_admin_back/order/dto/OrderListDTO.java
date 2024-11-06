package org.zerock.cleanaido_admin_back.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDTO {

    private Integer orderNumber;
    private Integer productNumber;
    private String customerId;
    private String phoneNumber;
    private String deliveryAddress;
    private Integer totalPrice;
    private LocalDateTime orderDate;
    private String trackingNumber;
    private String orderStatus;
}