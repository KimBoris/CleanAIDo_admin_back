package org.zerock.cleanaido_admin_back.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.cleanaido_admin_back.order.entity.Order;
import org.zerock.cleanaido_admin_back.order.entity.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDTO {

    private Long orderNumber;
    private String customerId;
    private String phoneNumber;
    private String deliveryAddress;
    private Integer totalPrice;
    private LocalDateTime orderDate;
    private String trackingNumber;
    private String orderStatus;
    private List<Integer> productNumbers;

    public OrderListDTO(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.customerId = order.getCustomerId();
        this.phoneNumber = order.getPhoneNumber();
        this.deliveryAddress = order.getDeliveryAddress();
        this.totalPrice = order.getTotalPrice();
        this.orderDate = order.getOrderDate();
        this.trackingNumber = order.getTrackingNumber();
        this.orderStatus = order.getOrderStatus();
        this.productNumbers = order.getOrderDetails().stream()
                .map(OrderDetail::getProductNumber)
                .collect(Collectors.toList());
    }
}
