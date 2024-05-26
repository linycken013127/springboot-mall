package net.ken.springbootmall.dao;

import net.ken.springbootmall.dto.ProductQueryParams;
import net.ken.springbootmall.dto.ProductRequest;
import net.ken.springbootmall.model.Product;
import net.ken.springbootmall.rowmapper.ProductRowMapper;
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
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date FROM product WHERE product_id = :product_id";

        Map<String, Object> map = new HashMap<>();
        map.put("product_id", productId);

        List<Product> product = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (product.size() == 0) {
            return null;
        }

        return product.get(0);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate )";

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getName());
        map.put("category", productRequest.getCategory().name());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :productName, category = :category, image_url = :imageUrl, " +
                "price = :price, stock = :stock, description = :description, last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getName());
        map.put("category", productRequest.getCategory().name());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("lastModifiedDate", new Date());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams params) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product " +
                "WHERE 1=1 ";

        Map<String, Object> map = new HashMap<>();

        if (params.getCategory() != null) {
            sql += "AND category = :category ";
            map.put("category", params.getCategory().name());
        }

        if (params.getSearch() != null) {
            sql += "AND product_name LIKE :productName ";
            map.put("productName", "%" + params.getSearch() + "%");
        }

        sql = sql + "ORDER BY " + params.getOrderBy() + " " + params.getSort() + " ";

        sql += "LIMIT :limit OFFSET :offset";
        map.put("limit", params.getLimit());
        map.put("offset", params.getOffset());

        List<Product> products = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        return products;
    }
}
