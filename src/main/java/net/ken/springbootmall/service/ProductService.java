package net.ken.springbootmall.service;

import net.ken.springbootmall.dto.ProductRequest;
import net.ken.springbootmall.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
