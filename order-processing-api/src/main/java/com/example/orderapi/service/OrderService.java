package com.example.orderapi.service;

import com.example.orderapi.dto.OrderRequest;
import com.example.orderapi.entity.Order;
import com.example.orderapi.entity.Product;
import com.example.orderapi.exception.InsufficientStockException;
import com.example.orderapi.exception.ResourceNotFoundException;
import com.example.orderapi.repository.OrderRepository;
import com.example.orderapi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Places an order. Runs in one transaction, so if anything fails
     * the stock deduction is rolled back automatically.
     */
    @Transactional
    public Order placeOrder(OrderRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id " + request.getProductId()));

        int quantity = request.getQuantity();

        // Business rule 1: cannot order an unavailable (out of stock) item
        if (product.getStock() <= 0) {
            throw new InsufficientStockException("Product '" + product.getName() + "' is out of stock");
        }

        // Business rule 2: cannot order more than what is available
        if (product.getStock() < quantity) {
            throw new InsufficientStockException("Only " + product.getStock()
                    + " unit(s) of '" + product.getName() + "' available, but " + quantity + " requested");
        }

        // Business rule 3: deduct stock
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return orderRepository.save(order);
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }
}
