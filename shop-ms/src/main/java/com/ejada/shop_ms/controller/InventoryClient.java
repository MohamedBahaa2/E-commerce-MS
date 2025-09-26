package com.ejada.shop_ms.controller;

import com.ejada.shop_ms.DTO.InventoryItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-ms")
public interface InventoryClient {
    @PostMapping("/inventory/reduce/{productId}")
    InventoryItem reduceStock(@PathVariable("productId") Long productId,


                              @RequestParam("quantity") Integer quantity);

    @GetMapping("/inventory/{productId}")
    Integer getStock(@PathVariable("productId") Long productId);

}
