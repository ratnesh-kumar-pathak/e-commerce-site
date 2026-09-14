package com.example.shopkart.config;

import com.example.shopkart.entity.Category;
import com.example.shopkart.repository.CategoryRepository;
import com.example.shopkart.repository.ProductRepository;
import com.example.shopkart.repository.ProductImageRepository;
import com.example.shopkart.repository.CartItemRepository;
import com.example.shopkart.service.AdminProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CartItemRepository cartItemRepository;
    private final AdminProductService adminProductService;

    @Autowired
    public DatabaseSeeder(CategoryRepository categoryRepository, 
                          ProductRepository productRepository, 
                          ProductImageRepository productImageRepository,
                          CartItemRepository cartItemRepository,
                          AdminProductService adminProductService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.cartItemRepository = cartItemRepository;
        this.adminProductService = adminProductService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Seed Categories
        if (categoryRepository.count() == 0) {
            System.out.println("Seeding Categories...");
            categoryRepository.save(new Category("Mobiles"));
            categoryRepository.save(new Category("Laptops"));
            categoryRepository.save(new Category("Headphones"));
            categoryRepository.save(new Category("Smart Watches"));
        }

        // Wipe existing products & carts to re-seed requested products
        System.out.println("Forcing database wipe for new products...");
        cartItemRepository.deleteAll();
        productImageRepository.deleteAll();
        productRepository.deleteAll();

        Category mobiles = categoryRepository.findByCategoryName("Mobiles").orElseThrow();
        Category laptops = categoryRepository.findByCategoryName("Laptops").orElseThrow();
        Category headphones = categoryRepository.findByCategoryName("Headphones").orElseThrow();
        Category watches = categoryRepository.findByCategoryName("Smart Watches").orElseThrow();

        // Mobiles (4 products)
        adminProductService.addProduct("iPhone 15 Pro", "The ultimate iPhone with Titanium frame.", BigDecimal.valueOf(134900.00), 50, mobiles.getCategoryId(), "https://images.unsplash.com/photo-1510557880182-3d4d3cba35a5?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Samsung Galaxy S24 Ultra", "Galaxy AI is here.", BigDecimal.valueOf(129999.00), 40, mobiles.getCategoryId(), "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Google Pixel 8 Pro", "Google's best phone yet.", BigDecimal.valueOf(106999.00), 30, mobiles.getCategoryId(), "https://images.unsplash.com/photo-1598327105666-5b89351aff97?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("OnePlus 12", "Smooth beyond belief.", BigDecimal.valueOf(64999.00), 45, mobiles.getCategoryId(), "https://images.unsplash.com/photo-1580910051074-3eb694886505?auto=format&fit=crop&w=500&q=80");

        // Laptops (4 products)
        adminProductService.addProduct("MacBook Pro 14\"", "M3 Pro chip, 18GB RAM.", BigDecimal.valueOf(199900.00), 20, laptops.getCategoryId(), "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Dell XPS 15", "Premium Windows laptop with OLED screen.", BigDecimal.valueOf(149990.00), 25, laptops.getCategoryId(), "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("ASUS ROG Zephyrus G14", "Powerful gaming laptop.", BigDecimal.valueOf(169990.00), 15, laptops.getCategoryId(), "https://images.unsplash.com/photo-1603302576837-37561b2e2302?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Lenovo ThinkPad X1", "The ultimate business machine.", BigDecimal.valueOf(154990.00), 30, laptops.getCategoryId(), "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?auto=format&fit=crop&w=500&q=80");
        
        // Headphones (4 products)
        adminProductService.addProduct("Sony WH-1000XM5", "Industry-leading noise cancellation.", BigDecimal.valueOf(34990.00), 100, headphones.getCategoryId(), "https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("AirPods Pro 2", "Magical audio experience.", BigDecimal.valueOf(24900.00), 200, headphones.getCategoryId(), "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Bose QuietComfort Ultra", "World-class quiet, better sound.", BigDecimal.valueOf(37900.00), 80, headphones.getCategoryId(), "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Sennheiser Momentum 4", "Superior sound signature.", BigDecimal.valueOf(29990.00), 90, headphones.getCategoryId(), "https://images.unsplash.com/photo-1583394838336-acd977736f90?auto=format&fit=crop&w=500&q=80");

        // Watches (4 products)
        adminProductService.addProduct("Apple Watch Series 9", "Smarter. Brighter. Mightier.", BigDecimal.valueOf(41900.00), 60, watches.getCategoryId(), "https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Garmin Fenix 7", "Ultimate multisport GPS watch.", BigDecimal.valueOf(69990.00), 15, watches.getCategoryId(), "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Samsung Galaxy Watch 6", "Advanced health tracking.", BigDecimal.valueOf(36999.00), 50, watches.getCategoryId(), "https://images.unsplash.com/photo-1579586337278-3befd40fd17a?auto=format&fit=crop&w=500&q=80");
        adminProductService.addProduct("Google Pixel Watch 2", "Help by Google. Health by Fitbit.", BigDecimal.valueOf(39900.00), 40, watches.getCategoryId(), "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=500&q=80");
    }
}
