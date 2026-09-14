package com.example.shopkart.service;

import com.example.shopkart.entity.User;
import com.example.shopkart.entity.Role;
import com.example.shopkart.repository.JWTTokenRepository;
import com.example.shopkart.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminUserService {

	private final UserRepository userRepository;
	private final JWTTokenRepository jwtTokenRepository;

	public AdminUserService(UserRepository userRepository, JWTTokenRepository jwtTokenRepository) {

		this.userRepository = userRepository;
		this.jwtTokenRepository = jwtTokenRepository;
	}

	@Transactional
	public User modifyUser(Integer userId, String username, String email, String role) {

		// Check if user exists
		Optional<User> userOptional = userRepository.findById(userId);

		if (userOptional.isEmpty()) {
			throw new IllegalArgumentException("User not found");
		}

		User existingUser = userOptional.get();

		// Update username
		if (username != null && !username.isEmpty()) {

			existingUser.setUsername(username);
		}

		// Update email
		if (email != null && !email.isEmpty()) {

			existingUser.setEmail(email);
		}

		// Update role
		if (role != null && !role.isEmpty()) {

			try {

				existingUser.setRole(Role.valueOf(role));

			} catch (IllegalArgumentException e) {

				throw new IllegalArgumentException("Invalid role: " + role);
			}
		}

		// Delete associated JWT tokens
		jwtTokenRepository.deleteByUserId(userId);

		// Save updated user
		return userRepository.save(existingUser);
	}

	public User getUserById(Integer userId) {

		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}