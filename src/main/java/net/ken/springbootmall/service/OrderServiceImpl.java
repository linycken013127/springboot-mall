package net.ken.springbootmall.service;

import net.ken.springbootmall.dao.OrderDao;
import net.ken.springbootmall.dao.ProductDao;
import net.ken.springbootmall.dto.BuyItem;
import net.ken.springbootmall.dto.CreateOrderRequest;
import net.ken.springbootmall.dto.OrderQueryRequest;
import net.ken.springbootmall.model.Order;
import net.ken.springbootmall.model.OrderItem;
import net.ken.springbootmall.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            checkProductStock(buyItem, product);

            productDao.updateStock(product.getId(), product.getStock() - buyItem.getQuantity());

            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItem(orderId, orderItemList);

        return orderId;
    }

    private static void checkProductStock(BuyItem buyItem, Product product) {
        if (product == null) {
            logger.warn("商品不存在");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (product.getStock() < buyItem.getQuantity()) {
            logger.warn("商品 {} 數量不足，剩餘 {}，欲購買數量為 {}", product.getName(), product.getStock(), buyItem.getQuantity());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(orderId);
        order.setOrderItemList(orderItemList);
        return order;
    }

    @Override
    public List<Order> getOrders(OrderQueryRequest orderQueryRequest) {
        List<Order> orderList = orderDao.getOrders(orderQueryRequest);

        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(order.getId());
            order.setOrderItemList(orderItemList);
        }
        return orderList;
    }

    @Override
    public Integer countOrder(OrderQueryRequest orderQueryRequest) {
        return orderDao.countOrder(orderQueryRequest);
    }
}
