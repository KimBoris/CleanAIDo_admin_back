
package org.zerock.cleanaido_admin_back.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.cleanaido_admin_back.order.entity.Order;

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

    // Order 엔티티를 기반으로 하는 생성자 추가
    public OrderListDTO(Order order) {
        this.orderNumber = order.getOrderNumber();
        //this.productNumber = order.getProductNumber();
        this.customerId = order.getCustomerId();
        this.phoneNumber = order.getPhoneNumber();
        this.deliveryAddress = order.getDeliveryAddress();
        this.totalPrice = order.getTotalPrice();
        this.orderDate = order.getOrderDate();
        this.trackingNumber = order.getTrackingNumber();
        this.orderStatus = order.getOrderStatus();
    }
}
