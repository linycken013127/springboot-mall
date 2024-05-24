package net.ken.springbootmall.dao;

import net.ken.springbootmall.constant.ProductCategory;
import net.ken.springbootmall.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductDaoImplTest {
    @Autowired
    private ProductDao productDao;

    @Test
    public void getById() {
        // Given
        Integer productId = 1;

        // When
        Product product = productDao.getProductById(productId);

        // Then
        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("蘋果（澳洲）", product.getName());
        assertEquals(ProductCategory.FOOD, product.getCategory());
        assertEquals("https://cdn.pixabay.com/photo/2016/11/30/15/00/apples-1872997_1280.jpg", product.getImageUrl());
        assertEquals(30, product.getPrice());
        assertEquals(10, product.getStock());
        assertEquals("這是來自澳洲的蘋果！", product.getDescription());
    }


}
