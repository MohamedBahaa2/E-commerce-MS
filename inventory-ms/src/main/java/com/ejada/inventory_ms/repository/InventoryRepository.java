package com.ejada.inventory_ms.repository;

import com.ejada.inventory_ms.bean.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem,Long> {
    Optional<InventoryItem> findByProductId(Long productId);
}
