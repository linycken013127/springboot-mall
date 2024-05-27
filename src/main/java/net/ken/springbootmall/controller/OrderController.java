package net.ken.springbootmall.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import net.ken.springbootmall.dto.CreateOrderRequest;
import net.ken.springbootmall.dto.OrderQueryRequest;
import net.ken.springbootmall.model.Order;
import net.ken.springbootmall.service.OrderService;
import net.ken.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("users/{userId}/orders")
    public ResponseEntity<Order> createOrder(
            @PathVariable Integer userId,
            @RequestBody @Valid CreateOrderRequest createOrderRequest
    ) {
        Integer orderId = orderService.createOrder(userId, createOrderRequest);
        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
        orderQueryRequest.setUserId(userId);
        orderQueryRequest.setLimit(limit);
        orderQueryRequest.setOffset(offset);

        List<Order> orderList = orderService.getOrders(orderQueryRequest);
        Integer orderCount = orderService.countOrder(orderQueryRequest);

        Page<Order> page = new Page<>();
        page.setTotal(orderCount);
        page.setLimit(limit);
        page.setOffset(offset);
        page.setResults(orderList);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
