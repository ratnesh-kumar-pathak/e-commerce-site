package com.example.shopkart.filter;

import com.example.shopkart.entity.Role;
import com.example.shopkart.entity.User;
import com.example.shopkart.repository.UserRepository;
import com.example.shopkart.service.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AuthenticationFilter extends OncePerRequestFilter {

	private final AuthService authService;
	private final UserRepository userRepository;

	public AuthenticationFilter(AuthService authService, UserRepository userRepository) {

		this.authService = authService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		System.out.println("FILTER EXECUTED");

		String requestURI = request.getRequestURI();

		System.out.println("REQUEST URI = " + requestURI);

		// Public endpoints or CORS preflight
		if (requestURI.equals("/api/auth/login") || requestURI.equals("/api/users/register") || request.getMethod().equalsIgnoreCase("OPTIONS")) {

			filterChain.doFilter(request, response);
			return;
		}

		// Read JWT from Authorization header
		String authHeader = request.getHeader("Authorization");

		System.out.println("AUTH HEADER = " + authHeader);

		String token = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {

			token = authHeader.substring(7);
		}

		System.out.println("TOKEN = " + token);

		if (token == null || !authService.validateToken(token)) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			response.getWriter().write("Unauthorized: Invalid or missing token");

			return;
		}

		String username = authService.extractUsername(token);

		Optional<User> userOptional = userRepository.findByUsername(username);

		if (userOptional.isEmpty()) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			response.getWriter().write("Unauthorized: User not found");

			return;
		}

		User authenticatedUser = userOptional.get();

		Role role = authenticatedUser.getRole();

		System.out.println("Authenticated User = " + authenticatedUser.getUsername());

		if (requestURI.startsWith("/admin/") && role != Role.ADMIN) {

			response.setStatus(HttpServletResponse.SC_FORBIDDEN);

			response.getWriter().write("Forbidden: Admin access required");

			return;
		}

		request.setAttribute("authenticatedUser", authenticatedUser);

		filterChain.doFilter(request, response);
	}
}