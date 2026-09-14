package com.example.shopkart.controller;

import com.example.shopkart.entity.Product;
import com.example.shopkart.entity.User;
import com.example.shopkart.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(required = false) String category,
			HttpServletRequest request) {

		try {

			List<Product> products = productService.getProductsByCategory(category);

			Map<String, Object> response = new HashMap<>();

			// Get authenticated user
			User authenticatedUser = (User) request.getAttribute("authenticatedUser");

			System.out.println("Authenticated User = " + authenticatedUser);

			Map<String, String> userInfo = new HashMap<>();

			if (authenticatedUser != null) {
				userInfo.put("name", authenticatedUser.getUsername());
				userInfo.put("role", authenticatedUser.getRole().name());
			} else {
				userInfo.put("name", "Guest");
				userInfo.put("role", "CUSTOMER");
			}

			response.put("user", userInfo);

			List<Map<String, Object>> productList = new ArrayList<>();

			for (Product product : products) {

				Map<String, Object> productDetails = new HashMap<>();

				productDetails.put("productId", product.getProductId());

				productDetails.put("name", product.getName());

				productDetails.put("description", product.getDescription());

				productDetails.put("price", product.getPrice());

				productDetails.put("stock", product.getStock());

				List<String> images = productService.getProductImages(product.getProductId());

				productDetails.put("images", images);

				productList.add(productDetails);
			}

			response.put("products", productList);

			return ResponseEntity.ok(response);

		} catch (Exception e) {

			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
}