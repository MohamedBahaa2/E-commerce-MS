package com.ejada.shop_ms.controller;

import com.ejada.shop_ms.bean.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ejada.shop_ms.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController( CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addItem(@PathVariable Long userId,
                                                               @RequestParam Long productId,
                                                               @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, productId, quantity));
    }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Cart> removeItem(@PathVariable Long userId,
                                                                  @RequestParam Long productId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, productId));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

}
