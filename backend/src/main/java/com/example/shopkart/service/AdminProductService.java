package com.example.shopkart.service;

import com.example.shopkart.entity.Category;
import com.example.shopkart.entity.Product;
import com.example.shopkart.entity.ProductImage;
import com.example.shopkart.repository.CategoryRepository;
import com.example.shopkart.repository.ProductImageRepository;
import com.example.shopkart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    public AdminProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    @Transactional
    public Map<String, Object> addProduct(String name, String description, BigDecimal price, Integer stock, Integer categoryId, String imageUrl) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        ProductImage productImage = new ProductImage();
        productImage.setProduct(savedProduct);
        productImage.setImageUrl(imageUrl);
        productImageRepository.save(productImage);

        Map<String, Object> response = new HashMap<>();
        response.put("product", savedProduct);
        response.put("imageUrl", imageUrl);

        return response;
    }

    @Transactional
    public void deleteProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        productImageRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
    }
}
