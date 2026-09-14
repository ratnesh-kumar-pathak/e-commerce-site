package com.example.shopkart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopkart.entity.JWTToken;

public interface JWTTokenRepository
        extends JpaRepository<JWTToken, Integer> {

    // Find all tokens belonging to a user
    @Query("""
            SELECT t
            FROM JWTToken t
            WHERE t.user.userId = :userId
            """)
    List<JWTToken> findByUserId(
            @Param("userId") Integer userId);

    // Find token by token string
    Optional<JWTToken> findByToken(
            String token);

    // Delete all tokens belonging to a user
    @Modifying
    @Transactional
    @Query("""
            DELETE
            FROM JWTToken t
            WHERE t.user.userId = :userId
            """)
    void deleteByUserId(
            @Param("userId") Integer userId);
}