package net.ken.springbootmall.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getById() throws Exception {
        // 建立 request
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/{productId}", 1);

        // 驗證 response
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
}
