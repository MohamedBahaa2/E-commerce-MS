package com.ejada.shop_ms.service;

import com.ejada.shop_ms.DTO.PaymentRequest;
import com.ejada.shop_ms.DTO.PaymentResponse;
import com.ejada.shop_ms.bean.Cart;
import com.ejada.shop_ms.bean.CartItem;
import com.ejada.shop_ms.bean.Order;
import com.ejada.shop_ms.bean.Product;
import com.ejada.shop_ms.controller.WalletClient;
import com.ejada.shop_ms.repository.CartItemRepository;
import com.ejada.shop_ms.repository.OrderRepository;
import com.ejada.shop_ms.repository.ProductRepository;
import com.ejada.shop_ms.controller.InventoryClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private final com.ejada.shop_ms.service.CartService cartService;
    private final WalletClient walletClient; // Feign client to Wallet MS
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, com.ejada.shop_ms.service.CartService cartService, WalletClient walletClient, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.walletClient = walletClient;
        this.inventoryClient = inventoryClient;
    }
/*
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = items.stream()
                .mapToDouble(i -> productRepository.findById(i.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"))
                        .getPrice() * i.getQuantity())
                .sum();

        // Call wallet MS for payment
        PaymentRequest request = new PaymentRequest(userId, total);
        PaymentResponse response = walletClient.pay(request);

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setStatus(response.isSuccess() ? "PAID" : "FAILED");
        order = orderRepository.save(order);

        if (response.isSuccess()) {
            cartService.clearCart(userId); // clear cart after successful order
        }

        return order;
    }
*/
   // @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackPlaceOrder")
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
//        System.out.println(cart + "cart");
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
//        System.out.println(items + "items");

        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = 0.0;

        // check inventory for each product
        for (CartItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Integer availableStock = inventoryClient.getStock(product.getId());
            if (availableStock < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // reduce stock immediately (reserve the items)
            inventoryClient.reduceStock(product.getId(), item.getQuantity());

            total += product.getPrice() * item.getQuantity();
        }

        // call wallet MS for payment
        PaymentRequest paymentRequest = new PaymentRequest(userId, total);
        PaymentResponse paymentResponse = walletClient.pay(paymentRequest);

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);

        if (paymentResponse.isSuccess()) {
            order.setStatus("PAID");
            cartService.clearCart(userId); // clear cart after successful order
        } else {
            order.setStatus("FAILED");
            // ❌ In a real system, you’d rollback inventory reservation here
        }

        return orderRepository.save(order);
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    public Order fallbackPlaceOrder(Long userId, Throwable t) {
        System.out.println("⚠️ Circuit breaker triggered for placeOrder: " + t.getMessage());

        Order failedOrder = new Order();
        failedOrder.setUserId(userId);
        failedOrder.setStatus("FAILED_CIRCUIT_BREAKER");
        failedOrder.setTotalAmount(0.0);

        return failedOrder;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}



