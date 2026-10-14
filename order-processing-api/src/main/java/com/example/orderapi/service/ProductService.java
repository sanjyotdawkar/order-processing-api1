package com.example.orderapi.service;

import com.example.orderapi.dto.ProductRequest;
import com.example.orderapi.entity.Product;
import com.example.orderapi.exception.ResourceNotFoundException;
import com.example.orderapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        copyFields(request, product);
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public Product update(Long id, ProductRequest request) {
        Product product = getById(id);
        copyFields(request, product);
        return productRepository.save(product);
    }

    private void copyFields(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
    }
}
