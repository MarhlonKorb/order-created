package order_created.controller.dto;

import order_created.entity.Order;

import java.math.BigDecimal;

public record OrderResponse(Long orderId, Long customerId, BigDecimal total) {

    public static OrderResponse fromEntity(Order order){
        return new OrderResponse(order.getOrderId(), order.getCustomerId(), order.getTotal());
    }
}
