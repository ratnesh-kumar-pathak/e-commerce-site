package com.example.shopkart.controller;

import com.example.shopkart.entity.User;
import com.example.shopkart.repository.UserRepository;
import com.example.shopkart.service.CartService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserRepository userRepository;

	// Get Cart Count
	@GetMapping("/items/count")
	public ResponseEntity<Integer> getCartItemCount(@RequestParam String username) {

		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		int count = cartService.getCartItemCount(user.getUserId());

		return ResponseEntity.ok(count);
	}

	// Get Cart Items
	@GetMapping("/items")
	public ResponseEntity<?> getCartItems(@RequestParam String username) {

		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		return ResponseEntity.ok(cartService.getCartItems(user.getUserId()));
	}

	// Add Product To Cart
	@PostMapping("/add")
	public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> request) {

		try {

			String username = (String) request.get("username");

			int productId = Integer.parseInt(request.get("productId").toString());

			int quantity = request.containsKey("quantity") ? Integer.parseInt(request.get("quantity").toString()) : 1;

			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("User not found"));

			cartService.addToCart(user.getUserId(), productId, quantity);

			return ResponseEntity.status(HttpStatus.CREATED).body("Added To Cart");

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Update Quantity
	@PutMapping("/update")
	public ResponseEntity<String> updateCartItemQuantity(@RequestBody Map<String, Object> request) {

		try {

			String username = (String) request.get("username");

			int productId = Integer.parseInt(request.get("productId").toString());

			int quantity = Integer.parseInt(request.get("quantity").toString());

			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("User not found"));

			cartService.updateCartItemQuantity(user.getUserId(), productId, quantity);

			return ResponseEntity.ok("Cart Updated");

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Delete Item
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteCartItem(@RequestBody Map<String, Object> request) {

		try {

			String username = (String) request.get("username");

			int productId = Integer.parseInt(request.get("productId").toString());

			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("User not found"));

			cartService.deleteCartItem(user.getUserId(), productId);

			return ResponseEntity.ok("Item Removed");

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}