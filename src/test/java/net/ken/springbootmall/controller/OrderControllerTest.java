package net.ken.springbootmall.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ken.springbootmall.dto.BuyItem;
import net.ken.springbootmall.dto.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    @Test
    public void createOrder_success() throws Exception {
        String json = createOrderRequestJson();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.totalAmount", equalTo(630)))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.orderItemList", hasSize(2)))
                .andExpect(jsonPath("$.orderItemList[0].productId", equalTo(1)))
                .andExpect(jsonPath("$.orderItemList[1].productId", equalTo(2)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    private String createOrderRequestJson() throws JsonProcessingException {
        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(1);
        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(2);

        List<BuyItem> buyItemList = new ArrayList<>();
        buyItemList.add(buyItem1);
        buyItemList.add(buyItem2);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);
        return json;
    }

    @Transactional
    @Test
    public void createOrder_illegalArgument_emptyBuyItemList() throws Exception {
        List<BuyItem> buyItemList = new ArrayList<>();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();

        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));

    }

    @Transactional
    @Test
    public void createOrder_userNotExist() throws Exception {
        String json = createOrderRequestJson();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_productNotExist() throws Exception {
        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(0);
        buyItem1.setQuantity(1);
        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(2);

        List<BuyItem> buyItemList = new ArrayList<>();
        buyItemList.add(buyItem1);
        buyItemList.add(buyItem2);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_stockNotEnough() throws Exception {
        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(999);
        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(2);

        List<BuyItem> buyItemList = new ArrayList<>();
        buyItemList.add(buyItem1);
        buyItemList.add(buyItem2);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }


    @Test
    public void getOrders() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 1);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].userId", equalTo(1)))
                .andExpect(jsonPath("$.results[0].id", notNullValue()))
                .andExpect(jsonPath("$.results[0].totalAmount", equalTo(100000)))
                .andExpect(jsonPath("$.results[0].createdDate", notNullValue()))
                .andExpect(jsonPath("$.results[0].lastModifiedDate", notNullValue()))
                .andExpect(jsonPath("$.results[1].id", notNullValue()))
                .andExpect(jsonPath("$.results[1].userId", equalTo(1)))
                .andExpect(jsonPath("$.results[1].totalAmount", equalTo(500690)))
                .andExpect(jsonPath("$.results[1].createdDate", notNullValue()))
                .andExpect(jsonPath("$.results[1].lastModifiedDate", notNullValue()));
    }

    @Test
    public void getOrders_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 1)
                .param("limit", "1")
                .param("offset", "0");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", equalTo(1)))
                .andExpect(jsonPath("$.offset", equalTo(0)))
                .andExpect(jsonPath("$.total", equalTo(2)))
                .andExpect(jsonPath("$.results", hasSize(1)));
    }

    @Test
    public void getOrders_userHasNoOrder() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 2);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", equalTo(10)))
                .andExpect(jsonPath("$.offset", equalTo(0)))
                .andExpect(jsonPath("$.total", equalTo(0)))
                .andExpect(jsonPath("$.results", hasSize(0)));
    }

    @Test
    public void getOrders_userNotExist() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 999);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(0)));
    }
}
