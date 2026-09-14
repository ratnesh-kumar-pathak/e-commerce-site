package com.example.shopkart;

import com.example.shopkart.entity.*;
import com.example.shopkart.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = ShopKartApplication.class)
public class PaymentServiceTest {

    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private CartRepository cartRepository;

    @Test
    public void testVerifyPayment() {
        try {
            User user = userRepository.findByUsername("sush2").orElseThrow(() -> new RuntimeException("User not found"));
            int userId = user.getUserId();
            String razorpayOrderId = "order_T1F6bMh3CBvja6"; // This order exists based on user's recent log

            System.out.println("Starting database operations...");

            Order order = orderRepository.findById(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setStatus(OrderStatus.SUCCESS);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
            System.out.println("Order updated!");

            List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetails(userId);
            System.out.println("Cart items fetched: " + cartItems.size());

            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductId(cartItem.getProduct().getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPricePerUnit(cartItem.getProduct().getPrice());
                orderItem.setTotalPrice(
                        cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                orderItemRepository.save(orderItem);
                System.out.println("OrderItem saved for product: " + cartItem.getProduct().getProductId());
            }

            cartRepository.deleteAllCartItemsByUserId(userId);
            System.out.println("Cart items deleted!");
            System.out.println("SUCCESS!");
        } catch (Exception e) {
            System.out.println("EXCEPTION THROWN IN DATABASE OPERATIONS:");
            e.printStackTrace();
        }
    }
}
