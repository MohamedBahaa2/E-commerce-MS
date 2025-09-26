package com.ejada.inventory_ms.service;

import com.ejada.inventory_ms.bean.InventoryItem;
import com.ejada.inventory_ms.repository.InventoryRepository;

import org.springframework.stereotype.Service;

@Service

public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository){
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryItem addStock(Long productId, Integer quantity) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElse(new InventoryItem());
        item.setProductId(productId);
        item.setQuantity(item.getQuantity() == null ? quantity : item.getQuantity() + quantity);
        return inventoryRepository.save(item);
    }

    public InventoryItem reduceStock(Long productId, Integer quantity) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found in inventory"));

        if (item.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock for product " + productId);
        }
        item.setQuantity(item.getQuantity() - quantity);
        return inventoryRepository.save(item);
    }

    public Integer getStock(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(InventoryItem::getQuantity)
                .orElse(0);
    }
}
