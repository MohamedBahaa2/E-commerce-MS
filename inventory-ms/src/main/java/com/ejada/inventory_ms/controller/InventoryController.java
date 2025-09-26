package com.ejada.inventory_ms.controller;

import com.ejada.inventory_ms.bean.InventoryItem;
import com.ejada.inventory_ms.service.InventoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;

    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<InventoryItem> addStock(@PathVariable Long productId,
                                                  @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.addStock(productId, quantity));
    }

    @PostMapping("/reduce/{productId}")
    public ResponseEntity<InventoryItem> reduceStock(@PathVariable Long productId,
                                                     @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.reduceStock(productId, quantity));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Integer> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStock(productId));
    }

}
