package com.ejada.shop_ms.repository;

import com.ejada.shop_ms.bean.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    List<CartItem> findByCartId(Long cartId);

    @Modifying
    @Transactional
    void deleteByCartIdAndProductId(Long cartId, Long productId);

    @Modifying
    @Transactional
    void deleteByCartId(Long cartId);
}
