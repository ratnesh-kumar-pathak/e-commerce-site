package com.example.shopkart.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shopkart.dto.LoginRequest;
import com.example.shopkart.entity.User;
import com.example.shopkart.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AuthController {

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	// =========================
	// LOGIN
	// =========================
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {

		try {

			User user = authService.authenticate(request.getUsername(), request.getPassword());

			String token = authService.generateToken(user);

			Cookie cookie = new Cookie("authToken", token);

			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge(60 * 60);

			response.addCookie(cookie);

			Map<String, Object> result = new HashMap<>();

			result.put("message", "Login successful");
			result.put("username", user.getUsername());
			result.put("role", user.getRole());
			result.put("token", token);

			return ResponseEntity.ok(result);

		} catch (RuntimeException e) {

			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	// =========================
	// LOGOUT
	// =========================
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

		try {

			User user = (User) request.getAttribute("authenticatedUser");

			authService.logout(user);

			Cookie cookie = new Cookie("authToken", null);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge(0);

			response.addCookie(cookie);

			return ResponseEntity.ok(Map.of("message", "Logout successful"));

		} catch (Exception e) {

			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
}