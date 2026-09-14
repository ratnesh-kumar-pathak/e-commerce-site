package com.example.shopkart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(
        scanBasePackages = "com.example.shopkart"
)
public class ShopKartApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                ShopKartApplication.class,
                args
        );
    }
}