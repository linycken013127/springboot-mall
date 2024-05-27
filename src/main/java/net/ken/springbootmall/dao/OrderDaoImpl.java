package net.ken.springbootmall.dao;

import net.ken.springbootmall.dto.OrderQueryRequest;
import net.ken.springbootmall.model.Order;
import net.ken.springbootmall.model.OrderItem;
import net.ken.springbootmall.rowmapper.OrderItemRowMapper;
import net.ken.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql = "INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date) " +
                "VALUES (:userId, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();

        map.put("userId", userId);
        map.put("totalAmount", totalAmount);

        Date date = new Date();
        map.put("createdDate", date);
        map.put("lastModifiedDate", date);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void createOrderItem(Integer orderId, List<OrderItem> orderItemList) {
        String sql = "INSERT INTO `order_item` (order_id, product_id, quantity, amount) " +
                "VALUES (:orderId, :productId, :quantity, :amount)";

        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource()
                    .addValue("orderId", orderId)
                    .addValue("productId", orderItem.getProductId())
                    .addValue("quantity", orderItem.getQuantity())
                    .addValue("amount", orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM `order` WHERE order_id = :orderId";

        Map<String, Object> map = new HashMap<>();

        map.put("orderId", orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if (orderList.size() == 0) {
            return null;
        }

        return orderList.get(0);
    }

    @Override
    public List<OrderItem> getOrderItemByOrderId(Integer orderId) {
        String sql = "SELECT order_item_id, order_id, p.product_id, quantity, amount, p.product_name, p.image_url " +
                "FROM order_item AS oi " +
                "LEFT JOIN product AS p ON oi.product_id = p.product_id " +
                "WHERE order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        if (orderItemList.size() == 0) {
            return null;
        }

        return orderItemList;
    }

    @Override
    public List<Order> getOrders(OrderQueryRequest orderQueryRequest) {
        String sql = "SELECT order_id, user_id, total_amount FROM `order` " +
                "WHERE 1=1 ";

        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(orderQueryRequest, sql, map);

        sql += "ORDER BY created_date desc ";

        sql += "LIMIT :limit OFFSET :offset";

        map.put("limit", orderQueryRequest.getLimit());
        map.put("offset", orderQueryRequest.getOffset());

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());
        return orderList;
    }

    private static String addFilteringSql(OrderQueryRequest orderQueryRequest, String sql, Map<String, Object> map) {
        if (orderQueryRequest.getUserId() != null) {
            sql += "AND user_id = :userId ";
            map.put("userId", orderQueryRequest.getUserId());
        }
        return sql;
    }

    @Override
    public Integer countOrder(OrderQueryRequest orderQueryRequest) {
        String sql = "SELECT count(*) FROM `order` " +
                "WHERE 1=1 ";

        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(orderQueryRequest, sql, map);

        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit", orderQueryRequest.getLimit());
        map.put("offset", orderQueryRequest.getOffset());

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return count;
    }
}
