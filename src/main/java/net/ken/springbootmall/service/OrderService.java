package net.ken.springbootmall.service;

import net.ken.springbootmall.dto.CreateOrderRequest;
import net.ken.springbootmall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);
}
