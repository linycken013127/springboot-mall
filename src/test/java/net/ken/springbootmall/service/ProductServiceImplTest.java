package net.ken.springbootmall.service;

import net.ken.springbootmall.constant.ProductCategory;
import net.ken.springbootmall.dao.ProductDao;
import net.ken.springbootmall.dto.ProductRequest;
import net.ken.springbootmall.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    ProductService productService;

    @MockBean
    ProductDao productDao;

    @BeforeEach
    public void before() {
        Product product = new Product();
        product.setId(1);
        product.setName("蘋果（澳洲）");
        product.setCategory(ProductCategory.FOOD);
        product.setImageUrl("https://cdn.pixabay.com/photo/2016/11/30/15/00/apples-1872997_1280.jpg");
        product.setPrice(30);
        product.setStock(10);
        product.setDescription("這是來自澳洲的蘋果！");
        Mockito.when(productDao.getProductById(Mockito.any()))
                .thenReturn(product);
    }

    @Test
    public void getProductById() {
        // Given
        Integer productId = 1;

        // When
        Product product = productService.getProductById(1);

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

    @Test
    public void createProduct() {
        // Given
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("蘋果");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://cdn.pixabay.com/photo/2014/02/01/17/28/apple-256261__480.jpg");
        productRequest.setPrice(20);
        productRequest.setStock(10);
        productRequest.setDescription("這是來自澳洲的蘋果！");

        // When
        Integer productId = productService.createProduct(productRequest);

        // Then
        assertNotNull(productId);
    }

    @Test
    public void updateProduct() {
        // Given
        Integer productId = 1;

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("蘋果A");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://cdn.pixabay.com/photo/2014/02/01/17/28/apple-256261__480.jpg");
        productRequest.setPrice(20);
        productRequest.setStock(10);
        productRequest.setDescription("這是來自澳洲的蘋果！");

        // When
        productService.updateProduct(productId, productRequest);

        // Then
        Mockito.verify(productDao, Mockito.times(1)).updateProduct(productId, productRequest);
    }
}
