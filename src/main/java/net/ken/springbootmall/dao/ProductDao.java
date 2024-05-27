package net.ken.springbootmall.dao;

import net.ken.springbootmall.dto.ProductQueryParams;
import net.ken.springbootmall.dto.ProductRequest;
import net.ken.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

    List<Product> getProducts(ProductQueryParams params);

    Integer countProduct(ProductQueryParams params);

    void updateStock(Integer productId, Integer stock);
}
