package net.ken.springbootmall.dao;

import net.ken.springbootmall.dto.ProductRequest;
import net.ken.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);
}
