package net.ken.springbootmall.service;

import net.ken.springbootmall.dao.ProductDao;
import net.ken.springbootmall.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }
}
