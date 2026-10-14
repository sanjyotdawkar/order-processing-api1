package com.example.orderapi.controller;

import com.example.orderapi.entity.Product;
import com.example.orderapi.repository.OrderRepository;
import com.example.orderapi.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Product savedProduct;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();

        Product p = new Product();
        p.setName("Phone");
        p.setDescription("Test phone");
        p.setPrice(new BigDecimal("50.00"));
        p.setStock(5);
        savedProduct = productRepository.save(p);
    }

    private String json(Object body) throws Exception {
        return objectMapper.writeValueAsString(body);
    }

    @Test
    void createOrder_success_returns201AndReducesStock() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("productId", savedProduct.getId(), "quantity", 2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(100.0));

        Product updated = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertEquals(3, updated.getStock());
    }

    @Test
    void createOrder_quantityMoreThanStock_returns409AndStockUnchanged() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("productId", savedProduct.getId(), "quantity", 10))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists());

        Product unchanged = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertEquals(5, unchanged.getStock());
    }

    @Test
    void createOrder_outOfStockProduct_returns409() throws Exception {
        savedProduct.setStock(0);
        productRepository.save(savedProduct);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("productId", savedProduct.getId(), "quantity", 1))))
                .andExpect(status().isConflict());
    }

    @Test
    void createOrder_invalidQuantity_returns400() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("productId", savedProduct.getId(), "quantity", 0))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void createOrder_unknownProduct_returns404() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("productId", 9999, "quantity", 1))))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOrder_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/orders/9999"))
                .andExpect(status().isNotFound());
    }
}
