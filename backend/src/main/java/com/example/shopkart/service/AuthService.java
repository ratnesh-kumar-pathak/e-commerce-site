package com.example.shopkart.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shopkart.entity.JWTToken;
import com.example.shopkart.entity.User;
import com.example.shopkart.repository.JWTTokenRepository;
import com.example.shopkart.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class AuthService {

    private static final String SECRET =
            "ThisIsAVeryLongSecretKeyForJWTAuthentication123456789";

    private final SecretKey SIGNING_KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private final UserRepository userRepository;
    private final JWTTokenRepository jwtTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository,
                       JWTTokenRepository jwtTokenRepository,
                       BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(String username, String password) {

        System.out.println("Received Username = " + username);
        System.out.println("Received Password = " + password);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("User NOT found");
                    return new RuntimeException("Invalid username or password");
                });

        System.out.println("DB Username = " + user.getUsername());
        System.out.println("DB Password = " + user.getPassword());

        if (!password.equals(user.getPassword())) {
            System.out.println("Password mismatch");
            throw new RuntimeException("Invalid username or password");
        }

        System.out.println("Login Success");
        return user;
    }

    public String generateToken(User user) {

        List<JWTToken> tokens =
                jwtTokenRepository.findByUserId(user.getUserId());

        if (!tokens.isEmpty()) {

            JWTToken existingToken = tokens.get(0);

            if (existingToken.getExpiresAt().isAfter(LocalDateTime.now())) {
                return existingToken.getToken();
            }
        }

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new java.util.Date())
                .setExpiration(
                        new java.util.Date(
                                System.currentTimeMillis() + 3600000))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();

        saveToken(user, token);

        return token;
    }
    public void saveToken(User user, String token) {

        JWTToken jwtToken = new JWTToken(
                user,
                token,
                LocalDateTime.now().plusHours(1));

        jwtTokenRepository.save(jwtToken);
    }
    
    public boolean validateToken(String token) {
        try {

            System.err.println("VALIDATING TOKEN...");

            // Parse and validate the token
            Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);

            // Check if the token exists in the database and is not expired
            Optional<JWTToken> jwtToken =
                    jwtTokenRepository.findByToken(token);

            if (jwtToken.isPresent()) {

                System.err.println("Token Expiry: "
                        + jwtToken.get().getExpiresAt());

                System.err.println("Current Time: "
                        + LocalDateTime.now());

                return jwtToken.get()
                        .getExpiresAt()
                        .isAfter(LocalDateTime.now());
            }

            return false;

        } catch (Exception e) {

            System.err.println(
                    "Token validation failed: "
                            + e.getMessage());

            return false;
        }
    }

    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public void logout(User user) {

        int userId = user.getUserId();

        List<JWTToken> tokens =
                jwtTokenRepository.findByUserId(userId);

        if (!tokens.isEmpty()) {

            jwtTokenRepository.deleteAll(tokens);

            System.out.println(
                    "Tokens deleted for user: "
                            + user.getUsername());
        }
    }
}