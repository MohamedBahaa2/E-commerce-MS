package com.ejada.shop_ms.service;

import com.ejada.shop_ms.bean.Cart;
import com.ejada.shop_ms.bean.CartItem;
import com.ejada.shop_ms.bean.Product;
import com.ejada.shop_ms.repository.CartItemRepository;
import com.ejada.shop_ms.repository.CartRepository;
import com.ejada.shop_ms.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart getCartByUserId(Long userId) {
        /* .orElse(null)
        * */
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(1000L, userId)));
        return cart;
    }

    public Cart addItemToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = getCartByUserId(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElse(new CartItem(null, cart.getId(), productId, 0));

        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);

        return cart;
    }

    public Cart removeItemFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
        return cart;
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cartItemRepository.deleteByCartId(cart.getId());
    }

}
