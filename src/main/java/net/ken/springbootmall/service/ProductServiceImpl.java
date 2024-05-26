package net.ken.springbootmall.service;

import net.ken.springbootmall.dao.ProductDao;
import net.ken.springbootmall.dto.ProductQueryParams;
import net.ken.springbootmall.dto.ProductRequest;
import net.ken.springbootmall.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams params) {
        return productDao.getProducts(params);
    }
}
