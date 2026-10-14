package com.example.orderapi.service;

import com.example.orderapi.dto.OrderRequest;
import com.example.orderapi.entity.Order;
import com.example.orderapi.entity.Product;
import com.example.orderapi.exception.InsufficientStockException;
import com.example.orderapi.exception.ResourceNotFoundException;
import com.example.orderapi.repository.OrderRepository;
import com.example.orderapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Product product(int stock) {
        Product p = new Product();
        p.setId(1L);
        p.setName("Laptop");
        p.setPrice(new BigDecimal("100.00"));
        p.setStock(stock);
        return p;
    }

    private OrderRequest request(Long productId, int quantity) {
        OrderRequest r = new OrderRequest();
        r.setProductId(productId);
        r.setQuantity(quantity);
        return r;
    }

    @Test
    void placeOrder_success_deductsStockAndCalculatesTotal() {
        Product p = product(10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order order = orderService.placeOrder(request(1L, 3));

        assertEquals(7, p.getStock());
        assertEquals(3, order.getQuantity());
        assertEquals(new BigDecimal("300.00"), order.getTotalPrice());
        verify(productRepository).save(p);
    }

    @Test
    void placeOrder_notEnoughStock_throwsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product(2)));

        assertThrows(InsufficientStockException.class,
                () -> orderService.placeOrder(request(1L, 5)));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void placeOrder_outOfStock_throwsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product(0)));

        assertThrows(InsufficientStockException.class,
                () -> orderService.placeOrder(request(1L, 1)));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void placeOrder_productNotFound_throwsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.placeOrder(request(99L, 1)));
    }
}
