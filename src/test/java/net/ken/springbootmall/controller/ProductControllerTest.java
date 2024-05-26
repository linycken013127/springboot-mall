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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/{productId}", 1);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("蘋果（澳洲）")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", equalTo("FOOD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image_url", equalTo("https://cdn.pixabay.com/photo/2016/11/30/15/00/apples-1872997_1280.jpg")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", equalTo(30)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", equalTo(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo("這是來自澳洲的蘋果！")))
                .andReturn();
    }

    @Test
    public void getProduct_notFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/{productId}", 0);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is(404))
                .andReturn();
    }

    @Transactional
    @Test
    public void createProduct_success() throws Exception {
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
    public void createProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = new ProductRequest();

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();
    }

    @Transactional
    @Test
    public void updateProduct_success() throws Exception {
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
                .andExpect(status().isOk())
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
    public void updateProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = new ProductRequest();

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 7)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is(400))
                .andReturn();
    }

    @Transactional
    @Test
    public void updateProduct_productNotFound() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("蘋果");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://cdn.pixabay.com/photo/2014/02/01/17/28/apple-256261__480.jpg");
        productRequest.setPrice(20);
        productRequest.setStock(10);
        productRequest.setDescription("這是來自澳洲的蘋果！");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(404))
                .andReturn();
    }

    @Transactional
    @Test
    public void deleteProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/{productId}", 1);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is(204))
                .andReturn();
    }

    @Transactional
    @Test
    public void deleteProduct_deleteNonExistingProduct() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/{productId}", 0);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is(204))
                .andReturn();
    }

    @Test
    public void getProducts_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", notNullValue()))
                .andReturn();
    }

    @Test
    public void getProducts_filtering() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("category", "CAR")
                .param("search", "T");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(2)));
    }

    @Test
    public void getProduct_sorting() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("orderBy", "price")
                .param("sort", "desc");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(5)))
                .andExpect(jsonPath("$.results[0].id", equalTo(6)))
                .andExpect(jsonPath("$.results[1].id", equalTo(5)))
                .andExpect(jsonPath("$.results[2].id", equalTo(7)))
                .andExpect(jsonPath("$.results[3].id", equalTo(4)))
                .andExpect(jsonPath("$.results[4].id", equalTo(2)));
    }

    @Test
    public void getProduct_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("limit", "2")
                .param("offset", "2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].id", equalTo(5)))
                .andExpect(jsonPath("$.results[1].id", equalTo(4)));
    }
}
