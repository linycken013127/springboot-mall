package net.ken.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ken.springbootmall.constant.ProductCategory;
import net.ken.springbootmall.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getProductById() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/{productId}", 1);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("蘋果（澳洲）")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", equalTo("FOOD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image_url", equalTo("https://cdn.pixabay.com/photo/2016/11/30/15/00/apples-1872997_1280.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", equalTo(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", equalTo(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo("這是來自澳洲的蘋果！")))
                .andReturn();
    }

    @Transactional
    @Test
    public void createProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("蘋果");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://cdn.pixabay.com/photo/2014/02/01/17/28/apple-256261__480.jpg");
        productRequest.setPrice(20);
        productRequest.setStock(10);
        productRequest.setDescription("這是來自澳洲的蘋果！");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("蘋果")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", equalTo("FOOD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image_url", equalTo("https://cdn.pixabay.com/photo/2014/02/01/17/28/apple-256261__480.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", equalTo(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", equalTo(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo("這是來自澳洲的蘋果！")))
                .andReturn();
    }

    @Transactional
    @Test
    public void updateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("蘋果");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://cdn.pixabay.com/photo/2014/02/01/17/28/apple-256261__480.jpg");
        productRequest.setPrice(20);
        productRequest.setStock(10);
        productRequest.setDescription("這是來自澳洲的蘋果！");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 7)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("蘋果")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", equalTo("FOOD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image_url", equalTo("https://cdn.pixabay.com/photo/2014/02/01/17/28/apple-256261__480.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", equalTo(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", equalTo(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo("這是來自澳洲的蘋果！")))
                .andReturn();
    }
}
